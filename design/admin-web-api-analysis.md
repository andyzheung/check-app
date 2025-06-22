# Admin-Web 接口需求分析

**创建日期**: 2025-01-27  
**版本**: v1.0  

## 🎯 分析目标

梳理admin-web管理后台所需的所有接口，对比check-app-server现有接口，找出缺失的接口并制定最小代价补充方案。

## 📊 Admin-Web 页面功能分析

### 1. 仪表盘 (Dashboard)
**页面**: `/dashboard`  
**功能**: 统计数据展示、图表分析、最新问题列表

**需要的接口**:
- ✅ `GET /api/v1/statistics/dashboard` - 获取仪表盘数据
- ❌ `GET /api/v1/issues/weekly` - 获取本周问题列表 **(缺失)**
- ❌ `GET /api/v1/statistics/inspectors/ranking` - 获取巡检人员排名 **(缺失)**

### 2. 巡检记录 (Records)
**页面**: `/records/list`, `/records/export`  
**功能**: 记录列表、筛选、导出

**需要的接口**:
- ✅ `GET /api/v1/records` - 获取记录列表
- ✅ `GET /api/v1/records/{id}` - 获取记录详情
- ✅ `GET /api/v1/records/export` - 导出记录
- ❌ `GET /api/v1/records/export/history` - 获取导出历史 **(缺失)**

### 3. 问题管理 (Issues)
**页面**: `/issues/list`, `/issues/statistics`  
**功能**: 问题列表、问题统计分析

**需要的接口**:
- ✅ `GET /api/v1/issues` - 获取问题列表
- ✅ `GET /api/v1/issues/{id}` - 获取问题详情
- ✅ `PUT /api/v1/issues/{id}` - 更新问题
- ✅ `DELETE /api/v1/issues/{id}` - 删除问题
- ✅ `GET /api/v1/statistics/issues` - 获取问题统计
- ✅ `GET /api/v1/statistics/issues/trend` - 获取问题趋势
- ✅ `GET /api/v1/statistics/issues/by-area` - 获取区域问题分布
- ❌ `GET /api/v1/statistics/issues/by-handler` - 获取问题处理人员排名 **(缺失)**

### 4. 用户管理 (Users)
**页面**: `/users/list`  
**功能**: 用户列表、权限管理、AD用户管理

**需要的接口**:
- ✅ `GET /api/v1/users/page` - 获取用户列表
- ✅ `POST /api/v1/users` - 创建用户
- ✅ `PUT /api/v1/users/{id}` - 更新用户
- ✅ `DELETE /api/v1/users/{id}` - 删除用户
- ✅ `GET /api/v1/users/{userId}/permissions` - 获取用户权限
- ✅ `PUT /api/v1/users/permissions` - 更新用户权限
- ✅ `GET /api/admin/ad-users/search` - 搜索AD用户
- ✅ `GET /api/admin/ad-users` - 获取所有AD用户
- ✅ `POST /api/admin/ad-users/{adUsername}/assign-role` - 为AD用户分配角色
- ❌ `GET /api/v1/permissions` - 获取所有权限列表 **(缺失)**

### 5. 系统配置 (Config)
**页面**: `/areas/config`  
**功能**: 区域配置、系统参数、AD配置

**需要的接口**:
- ✅ `GET /api/v1/areas` - 获取区域列表
- ✅ `PUT /api/v1/areas/{id}/config` - 更新区域配置
- ✅ `GET /api/v1/system/params` - 获取所有系统参数
- ✅ `POST /api/v1/system/params` - 设置系统参数
- ✅ `DELETE /api/v1/system/params/{key}` - 删除系统参数
- ❌ `GET /api/v1/ad/config` - 获取AD配置 **(缺失)**
- ❌ `PUT /api/v1/ad/config` - 更新AD配置 **(缺失)**
- ❌ `POST /api/v1/ad/sync` - 手动同步AD用户 **(缺失)**

### 6. 巡检排班 (Schedule)
**页面**: 暂未实现，计划中  
**功能**: 任务排班、日历视图

**需要的接口**:
- ❌ `GET /api/v1/schedules` - 获取排班列表 **(缺失)**
- ❌ `POST /api/v1/schedules` - 创建排班 **(缺失)**
- ❌ `PUT /api/v1/schedules/{id}` - 更新排班 **(缺失)**
- ❌ `DELETE /api/v1/schedules/{id}` - 删除排班 **(缺失)**
- ❌ `GET /api/v1/schedules/calendar` - 获取日历视图数据 **(缺失)**

## 🔧 缺失接口汇总

### 高优先级（当前页面需要）
1. **问题相关**
   - `GET /api/v1/issues/weekly` - 获取本周问题列表
   - `GET /api/v1/statistics/issues/by-handler` - 问题处理人员排名

2. **统计相关**
   - `GET /api/v1/statistics/inspectors/ranking` - 巡检人员排名

3. **用户权限相关**
   - `GET /api/v1/permissions` - 获取所有权限列表

4. **导出相关**
   - `GET /api/v1/records/export/history` - 导出历史记录

### 中优先级（系统配置需要）
5. **AD配置相关**
   - `GET /api/v1/ad/config` - 获取AD配置
   - `PUT /api/v1/ad/config` - 更新AD配置
   - `POST /api/v1/ad/sync` - 手动同步AD用户

### 低优先级（排班功能）
6. **排班管理相关**
   - `GET /api/v1/schedules` - 获取排班列表
   - `POST /api/v1/schedules` - 创建排班
   - `PUT /api/v1/schedules/{id}` - 更新排班
   - `DELETE /api/v1/schedules/{id}` - 删除排班
   - `GET /api/v1/schedules/calendar` - 获取日历视图数据

## 🚀 最小代价实现方案

### Phase 1: 修复当前页面运行问题 (0.5天)

#### 1.1 扩展现有Controller
在现有Controller中添加缺失的方法：

```java
// IssueController.java 添加
@GetMapping("/weekly")
public Result<List<IssueDTO>> getWeeklyIssues() {
    // 获取本周问题列表
}

// StatisticsController.java 添加
@GetMapping("/inspectors/ranking")
public Result<List<InspectorRankingVO>> getInspectorRanking() {
    // 获取巡检人员排名
}

@GetMapping("/issues/by-handler")
public Result<List<IssueHandlerVO>> getIssueByHandler() {
    // 获取问题处理人员统计
}

// UserController.java 添加
@GetMapping("/permissions")
public Result<List<PermissionDTO>> getAllPermissions() {
    // 获取所有权限列表
}
```

#### 1.2 简化实现策略
- 使用现有Service层逻辑
- 返回模拟数据或简化统计
- 确保接口可调用，数据后续完善

### Phase 2: AD配置管理 (0.5天)

#### 2.1 新增ADConfigController
```java
@RestController
@RequestMapping("/api/v1/ad")
public class ADConfigController {
    
    @GetMapping("/config")
    public Result<Map<String, String>> getADConfig() {
        // 从系统参数表读取AD配置
    }
    
    @PutMapping("/config")
    public Result<Void> updateADConfig(@RequestBody Map<String, String> config) {
        // 更新AD配置到系统参数表
    }
    
    @PostMapping("/sync")
    public Result<ADSyncResultVO> syncADUsers() {
        // 手动同步AD用户
    }
}
```

### Phase 3: 排班功能 (1天)
- 实现ScheduleController
- 完善排班相关Service
- 前端排班日历组件

## 📋 实施检查清单

### ✅ 已完成
- [x] ESLint错误修复
- [x] 现有接口梳理
- [x] 缺失接口分析

### 🔄 进行中
- [ ] Phase 1: 修复当前页面运行问题
- [ ] Phase 2: AD配置管理
- [ ] Phase 3: 排班功能

### ⏳ 待办
- [ ] 接口文档更新
- [ ] 前端API调用适配
- [ ] 集成测试

## 🎯 成功标准

1. **功能完整性**: admin-web所有页面正常运行
2. **接口一致性**: API路径和数据格式统一
3. **代码质量**: 遵循现有代码规范
4. **最小影响**: 不破坏现有功能
5. **可扩展性**: 便于后续功能扩展 