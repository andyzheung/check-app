# 前后端集成测试计划与问题解决方案

**文档编号**: 040  
**创建时间**: 2025-01-27  
**文档版本**: v1.0  
**作者**: Claude Code  

---

## 📋 **问题分析**

### **1. 开发环境兼容性问题**
**问题**: 当前K8s部署方案中，admin-web的vue.config.js只配置了API代理，但没有配置二维码静态资源代理，导致本地开发时无法访问二维码。

**影响**: 本地开发调试受阻，开发效率降低。

### **2. App/web前端数据显示问题**
**问题分析**:
1. **首页统计数据为0**: 后端`/api/v1/tasks/today/stats`接口可能返回空数据
2. **待完成任务为空**: 后端`/api/v1/tasks`接口可能没有数据或状态不匹配
3. **巡检记录详情显示不完整**: 动态显示逻辑可能有问题，环境巡检和安全巡检项为0

---

## 🎯 **解决方案**

### **方案1: 开发环境兼容性修复**

#### **1.1 修复admin-web本地开发配置**

**文件**: `admin-web/vue.config.js`

```javascript
module.exports = {
  devServer: {
    port: 8082,
    open: true,
    proxy: {
      '/api/v1': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      // 新增：二维码静态资源代理
      '/qrcode': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      // 新增：上传文件静态资源代理
      '/uploads': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
}
```

#### **1.2 修复App/web本地开发配置**

**文件**: `App/web/vite.config.js`

```javascript
// 在现有proxy配置中添加静态资源代理
server: {
  port: 5173,
  open: true,
  host: true,
  cors: true,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
      secure: false,
      ws: true
    },
    // 新增：二维码静态资源代理
    '/qrcode': {
      target: 'http://localhost:8080',
      changeOrigin: true,
      secure: false
    },
    // 新增：上传文件静态资源代理
    '/uploads': {
      target: 'http://localhost:8080',
      changeOrigin: true,
      secure: false
    }
  }
}
```

### **方案2: 后端数据接口问题修复**

#### **2.1 任务统计接口数据问题**

**问题定位**: `TaskServiceImpl.getTodayStats()`方法依赖用户登录状态和数据库中的任务数据。

**修复方案**:
1. 确保用户认证正常
2. 检查数据库中是否有今日任务数据
3. 添加默认数据返回逻辑

#### **2.2 巡检记录详情显示问题**

**问题定位**: `RecordDetail.vue`中的动态显示逻辑依赖后端返回的`environmentItems`和`securityItems`数组。

**根本原因**: 
- 后端`InspectionRecordDetailDTO`结构与前端期望不匹配
- 前端组件中的数据绑定路径可能有误

---

## 🔧 **端到端测试方案**

### **测试环境准备**

#### **1. 本地开发环境**
```bash
# 启动后端服务
cd check-app-server
mvn spring-boot:run

# 启动admin-web前端
cd admin-web
npm run serve

# 启动App/web前端
cd App/web
npm run dev
```

#### **2. 数据库准备**
```sql
-- 插入测试用户
INSERT INTO t_user (username, password, real_name, role, status, create_time, update_time, deleted) 
VALUES ('testuser', '$2a$10$...', '测试用户', 'user', 'active', NOW(), NOW(), 0);

-- 插入测试区域
INSERT INTO t_area (area_code, area_name, area_type, status, create_time, update_time, deleted) 
VALUES ('TEST001', '测试数据中心', 'D', 'active', NOW(), NOW(), 0);

-- 插入测试任务
INSERT INTO t_inspection_task (task_name, area_id, inspector_id, plan_time, status, create_time, update_time, deleted)
VALUES ('今日测试任务', 1, 1, CURDATE(), 'PENDING', NOW(), NOW(), 0);

-- 插入测试巡检记录
INSERT INTO t_inspection_record (record_no, area_id, inspector_id, start_time, end_time, status, create_time, update_time, deleted)
VALUES ('R20250127001', 1, 1, NOW(), NOW(), 'normal', NOW(), NOW(), 0);

-- 插入测试巡检项
INSERT INTO t_inspection_item (record_id, name, type, status, remark, create_time, update_time, deleted)
VALUES 
(1, '机房温度检查', 'environment', 'normal', '温度正常', NOW(), NOW(), 0),
(1, '安全门禁检查', 'security', 'normal', '门禁正常', NOW(), NOW(), 0);
```

### **测试用例**

#### **测试用例1: 首页统计数据显示**

**目标**: 验证首页今日任务、已完成、待完成统计数据正确显示

**前置条件**:
1. 用户已登录
2. 数据库中有今日任务数据

**测试步骤**:
1. 访问App/web首页
2. 检查任务统计卡片显示
3. 验证API调用: `GET /api/v1/tasks/today/stats`

**预期结果**:
```json
{
  "code": 200,
  "data": {
    "totalTasks": 5,
    "completedTasks": 2,
    "pendingTasks": 3
  }
}
```

**验证方法**:
```javascript
// 在浏览器控制台执行
fetch('/api/v1/tasks/today/stats', {
  headers: {
    'Authorization': 'Bearer ' + localStorage.getItem('token')
  }
})
.then(res => res.json())
.then(data => console.log('任务统计:', data));
```

#### **测试用例2: 待完成任务列表显示**

**目标**: 验证待完成任务列表正确显示

**测试步骤**:
1. 在首页点击"查看全部"
2. 检查任务列表显示
3. 验证API调用: `GET /api/v1/tasks?status=PENDING`

**预期结果**: 显示待完成任务列表，每个任务包含区域名称、计划时间等信息

#### **测试用例3: 巡检记录详情显示**

**目标**: 验证巡检记录详情页面环境巡检和安全巡检项正确显示

**测试步骤**:
1. 进入巡检记录列表
2. 点击某条记录查看详情
3. 检查环境巡检和安全巡检项显示

**预期结果**: 
- 环境巡检项列表显示正常
- 安全巡检项列表显示正常
- 每个巡检项包含名称、状态、备注等信息

**API验证**:
```javascript
// 验证记录详情API
fetch('/api/v1/records/1', {
  headers: {
    'Authorization': 'Bearer ' + localStorage.getItem('token')
  }
})
.then(res => res.json())
.then(data => {
  console.log('记录详情:', data);
  console.log('环境巡检项数量:', data.data.environmentItems?.length || 0);
  console.log('安全巡检项数量:', data.data.securityItems?.length || 0);
});
```

#### **测试用例4: 二维码功能测试**

**目标**: 验证二维码生成和访问功能在本地开发环境正常工作

**测试步骤**:
1. 在admin-web中生成区域二维码
2. 验证二维码图片能正常显示
3. 扫描二维码进入巡检表单

**API验证**:
```bash
# 生成二维码
curl -X POST http://localhost:8082/api/v1/areas/1/qrcode \
  -H "Authorization: Bearer YOUR_TOKEN"

# 访问二维码图片
curl -I http://localhost:8082/qrcode/AREA001_xxx.png
```

---

## 🚀 **实施步骤**

### **第一阶段: 配置修复**
1. 修复admin-web和App/web的本地开发代理配置
2. 验证本地开发环境可以正常访问所有资源

### **第二阶段: 数据接口调试**
1. 检查后端任务统计接口实现
2. 验证数据库中的测试数据
3. 调试前端API调用逻辑

### **第三阶段: 端到端测试**
1. 执行完整的用户流程测试
2. 验证所有功能模块正常工作
3. 记录和修复发现的问题

### **第四阶段: 生产环境验证**
1. 在K8s环境中部署和测试
2. 验证Nginx代理配置正确
3. 确保生产环境功能正常

---

## 📊 **测试检查清单**

### **开发环境兼容性**
- [ ] admin-web本地开发可以访问二维码
- [ ] App/web本地开发可以访问二维码
- [ ] 所有API接口在本地正常工作

### **数据显示功能**
- [ ] 首页统计数据正确显示
- [ ] 待完成任务列表正确显示
- [ ] 巡检记录详情完整显示
- [ ] 环境巡检项数量正确
- [ ] 安全巡检项数量正确

### **用户流程**
- [ ] 用户登录流程正常
- [ ] 二维码扫描流程正常
- [ ] 巡检表单提交流程正常
- [ ] 记录查看流程正常

### **生产环境**
- [ ] K8s部署成功
- [ ] Nginx代理配置正确
- [ ] 静态资源访问正常
- [ ] 所有功能在生产环境正常

---

## 🔍 **问题排查指南**

### **API调用失败**
```javascript
// 检查网络请求
// 在浏览器开发者工具Network标签中查看请求状态
// 检查请求头、响应状态码、响应内容

// 检查认证状态
console.log('Token:', localStorage.getItem('token'));

// 检查API基础URL
console.log('API Base URL:', process.env.VUE_APP_API_BASE_URL);
```

### **数据显示异常**
```javascript
// 检查组件数据绑定
// 在Vue DevTools中查看组件状态
// 检查computed属性和methods

// 检查API响应数据结构
console.log('API响应:', response.data);
console.log('数据类型:', typeof response.data);
console.log('数组长度:', Array.isArray(response.data) ? response.data.length : 'Not Array');
```

### **后端日志检查**
```bash
# 查看Spring Boot应用日志
tail -f logs/application.log

# 查看特定接口调用日志
grep "tasks/today/stats" logs/application.log

# 查看SQL执行日志
grep "SELECT" logs/application.log
```

---

## 📈 **性能优化建议**

### **前端优化**
1. 使用loading状态提升用户体验
2. 实现数据缓存减少重复请求
3. 添加错误处理和重试机制

### **后端优化**
1. 添加接口响应时间监控
2. 优化数据库查询性能
3. 实现结果缓存机制

### **网络优化**
1. 启用gzip压缩
2. 配置合适的缓存策略
3. 使用CDN加速静态资源

---

## 🎯 **总结**

这个测试计划解决了以下关键问题：

1. **开发环境兼容性**: 通过修复代理配置，确保本地开发环境正常工作
2. **数据显示问题**: 通过端到端测试，定位和修复数据显示异常
3. **系统集成测试**: 提供完整的测试用例和验证方法
4. **问题排查指南**: 提供详细的调试和排查方法

通过执行这个测试计划，可以确保前后端集成功能正常，提升开发效率和用户体验。 