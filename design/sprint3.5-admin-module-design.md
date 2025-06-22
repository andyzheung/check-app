# Sprint 3.5 管理后台模块配置设计

**制定日期**: 2025-01-27  
**版本**: v1.0  
**负责人**: 开发团队  

## 🎯 设计目标

基于现有数据库结构，以**最小代价**原则实现：
1. 管理后台的区域模块配置功能
2. AD集成的打桩方案
3. 用户权限管理增强
4. 巡检任务排班功能

## 📊 当前数据库结构分析

### 现有表结构优势
- ✅ `t_area` 表已有 `module_count` 和 `config_json` 字段
- ✅ `t_user` 表结构完整，支持角色管理
- ✅ `t_user_permission` 表支持细粒度权限控制
- ✅ `t_inspection_task` 表支持任务排班
- ✅ 数据中心已有模块配置示例数据

### 需要最小扩展的部分
- 🔧 `t_user` 表增加AD相关字段
- 🔧 新增AD用户同步机制
- 🔧 管理后台Vue组件开发

## 🏗️ 最小代价扩展原则

### 1. 数据库扩展策略
**原则**: 不破坏现有结构，只增加必要字段

```sql
-- 对 t_user 表的最小扩展
ALTER TABLE `t_user` 
ADD COLUMN `ad_username` varchar(100) DEFAULT NULL COMMENT 'AD域账号',
ADD COLUMN `is_ad_user` tinyint DEFAULT 0 COMMENT '是否AD用户：0-本地用户，1-AD用户',
ADD COLUMN `ad_sync_time` datetime DEFAULT NULL COMMENT 'AD同步时间';

-- 添加索引
ALTER TABLE `t_user` ADD INDEX `idx_ad_username` (`ad_username`);
```

### 2. AD打桩方案
**原则**: 模拟AD环境，便于后续真实AD集成

#### 2.1 AD用户数据打桩
```json
// 模拟AD用户数据
{
  "ad_users": [
    {
      "username": "zhang.san",
      "displayName": "张三",
      "department": "运维部",
      "email": "zhang.san@company.com",
      "phone": "13800138001"
    },
    {
      "username": "li.si", 
      "displayName": "李四",
      "department": "网络部",
      "email": "li.si@company.com",
      "phone": "13800138002"
    }
  ]
}
```

#### 2.2 AD服务打桩类
```java
@Service
@Profile("!prod") // 非生产环境使用打桩
public class MockAdService implements AdService {
    
    @Override
    public List<AdUser> searchUsers(String keyword) {
        // 返回模拟的AD用户列表
        return mockAdUsers.stream()
            .filter(user -> user.getDisplayName().contains(keyword) 
                         || user.getUsername().contains(keyword))
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean validateUser(String username, String password) {
        // 模拟AD认证，开发阶段返回true
        return mockAdUsers.stream()
            .anyMatch(user -> user.getUsername().equals(username));
    }
}
```

## 🔧 功能实现方案

### 1. 区域模块配置管理

#### 1.1 后端API设计
```java
@RestController
@RequestMapping("/api/admin/area-config")
public class AreaConfigController {
    
    @GetMapping("/{areaId}/modules")
    public Result<AreaModuleConfig> getModuleConfig(@PathVariable Long areaId) {
        // 从 t_area.config_json 读取模块配置
    }
    
    @PutMapping("/{areaId}/modules")
    public Result<Void> updateModuleConfig(@PathVariable Long areaId, 
                                         @RequestBody AreaModuleConfig config) {
        // 更新 t_area.module_count 和 config_json
    }
}
```

#### 1.2 前端管理界面
- 复用现有 `admin-web` 项目
- 新增 `AreaConfig.vue` 组件
- 支持数据中心模块数量配置
- 支持模块名称自定义

### 2. 用户权限管理增强

#### 2.1 权限分配界面
```vue
<!-- UserPermission.vue -->
<template>
  <div class="user-permission">
    <el-table :data="adUsers">
      <el-table-column prop="displayName" label="姓名" />
      <el-table-column prop="department" label="部门" />
      <el-table-column label="角色">
        <template #default="{ row }">
          <el-select v-model="row.role" @change="updateUserRole(row)">
            <el-option label="管理员" value="admin" />
            <el-option label="巡检员" value="inspector" />
            <el-option label="普通用户" value="user" />
          </el-select>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>
```

#### 2.2 权限控制逻辑
- 管理员可以给AD用户分配角色
- 支持批量权限操作
- 权限变更日志记录

### 3. 巡检任务排班

#### 3.1 排班日历组件
```vue
<!-- TaskSchedule.vue -->
<template>
  <div class="task-schedule">
    <el-calendar v-model="selectedDate">
      <template #dateCell="{ data }">
        <div class="schedule-cell">
          <div class="date">{{ data.day.split('-').slice(2).join('') }}</div>
          <div class="tasks">
            <div v-for="task in getTasksByDate(data.day)" 
                 :key="task.id" 
                 class="task-item">
              {{ task.areaName }} - {{ task.inspectorName }}
            </div>
          </div>
        </div>
      </template>
    </el-calendar>
  </div>
</template>
```

#### 3.2 排班管理API
```java
@RestController
@RequestMapping("/api/admin/schedule")
public class ScheduleController {
    
    @PostMapping("/assign")
    public Result<Void> assignTask(@RequestBody TaskAssignRequest request) {
        // 创建 t_inspection_task 记录
    }
    
    @GetMapping("/calendar")
    public Result<List<TaskScheduleVO>> getScheduleCalendar(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        // 返回日历视图数据
    }
}
```

## 📱 开发计划

### Phase 1: 数据库扩展 (0.5天)
- ✅ 当前数据库结构已支持大部分功能
- 🔧 仅需添加AD相关字段
- 🔧 插入模拟AD用户数据

### Phase 2: 后端打桩服务 (0.5天)
- 🔧 实现MockAdService
- 🔧 完善AreaConfigController
- 🔧 增强UserController权限管理

### Phase 3: 前端管理界面 (1天)
- 🔧 AreaConfig.vue - 区域模块配置
- 🔧 UserPermission.vue - 用户权限管理  
- 🔧 TaskSchedule.vue - 任务排班日历

### Phase 4: 集成测试 (0.5天)
- 🔧 功能测试
- 🔧 权限验证
- 🔧 数据一致性检查

## 🚀 技术实现细节

### 1. 模块配置数据结构
```json
{
  "modules": [
    {
      "id": 1,
      "name": "计算模块1",
      "type": "compute",
      "location": "A区-01柜"
    },
    {
      "id": 2, 
      "name": "存储模块1",
      "type": "storage",
      "location": "A区-02柜"
    }
  ],
  "layout": {
    "rows": 2,
    "cols": 3
  }
}
```

### 2. AD打桩数据初始化
```sql
-- 插入模拟AD用户
INSERT INTO `t_user` (`username`, `password`, `real_name`, `role`, `status`, `ad_username`, `is_ad_user`) VALUES
('zhang.san', '$2a$10$mock.hash', '张三', 'inspector', 'active', 'zhang.san', 1),
('li.si', '$2a$10$mock.hash', '李四', 'inspector', 'active', 'li.si', 1),
('wang.wu', '$2a$10$mock.hash', '王五', 'user', 'active', 'wang.wu', 1);
```

### 3. 权限管理逻辑
```java
@Service
public class UserPermissionService {
    
    public void assignRole(Long userId, String role) {
        // 1. 更新用户角色
        userMapper.updateRole(userId, role);
        
        // 2. 分配对应权限
        List<String> permissions = getRolePermissions(role);
        userPermissionMapper.batchInsert(userId, permissions);
        
        // 3. 记录操作日志
        logService.recordPermissionChange(userId, role);
    }
}
```

## 🔍 风险控制

### 1. 数据安全
- AD打桩数据不包含真实密码
- 权限变更需要审计日志
- 敏感操作需要二次确认

### 2. 系统稳定性
- 最小化数据库结构变更
- 保持向后兼容性
- 分阶段部署验证

### 3. 用户体验
- 界面操作简单直观
- 错误提示清晰明确
- 支持批量操作提高效率

## 📋 验收标准

### 1. 功能验收
- ✅ 管理员可以配置数据中心模块数量
- ✅ 管理员可以给AD用户分配权限
- ✅ 管理员可以安排巡检任务排班
- ✅ 移动端根据后台配置显示正确模块数

### 2. 性能验收
- ✅ 配置页面加载时间 < 2秒
- ✅ 权限分配操作响应时间 < 1秒
- ✅ 排班日历渲染时间 < 3秒

### 3. 安全验收
- ✅ 非管理员无法访问配置功能
- ✅ 权限变更有完整审计日志
- ✅ AD打桩不泄露真实用户信息

---

**设计原则**: 最小代价、最大复用、渐进演进  
**实施策略**: 打桩先行、功能验证、逐步完善 