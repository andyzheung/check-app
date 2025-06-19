# 排班系统与用户角色管理的数据库和API更新设计

本文档详细描述了为支持排班系统、用户角色管理和AD域账号集成而需要进行的数据库和API更新。

## 1. 数据库更新

### 1.1 用户表（t_user）修改

需要对用户表进行修改，增加对运维人员角色的支持以及AD域账号相关字段：

```sql
-- 修改角色字段
ALTER TABLE t_user 
MODIFY COLUMN role VARCHAR(20) NOT NULL DEFAULT 'user' 
COMMENT '角色：admin-管理员，operator-运维人员，user-普通用户';

-- 添加AD域账号相关字段
ALTER TABLE t_user 
ADD COLUMN ad_account VARCHAR(100) COMMENT 'AD域账号',
ADD COLUMN ad_domain VARCHAR(100) COMMENT 'AD域名',
ADD COLUMN ad_last_sync DATETIME COMMENT 'AD最后同步时间',
ADD COLUMN is_ad_user TINYINT DEFAULT 0 COMMENT '是否AD用户：0-否，1-是',
ADD UNIQUE INDEX uk_ad_account (ad_account, ad_domain, deleted);
```

### 1.2 权限表补充

为支持排班系统和用户角色管理，需要在系统中添加新的权限记录（不需要修改表结构，只需添加数据）：

```sql
INSERT INTO t_user_permission (user_id, permission_code, permission_name)
VALUES 
(1, 'schedule_view', '排班查看权限'),
(1, 'schedule_edit', '排班编辑权限'),
(1, 'schedule_all', '查看所有排班权限'),
(1, 'area_manage', '区域管理权限'),
(1, 'statistics_view', '统计信息查看权限'),
(1, 'ad_config', 'AD配置管理权限'),
(1, 'ad_sync', 'AD用户同步权限');

-- 为现有运维人员添加权限
INSERT INTO t_user_permission (user_id, permission_code, permission_name)
SELECT u.id, 'schedule_view', '排班查看权限'
FROM t_user u WHERE u.role = 'operator' AND u.deleted = 0;

INSERT INTO t_user_permission (user_id, permission_code, permission_name)
SELECT u.id, 'statistics_view', '统计信息查看权限'
FROM t_user u WHERE u.role = 'operator' AND u.deleted = 0;
```

### 1.3 新增AD配置表和同步日志表

```sql
-- AD配置表
CREATE TABLE IF NOT EXISTS t_ad_config (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
    config_key VARCHAR(50) NOT NULL COMMENT '配置键',
    config_value VARCHAR(500) NOT NULL COMMENT '配置值',
    description VARCHAR(200) COMMENT '描述',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_config_key (config_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AD配置表';

-- AD同步日志表
CREATE TABLE IF NOT EXISTS t_ad_sync_log (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    sync_type VARCHAR(20) NOT NULL COMMENT '同步类型：full-全量同步，incremental-增量同步，single-单用户同步',
    start_time DATETIME NOT NULL COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    total_count INT DEFAULT 0 COMMENT '总处理数量',
    success_count INT DEFAULT 0 COMMENT '成功数量',
    fail_count INT DEFAULT 0 COMMENT '失败数量',
    status VARCHAR(20) NOT NULL DEFAULT 'processing' COMMENT '状态：processing-处理中，success-成功，fail-失败',
    error_message VARCHAR(500) COMMENT '错误信息',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AD同步日志表';
```

## 2. API更新

### 2.1 用户管理API更新

#### 2.1.1 创建/更新用户API

需要修改用户创建和更新API，支持运维人员角色和AD域账号：

```java
/**
 * 创建用户
 */
@PostMapping("/users")
public Result<Long> createUser(@RequestBody @Valid UserCreateDTO userDTO) {
    // 验证角色是否有效
    if (!Arrays.asList("admin", "operator", "user").contains(userDTO.getRole())) {
        return Result.fail("无效的用户角色");
    }
    
    // 处理AD用户信息
    if (userDTO.getIsAdUser() != null && userDTO.getIsAdUser()) {
        if (StringUtils.isEmpty(userDTO.getAdAccount()) || StringUtils.isEmpty(userDTO.getAdDomain())) {
            return Result.fail("AD用户必须提供AD账号和域名");
        }
    }
    
    // 其他逻辑保持不变
    Long userId = userService.createUser(userDTO);
    return Result.success(userId);
}

/**
 * 更新用户
 */
@PutMapping("/users/{id}")
public Result<Void> updateUser(@PathVariable Long id, @RequestBody @Valid UserUpdateDTO userDTO) {
    // 验证角色是否有效
    if (userDTO.getRole() != null && !Arrays.asList("admin", "operator", "user").contains(userDTO.getRole())) {
        return Result.fail("无效的用户角色");
    }
    
    // 处理AD用户信息
    if (userDTO.getIsAdUser() != null) {
        // 如果要将用户设为AD用户，需要提供AD账号和域名
        if (userDTO.getIsAdUser() && 
            (StringUtils.isEmpty(userDTO.getAdAccount()) || StringUtils.isEmpty(userDTO.getAdDomain()))) {
            return Result.fail("AD用户必须提供AD账号和域名");
        }
    }
    
    // 其他逻辑保持不变
    userService.updateUser(id, userDTO);
    return Result.success();
}
```

#### 2.1.2 获取所有权限API

添加一个新的API，用于获取系统中的所有权限，包括AD相关权限：

```java
/**
 * 获取所有权限
 */
@GetMapping("/permissions")
public Result<List<PermissionDTO>> getAllPermissions() {
    List<PermissionDTO> permissions = permissionService.getAllPermissions();
    return Result.success(permissions);
}
```

### 2.2 排班管理API

#### 2.2.1 获取排班列表

```java
/**
 * 获取排班列表
 */
@GetMapping("/schedules")
public Result<PageResult<ScheduleDTO>> getSchedules(
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        @RequestParam(required = false) Long areaId,
        @RequestParam(required = false) Long userId,
        @RequestParam(required = false) String shift,
        @RequestParam(required = false) String status,
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize) {
    
    // 权限检查
    Long currentUserId = SecurityUtils.getCurrentUserId();
    boolean hasAllPermission = SecurityUtils.hasPermission("schedule_all");
    
    // 如果不是管理员且没有查看所有排班的权限，只能查看自己的排班
    if (!SecurityUtils.isAdmin() && !hasAllPermission) {
        userId = currentUserId;
    }
    
    PageResult<ScheduleDTO> result = scheduleService.getSchedules(startDate, endDate, areaId, userId, shift, status, pageNum, pageSize);
    return Result.success(result);
}
```

#### 2.2.2 获取日历视图排班数据

```java
/**
 * 获取日历视图排班数据
 */
@GetMapping("/schedules/calendar")
public Result<List<CalendarScheduleDTO>> getCalendarSchedules(
        @RequestParam Integer year,
        @RequestParam Integer month,
        @RequestParam(required = false) Long areaId) {
    
    // 权限检查
    Long currentUserId = SecurityUtils.getCurrentUserId();
    boolean hasAllPermission = SecurityUtils.hasPermission("schedule_all");
    
    List<CalendarScheduleDTO> result;
    if (SecurityUtils.isAdmin() || hasAllPermission) {
        result = scheduleService.getCalendarSchedules(year, month, areaId, null);
    } else {
        result = scheduleService.getCalendarSchedules(year, month, areaId, currentUserId);
    }
    
    return Result.success(result);
}
```

#### 2.2.3 创建排班

```java
/**
 * 创建排班
 */
@PostMapping("/schedules")
public Result<Long> createSchedule(@RequestBody @Valid ScheduleCreateDTO scheduleDTO) {
    // 权限检查
    if (!SecurityUtils.hasPermission("schedule_edit")) {
        return Result.fail("没有创建排班的权限");
    }
    
    Long scheduleId = scheduleService.createSchedule(scheduleDTO);
    return Result.success(scheduleId);
}
```

#### 2.2.4 批量创建排班

```java
/**
 * 批量创建排班
 */
@PostMapping("/schedules/batch")
public Result<Integer> batchCreateSchedules(@RequestBody @Valid BatchScheduleCreateDTO batchDTO) {
    // 权限检查
    if (!SecurityUtils.hasPermission("schedule_edit")) {
        return Result.fail("没有创建排班的权限");
    }
    
    int count = scheduleService.batchCreateSchedules(batchDTO);
    return Result.success(count);
}
```

#### 2.2.5 更新排班

```java
/**
 * 更新排班
 */
@PutMapping("/schedules/{id}")
public Result<Void> updateSchedule(@PathVariable Long id, @RequestBody @Valid ScheduleUpdateDTO scheduleDTO) {
    // 权限检查
    if (!SecurityUtils.hasPermission("schedule_edit")) {
        return Result.fail("没有更新排班的权限");
    }
    
    scheduleService.updateSchedule(id, scheduleDTO);
    return Result.success();
}
```

#### 2.2.6 删除排班

```java
/**
 * 删除排班
 */
@DeleteMapping("/schedules/{id}")
public Result<Void> deleteSchedule(@PathVariable Long id) {
    // 权限检查
    if (!SecurityUtils.hasPermission("schedule_edit")) {
        return Result.fail("没有删除排班的权限");
    }
    
    scheduleService.deleteSchedule(id);
    return Result.success();
}
```

#### 2.2.7 获取排班统计

```java
/**
 * 获取排班统计
 */
@GetMapping("/schedules/statistics")
public Result<ScheduleStatisticsDTO> getScheduleStatistics(
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        @RequestParam(required = false) Long areaId) {
    
    // 权限检查
    if (!SecurityUtils.hasPermission("statistics_view")) {
        return Result.fail("没有查看统计信息的权限");
    }
    
    ScheduleStatisticsDTO statistics = scheduleService.getScheduleStatistics(startDate, endDate, areaId);
    return Result.success(statistics);
}
```

### 2.3 AD认证与集成API

#### 2.3.1 AD域账号登录

```java
/**
 * AD账号登录
 */
@PostMapping("/auth/ad-login")
public Result<TokenDTO> adLogin(@RequestBody @Valid ADLoginDTO loginDTO) {
    // 调用AD认证服务进行身份验证
    ADAuthResult authResult = adAuthService.authenticate(loginDTO.getUsername(), loginDTO.getPassword());
    
    if (!authResult.isSuccess()) {
        return Result.fail(authResult.getMessage());
    }
    
    // 检查用户是否已在本地存在，不存在则创建
    User user = userService.getUserByADAccount(loginDTO.getUsername());
    if (user == null) {
        // 获取AD用户信息
        ADUserInfo adUserInfo = adAuthService.getUserInfo(loginDTO.getUsername());
        // 创建本地用户
        user = userService.createUserFromAD(adUserInfo);
    } else {
        // 更新最后登录时间
        userService.updateLastLoginTime(user.getId());
    }
    
    // 生成JWT Token
    String token = jwtTokenProvider.generateToken(user);
    
    // 返回登录结果
    TokenDTO tokenDTO = new TokenDTO();
    tokenDTO.setToken(token);
    tokenDTO.setUser(UserDTO.fromUser(user));
    
    return Result.success(tokenDTO);
}
```

#### 2.3.2 AD配置管理

```java
/**
 * 获取AD配置
 */
@GetMapping("/admin/ad/config")
public Result<ADConfigDTO> getADConfig() {
    // 权限检查
    if (!SecurityUtils.hasPermission("ad_config")) {
        return Result.fail("没有AD配置管理权限");
    }
    
    // 获取配置
    ADConfigDTO config = adConfigService.getADConfig();
    
    // 敏感信息脱敏
    config.setBindPassword("******");
    
    return Result.success(config);
}

/**
 * 更新AD配置
 */
@PutMapping("/admin/ad/config")
public Result<Void> updateADConfig(@RequestBody @Valid ADConfigDTO config) {
    // 权限检查
    if (!SecurityUtils.hasPermission("ad_config")) {
        return Result.fail("没有AD配置管理权限");
    }
    
    // 更新配置
    boolean success = adConfigService.updateADConfig(config);
    
    if (success) {
        return Result.success();
    } else {
        return Result.fail("更新配置失败");
    }
}

/**
 * 测试AD连接
 */
@PostMapping("/admin/ad/test-connection")
public Result<TestResultDTO> testConnection(@RequestBody @Valid ADConfigDTO config) {
    // 权限检查
    if (!SecurityUtils.hasPermission("ad_config")) {
        return Result.fail("没有AD配置管理权限");
    }
    
    // 测试连接
    TestResult result = adConfigService.testConnection(config);
    
    return Result.success(TestResultDTO.fromTestResult(result));
}
```

#### 2.3.3 AD用户同步

```java
/**
 * 手动触发全量同步
 */
@PostMapping("/admin/ad/sync/all")
public Result<SyncResultDTO> syncAllUsers() {
    // 权限检查
    if (!SecurityUtils.hasPermission("ad_sync")) {
        return Result.fail("没有AD用户同步权限");
    }
    
    // 调用同步服务
    SyncResult result = adSyncService.syncAllUsers();
    
    return Result.success(SyncResultDTO.fromSyncResult(result));
}

/**
 * 手动触发增量同步
 */
@PostMapping("/admin/ad/sync/incremental")
public Result<SyncResultDTO> syncIncrementalUsers() {
    // 权限检查
    if (!SecurityUtils.hasPermission("ad_sync")) {
        return Result.fail("没有AD用户同步权限");
    }
    
    // 调用同步服务
    SyncResult result = adSyncService.syncIncrementalUsers();
    
    return Result.success(SyncResultDTO.fromSyncResult(result));
}

/**
 * 同步指定用户
 */
@PostMapping("/admin/ad/sync/user")
public Result<SyncResultDTO> syncUser(@RequestParam String username) {
    // 权限检查
    if (!SecurityUtils.hasPermission("ad_sync")) {
        return Result.fail("没有AD用户同步权限");
    }
    
    // 调用同步服务
    SyncResult result = adSyncService.syncUser(username);
    
    return Result.success(SyncResultDTO.fromSyncResult(result));
}

/**
 * 获取同步日志
 */
@GetMapping("/admin/ad/sync/logs")
public Result<PageResult<SyncLogDTO>> getSyncLogs(
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize) {
    // 权限检查
    if (!SecurityUtils.hasPermission("ad_sync")) {
        return Result.fail("没有AD用户同步权限");
    }
    
    // 获取同步日志
    PageResult<SyncLogDTO> logs = adSyncService.getSyncLogs(pageNum, pageSize);
    
    return Result.success(logs);
}
```

## 3. 前端组件更新

### 3.1 用户管理页面更新

需要更新用户管理页面，支持运维人员角色的选择以及AD用户信息的管理：

```javascript
// 用户角色选项
const roleOptions = [
  { value: 'admin', label: '管理员' },
  { value: 'operator', label: '运维人员' },
  { value: 'user', label: '普通用户' }
];

// 用户表单增加AD域账号相关字段
const userForm = reactive({
  username: '',
  password: '',
  realName: '',
  departmentId: null,
  role: 'user',
  status: 'active',
  phone: '',
  email: '',
  isAdUser: false,
  adAccount: '',
  adDomain: ''
});
```

### 3.2 登录页面更新

增加AD域账号登录选项：

```javascript
// 登录页面增加AD域账号登录选项
// 在Login.vue组件中添加AD登录相关代码
import { login, adLogin } from '@/api/auth';

// AD域登录处理
const handleADLogin = async () => {
  try {
    await adFormRef.value.validate();
    const result = await adLogin(adForm);
    if (result.code === 200) {
      userStore.setToken(result.data.token);
      userStore.setUser(result.data.user);
      message.success('登录成功');
      router.push('/');
    } else {
      message.error(result.message || '登录失败');
    }
  } catch (error) {
    console.error('AD login validation failed:', error);
  }
};
```

### 3.3 系统设置菜单更新

在系统设置中增加AD配置菜单项：

```javascript
// 在路由配置中添加AD配置页面
{
  path: '/system/ad-config',
  name: 'ADConfig',
  component: () => import('@/views/system/ADConfig.vue'),
  meta: { title: 'AD域配置', icon: 'setting', roles: ['admin'], permissions: ['ad_config'] }
}
```

### 3.4 新增AD配置管理页面

```javascript
// 创建ADConfig.vue组件
// 实现AD配置管理和用户同步功能
import { getADConfig, updateADConfig, testConnection, syncAllUsers, syncIncrementalUsers, getSyncLogs } from '@/api/ad';

// AD配置表单
const configForm = reactive({
  ldapUrl: '',
  domain: '',
  bindDn: '',
  bindPassword: '',
  userSearchBase: '',
  userSearchFilter: '',
  userObjectClass: '',
  usernameAttribute: '',
  nameAttribute: '',
  emailAttribute: '',
  departmentAttribute: '',
  enabled: false,
  syncInterval: 60
});

// 加载AD配置
const loadADConfig = async () => {
  try {
    const result = await getADConfig();
    if (result.code === 200) {
      Object.assign(configForm, result.data);
    }
  } catch (error) {
    console.error('Failed to load AD config:', error);
  }
};

// 保存AD配置
const saveADConfig = async () => {
  try {
    const result = await updateADConfig(configForm);
    if (result.code === 200) {
      message.success('配置保存成功');
    } else {
      message.error(result.message || '配置保存失败');
    }
  } catch (error) {
    console.error('Failed to save AD config:', error);
  }
};

// 测试AD连接
const testADConnection = async () => {
  try {
    const result = await testConnection(configForm);
    if (result.code === 200 && result.data.success) {
      message.success('连接测试成功');
    } else {
      message.error(result.data.message || '连接测试失败');
    }
  } catch (error) {
    console.error('Failed to test AD connection:', error);
  }
};
```

## 4. 权限控制更新

### 4.1 后端权限拦截器

需要更新权限拦截器，支持新增的权限代码：

```java
// 权限常量定义
public interface PermissionConstants {
    // 现有权限
    String DASHBOARD = "dashboard";
    String RECORDS_VIEW = "records_view";
    String RECORDS_ALL = "records_all";
    String RECORDS_EXPORT = "records_export";
    String ISSUES_VIEW = "issues_view";
    String ISSUES_EDIT = "issues_edit";
    String USER_MANAGE = "user_manage";
    String SYSTEM_CONFIG = "system_config";
    
    // 排班相关权限
    String SCHEDULE_VIEW = "schedule_view";
    String SCHEDULE_EDIT = "schedule_edit";
    String SCHEDULE_ALL = "schedule_all";
    String AREA_MANAGE = "area_manage";
    String STATISTICS_VIEW = "statistics_view";
    
    // AD相关权限
    String AD_CONFIG = "ad_config";
    String AD_SYNC = "ad_sync";
}
```

### 4.2 前端权限指令

需要更新前端权限指令，支持新增的权限代码：

```javascript
// 权限指令
export const hasPermission = {
  mounted(el, binding) {
    const { value } = binding;
    const permissions = store.getters.permissions;
    
    if (value && value instanceof Array && value.length > 0) {
      const requiredPermissions = value;
      const hasPermission = permissions.some(p => requiredPermissions.includes(p));
      
      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el);
      }
    }
  }
};
```

### 4.3 AD用户权限自动分配

为AD用户自动分配权限的逻辑：

```java
/**
 * 根据AD组为用户分配权限
 */
public void assignPermissionsByADGroups(Long userId, List<String> adGroups) {
    // 权限映射关系
    Map<String, List<String>> groupToPermissions = new HashMap<>();
    
    // 定义AD组和权限的映射关系
    groupToPermissions.put("IT Administrators", Arrays.asList(
        "dashboard", "records_view", "records_all", "records_export", 
        "issues_view", "issues_edit", "user_manage", "system_config",
        "schedule_view", "schedule_edit", "schedule_all", "area_manage", 
        "statistics_view", "ad_config", "ad_sync"
    ));
    
    groupToPermissions.put("Operation Engineers", Arrays.asList(
        "dashboard", "records_view", "issues_view", "issues_edit",
        "schedule_view", "statistics_view"
    ));
    
    groupToPermissions.put("General Users", Arrays.asList(
        "dashboard"
    ));
    
    // 清除用户现有权限
    permissionMapper.deleteByUserId(userId);
    
    // 根据AD组分配新权限
    Set<String> permissions = new HashSet<>();
    
    for (String group : adGroups) {
        if (groupToPermissions.containsKey(group)) {
            permissions.addAll(groupToPermissions.get(group));
        }
    }
    
    // 保存新权限
    for (String permission : permissions) {
        UserPermission up = new UserPermission();
        up.setUserId(userId);
        up.setPermissionCode(permission);
        up.setPermissionName(getPermissionName(permission));
        permissionMapper.insert(up);
    }
}

/**
 * 获取权限名称
 */
private String getPermissionName(String permissionCode) {
    Map<String, String> permissionNames = new HashMap<>();
    permissionNames.put("dashboard", "仪表盘查看权限");
    permissionNames.put("records_view", "巡检记录查看权限");
    permissionNames.put("records_all", "查看所有巡检记录");
    permissionNames.put("records_export", "巡检记录导出权限");
    permissionNames.put("issues_view", "问题列表查看权限");
    permissionNames.put("issues_edit", "问题处理权限");
    permissionNames.put("user_manage", "用户管理权限");
    permissionNames.put("system_config", "系统配置权限");
    permissionNames.put("schedule_view", "排班查看权限");
    permissionNames.put("schedule_edit", "排班编辑权限");
    permissionNames.put("schedule_all", "查看所有排班");
    permissionNames.put("area_manage", "区域管理权限");
    permissionNames.put("statistics_view", "统计信息查看权限");
    permissionNames.put("ad_config", "AD配置管理权限");
    permissionNames.put("ad_sync", "AD用户同步权限");
    
    return permissionNames.getOrDefault(permissionCode, permissionCode);
}
```

## 5. 实现计划

### 5.1 阶段一：数据库更新（预计1天）
- 更新用户表的角色字段
- 添加新的权限记录
- 创建AD配置表和同步日志表

### 5.2 阶段二：后端开发（预计5天）
- 修改用户管理相关API
- 实现AD认证服务
- 开发AD用户同步功能
- 实现AD配置管理功能
- 更新权限控制逻辑

### 5.3 阶段三：前端开发（预计5天）
- 更新用户管理页面
- 开发登录页面的AD域账号登录功能
- 实现AD配置管理页面
- 开发用户同步管理页面
- 更新菜单和权限控制

### 5.4 阶段四：测试和部署（预计3天）
- 功能测试
- 安全测试
- AD集成测试
- 部署上线

## 6. 注意事项

### 6.1 安全注意事项
- AD管理员账号密码必须加密存储
- AD连接必须使用SSL/TLS保证安全
- 用户权限变更需要记录审计日志
- 密码策略应与公司AD策略保持一致

### 6.2 性能注意事项
- 大量用户同步时需要分批处理，避免系统压力过大
- AD连接应设置合理的超时时间
- 添加监控和告警，及时发现AD连接问题

### 6.3 兼容性注意事项
- 保留本地账号登录功能，作为备用登录方式
- 确保系统在AD不可用时仍能运行
- 处理好AD用户与本地用户的权限管理差异

## 7. AD域账号集成增量设计补充

### 7.1 AD账号同步策略
- t_user表增加ad_sync_status字段（同步状态）、ad_error_msg字段（同步失败原因）。
- 支持定时任务表（如t_ad_sync_job）记录同步计划、状态、异常。
- API增加同步状态查询接口，如GET /api/v1/ad/sync/status。

### 7.2 异常处理与兜底
- t_user表增加is_local_fallback字段，标记是否允许本地账号兜底登录。
- API登录接口增加错误码区分（如AD服务不可用、AD账号被禁用、本地账号锁定等）。
- t_ad_sync_log表需记录详细异常信息。

### 7.3 权限映射与组同步
- t_user_permission表支持多组权限叠加，增加ad_group字段，记录来源AD组。
- API支持批量权限刷新接口，如POST /api/v1/ad/refresh-permissions。
- 用户权限变更时需记录操作日志。

### 7.4 接口/配置/文档补充
- 用户相关API（如GET /api/v1/users/{id}）返回isAdUser、adAccount、adSyncStatus、adErrorMsg等字段。
- 系统配置API增加AD集成开关、同步策略、组映射规则等配置项。
- AD同步任务API需返回进度、异常、结果明细，便于前端和运维监控。

### 7.5 其他
- 支持AD账号与本地账号冲突检测，API返回冲突详情。
- 支持多AD域扩展预留（如ad_domain字段扩展为多域标识）。
- 所有AD相关操作需有详细审计日志表设计。 