# AD域账号集成设计

## 1. 概述

本文档描述了巡检App管理系统与公司AD（Active Directory）域账号系统的集成方案，实现统一的身份认证和授权。通过集成AD域账号，用户可以使用公司统一的账号密码登录系统，提高安全性并简化账号管理。

## 2. 需求分析

### 2.1 功能需求
- 支持AD域账号登录验证
- 用户首次登录时自动创建本地账号并同步基本信息
- 定期同步AD域中的用户信息
- 支持本地账号与AD账号的映射关系维护
- 保留本地管理员账号作为备用登录方式

### 2.2 技术需求
- 与Microsoft Active Directory的LDAP协议集成
- 安全的身份验证和数据传输
- 高效的用户信息同步机制
- 灵活的权限映射配置

## 3. 系统架构

### 3.1 总体架构
```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│                 │    │                  │    │                 │
│  巡检App管理系统  │<───│  认证集成服务层   │<───│    AD域服务器   │
│                 │    │                  │    │                 │
└─────────────────┘    └──────────────────┘    └─────────────────┘
```

### 3.2 认证流程
```
┌──────┐    ┌────────────┐    ┌───────────────┐    ┌──────────┐
│ 用户  │───>│ 登录界面   │───>│ 认证服务      │───>│ AD服务器 │
└──────┘    └────────────┘    └───────────────┘    └──────────┘
                                     │
                                     ↓
                             ┌───────────────┐
                             │ 用户信息同步  │
                             └───────────────┘
                                     │
                                     ↓
                             ┌───────────────┐
                             │  权限分配     │
                             └───────────────┘
                                     │
                                     ↓
                             ┌───────────────┐
                             │ 登录成功/失败 │
                             └───────────────┘
```

## 4. 数据库设计

### 4.1 现有表结构调整

#### 4.1.1 用户表（t_user）修改

需要对用户表进行修改，增加AD域账号相关字段：

```sql
ALTER TABLE t_user 
ADD COLUMN ad_account VARCHAR(100) COMMENT 'AD域账号',
ADD COLUMN ad_domain VARCHAR(100) COMMENT 'AD域名',
ADD COLUMN ad_last_sync DATETIME COMMENT 'AD最后同步时间',
ADD COLUMN is_ad_user TINYINT DEFAULT 0 COMMENT '是否AD用户：0-否，1-是',
ADD UNIQUE INDEX uk_ad_account (ad_account, ad_domain, deleted);
```

### 4.2 新增表结构

#### 4.2.1 AD配置表（t_ad_config）

```sql
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
```

#### 4.2.2 AD同步日志表（t_ad_sync_log）

```sql
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

## 5. 组件设计

### 5.1 AD认证服务（ADAuthService）

负责与AD服务器交互，验证用户身份信息。

```java
/**
 * AD认证服务接口
 */
public interface ADAuthService {
    
    /**
     * AD域用户登录验证
     *
     * @param username AD域用户名
     * @param password 密码
     * @return 验证结果
     */
    ADAuthResult authenticate(String username, String password);
    
    /**
     * 获取AD用户信息
     *
     * @param username AD域用户名
     * @return AD用户信息
     */
    ADUserInfo getUserInfo(String username);
    
    /**
     * 获取AD用户所属组信息
     *
     * @param username AD域用户名
     * @return 用户所属AD组列表
     */
    List<String> getUserGroups(String username);
    
    /**
     * 测试AD连接
     *
     * @return 连接测试结果
     */
    boolean testConnection();
}
```

### 5.2 AD用户同步服务（ADSyncService）

负责同步AD域用户信息到本地系统。

```java
/**
 * AD用户同步服务接口
 */
public interface ADSyncService {
    
    /**
     * 全量同步AD用户
     *
     * @return 同步结果
     */
    SyncResult syncAllUsers();
    
    /**
     * 增量同步AD用户（基于上次同步时间）
     *
     * @return 同步结果
     */
    SyncResult syncIncrementalUsers();
    
    /**
     * 同步指定AD用户
     *
     * @param username AD域用户名
     * @return 同步结果
     */
    SyncResult syncUser(String username);
    
    /**
     * 获取同步日志
     *
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 同步日志分页结果
     */
    PageResult<SyncLogDTO> getSyncLogs(int pageNum, int pageSize);
}
```

### 5.3 AD配置管理服务（ADConfigService）

负责管理AD连接配置。

```java
/**
 * AD配置管理服务接口
 */
public interface ADConfigService {
    
    /**
     * 获取AD配置
     *
     * @return AD配置
     */
    ADConfigDTO getADConfig();
    
    /**
     * 更新AD配置
     *
     * @param config AD配置
     * @return 是否成功
     */
    boolean updateADConfig(ADConfigDTO config);
    
    /**
     * 测试AD连接
     *
     * @param config AD配置
     * @return 测试结果
     */
    TestResult testConnection(ADConfigDTO config);
}
```

## 6. API设计

### 6.1 登录认证API

#### 6.1.1 AD账号登录

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

### 6.2 AD用户同步API

#### 6.2.1 手动触发全量同步

```java
/**
 * 手动触发全量同步
 */
@PostMapping("/admin/ad/sync/all")
public Result<SyncResultDTO> syncAllUsers() {
    // 权限检查
    if (!SecurityUtils.hasPermission("system_config")) {
        return Result.fail("没有系统配置权限");
    }
    
    // 调用同步服务
    SyncResult result = adSyncService.syncAllUsers();
    
    return Result.success(SyncResultDTO.fromSyncResult(result));
}
```

#### 6.2.2 手动触发增量同步

```java
/**
 * 手动触发增量同步
 */
@PostMapping("/admin/ad/sync/incremental")
public Result<SyncResultDTO> syncIncrementalUsers() {
    // 权限检查
    if (!SecurityUtils.hasPermission("system_config")) {
        return Result.fail("没有系统配置权限");
    }
    
    // 调用同步服务
    SyncResult result = adSyncService.syncIncrementalUsers();
    
    return Result.success(SyncResultDTO.fromSyncResult(result));
}
```

#### 6.2.3 同步指定用户

```java
/**
 * 同步指定用户
 */
@PostMapping("/admin/ad/sync/user")
public Result<SyncResultDTO> syncUser(@RequestParam String username) {
    // 权限检查
    if (!SecurityUtils.hasPermission("system_config")) {
        return Result.fail("没有系统配置权限");
    }
    
    // 调用同步服务
    SyncResult result = adSyncService.syncUser(username);
    
    return Result.success(SyncResultDTO.fromSyncResult(result));
}
```

### 6.3 AD配置管理API

#### 6.3.1 获取AD配置

```java
/**
 * 获取AD配置
 */
@GetMapping("/admin/ad/config")
public Result<ADConfigDTO> getADConfig() {
    // 权限检查
    if (!SecurityUtils.hasPermission("system_config")) {
        return Result.fail("没有系统配置权限");
    }
    
    // 获取配置
    ADConfigDTO config = adConfigService.getADConfig();
    
    // 敏感信息脱敏
    config.setBindPassword("******");
    
    return Result.success(config);
}
```

#### 6.3.2 更新AD配置

```java
/**
 * 更新AD配置
 */
@PutMapping("/admin/ad/config")
public Result<Void> updateADConfig(@RequestBody @Valid ADConfigDTO config) {
    // 权限检查
    if (!SecurityUtils.hasPermission("system_config")) {
        return Result.fail("没有系统配置权限");
    }
    
    // 更新配置
    boolean success = adConfigService.updateADConfig(config);
    
    if (success) {
        return Result.success();
    } else {
        return Result.fail("更新配置失败");
    }
}
```

#### 6.3.3 测试AD连接

```java
/**
 * 测试AD连接
 */
@PostMapping("/admin/ad/test-connection")
public Result<TestResultDTO> testConnection(@RequestBody @Valid ADConfigDTO config) {
    // 权限检查
    if (!SecurityUtils.hasPermission("system_config")) {
        return Result.fail("没有系统配置权限");
    }
    
    // 测试连接
    TestResult result = adConfigService.testConnection(config);
    
    return Result.success(TestResultDTO.fromTestResult(result));
}
```

## 7. 前端设计

### 7.1 登录页面调整

登录页面需要增加AD域账号登录选项。

```vue
<template>
  <div class="login-container">
    <div class="login-form">
      <div class="login-title">巡检App管理系统</div>
      
      <!-- 登录方式切换 -->
      <a-tabs v-model:activeKey="loginType">
        <a-tab-pane key="local" tab="系统账号登录">
          <!-- 现有本地登录表单 -->
          <a-form :model="localForm" :rules="localRules" ref="localFormRef">
            <a-form-item name="username">
              <a-input v-model:value="localForm.username" placeholder="用户名" />
            </a-form-item>
            <a-form-item name="password">
              <a-input-password v-model:value="localForm.password" placeholder="密码" />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" block @click="handleLocalLogin">登录</a-button>
            </a-form-item>
          </a-form>
        </a-tab-pane>
        
        <a-tab-pane key="ad" tab="AD域账号登录">
          <!-- AD域登录表单 -->
          <a-form :model="adForm" :rules="adRules" ref="adFormRef">
            <a-form-item name="username">
              <a-input v-model:value="adForm.username" placeholder="AD账号" />
            </a-form-item>
            <a-form-item name="password">
              <a-input-password v-model:value="adForm.password" placeholder="密码" />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" block @click="handleADLogin">登录</a-button>
            </a-form-item>
          </a-form>
        </a-tab-pane>
      </a-tabs>
    </div>
  </div>
</template>

<script>
import { defineComponent, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { login, adLogin } from '@/api/auth'
import { useUserStore } from '@/stores/user'

export default defineComponent({
  name: 'Login',
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    
    // 登录类型
    const loginType = ref('local')
    
    // 本地登录表单
    const localFormRef = ref(null)
    const localForm = reactive({
      username: '',
      password: ''
    })
    
    // AD域登录表单
    const adFormRef = ref(null)
    const adForm = reactive({
      username: '',
      password: ''
    })
    
    // 表单验证规则
    const localRules = {
      username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
      password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
    }
    
    const adRules = {
      username: [{ required: true, message: '请输入AD账号', trigger: 'blur' }],
      password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
    }
    
    // 本地登录处理
    const handleLocalLogin = async () => {
      try {
        await localFormRef.value.validate()
        
        const result = await login(localForm)
        if (result.code === 200) {
          userStore.setToken(result.data.token)
          userStore.setUser(result.data.user)
          message.success('登录成功')
          router.push('/')
        } else {
          message.error(result.message || '登录失败')
        }
      } catch (error) {
        console.error('Login validation failed:', error)
      }
    }
    
    // AD域登录处理
    const handleADLogin = async () => {
      try {
        await adFormRef.value.validate()
        
        const result = await adLogin(adForm)
        if (result.code === 200) {
          userStore.setToken(result.data.token)
          userStore.setUser(result.data.user)
          message.success('登录成功')
          router.push('/')
        } else {
          message.error(result.message || '登录失败')
        }
      } catch (error) {
        console.error('AD login validation failed:', error)
      }
    }
    
    return {
      loginType,
      localForm,
      adForm,
      localRules,
      adRules,
      localFormRef,
      adFormRef,
      handleLocalLogin,
      handleADLogin
    }
  }
})
</script>
```

### 7.2 AD配置管理页面

在系统设置中添加AD配置管理页面。

```vue
<template>
  <div>
    <div class="card">
      <div class="card-title">AD域配置</div>
      
      <a-form
        :model="configForm"
        :rules="rules"
        ref="configFormRef"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 14 }"
      >
        <a-form-item label="LDAP服务器地址" name="ldapUrl">
          <a-input v-model:value="configForm.ldapUrl" placeholder="ldap://example.com:389" />
        </a-form-item>
        
        <a-form-item label="域名" name="domain">
          <a-input v-model:value="configForm.domain" placeholder="example.com" />
        </a-form-item>
        
        <a-form-item label="管理员DN" name="bindDn">
          <a-input v-model:value="configForm.bindDn" placeholder="cn=admin,dc=example,dc=com" />
        </a-form-item>
        
        <a-form-item label="管理员密码" name="bindPassword">
          <a-input-password v-model:value="configForm.bindPassword" placeholder="密码" />
        </a-form-item>
        
        <a-form-item label="用户搜索基础" name="userSearchBase">
          <a-input v-model:value="configForm.userSearchBase" placeholder="ou=users,dc=example,dc=com" />
        </a-form-item>
        
        <a-form-item label="用户搜索过滤器" name="userSearchFilter">
          <a-input v-model:value="configForm.userSearchFilter" placeholder="(sAMAccountName={0})" />
        </a-form-item>
        
        <a-form-item label="用户对象类" name="userObjectClass">
          <a-input v-model:value="configForm.userObjectClass" placeholder="person" />
        </a-form-item>
        
        <a-form-item label="用户名属性" name="usernameAttribute">
          <a-input v-model:value="configForm.usernameAttribute" placeholder="sAMAccountName" />
        </a-form-item>
        
        <a-form-item label="姓名属性" name="nameAttribute">
          <a-input v-model:value="configForm.nameAttribute" placeholder="cn" />
        </a-form-item>
        
        <a-form-item label="邮箱属性" name="emailAttribute">
          <a-input v-model:value="configForm.emailAttribute" placeholder="mail" />
        </a-form-item>
        
        <a-form-item label="部门属性" name="departmentAttribute">
          <a-input v-model:value="configForm.departmentAttribute" placeholder="department" />
        </a-form-item>
        
        <a-form-item label="启用AD集成" name="enabled">
          <a-switch v-model:checked="configForm.enabled" />
        </a-form-item>
        
        <a-form-item label="同步间隔(分钟)" name="syncInterval">
          <a-input-number v-model:value="configForm.syncInterval" :min="5" :max="1440" />
        </a-form-item>
        
        <a-form-item :wrapper-col="{ offset: 6, span: 14 }">
          <a-space>
            <a-button type="primary" @click="handleSave">保存配置</a-button>
            <a-button @click="handleTest">测试连接</a-button>
          </a-space>
        </a-form-item>
      </a-form>
    </div>
    
    <div class="card" style="margin-top: 16px;">
      <div style="display: flex; justify-content: space-between; margin-bottom: 16px;">
        <div class="card-title">AD用户同步</div>
        <a-space>
          <a-button @click="handleSyncIncremental">增量同步</a-button>
          <a-button type="primary" @click="handleSyncAll">全量同步</a-button>
        </a-space>
      </div>
      
      <a-table
        :columns="columns"
        :data-source="syncLogs"
        :loading="loading"
        :pagination="pagination"
        @change="handleTableChange"
        row-key="id"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="getStatusColor(record.status)">
              {{ getStatusText(record.status) }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'duration'">
            {{ calculateDuration(record.startTime, record.endTime) }}
          </template>
        </template>
      </a-table>
    </div>
  </div>
</template>
```

## 8. 安全设计

### 8.1 安全传输
- 使用LDAPS (LDAP over SSL) 进行安全连接
- 所有密码都经过加密传输和存储
- 支持TLS/SSL连接

### 8.2 账号安全
- 管理员账号信息加密存储
- 用户密码不在本地存储，每次都从AD验证
- 支持账号锁定机制

### 8.3 权限映射安全
- AD组与系统角色/权限的映射关系管理
- 基于最小权限原则分配权限
- 权限变更审计日志

## 9. 实现计划

### 9.1 阶段一：AD连接模块开发（3天）
- 实现AD连接和身份验证
- 实现AD用户信息查询
- 开发配置管理功能

### 9.2 阶段二：用户同步模块开发（2天）
- 实现AD用户同步逻辑
- 开发增量同步功能
- 实现同步日志记录

### 9.3 阶段三：认证集成开发（2天）
- 实现AD域登录功能
- 集成JWT认证
- 开发权限映射功能

### 9.4 阶段四：前端开发（3天）
- 开发AD域登录界面
- 实现AD配置管理页面
- 开发同步管理界面

### 9.5 阶段五：测试和部署（2天）
- 功能测试
- 安全测试
- 部署上线

## 10. 风险与应对

### 10.1 潜在风险
- AD服务器不可用导致无法登录
- 大量用户同步可能导致性能问题
- AD用户权限变更可能导致安全风险
- 用户信息不一致可能导致系统混乱

### 10.2 应对措施
- 保留本地管理员账号作为备用登录方式
- 实现分批同步和限流机制
- 定期审计用户权限
- 实现数据一致性检查和冲突解决机制

## 11. 附录

### 11.1 AD属性映射参考

| 系统字段     | AD属性           | 描述           |
|------------|-----------------|---------------|
| username   | sAMAccountName  | 用户登录名      |
| realName   | cn 或 displayName| 用户真实姓名    |
| email      | mail            | 用户邮箱       |
| department | department      | 用户部门       |
| phone      | telephoneNumber | 用户电话       |

### 11.2 AD组与系统角色映射参考

| AD组                  | 系统角色         |
|----------------------|----------------|
| IT Administrators    | admin          |
| Operation Engineers  | operator       |
| General Users        | user           |
```

## 12. 增量补充：AD域账号集成细节

### 12.1 AD账号同步策略
- 支持"登录即同步"与"定时全量/增量同步"两种模式，具体可在AD配置中切换：
  - 登录即同步：用户首次或每次登录时自动同步AD信息。
  - 定时同步：通过定时任务（如每晚/每小时）全量或增量同步AD用户，支持同步禁用/离职用户状态。
- 支持AD用户自动禁用/删除同步，若AD中用户被禁用或删除，系统可自动将本地账号设为禁用或逻辑删除。

### 12.2 异常处理与兜底方案
- AD服务器不可用时，允许本地账号登录（如管理员账号），并在登录界面提示"AD服务暂不可用"。
- AD用户被禁用/离职时，下一次同步或登录时本地账号自动禁用。
- 所有AD相关异常均需记录操作日志，便于审计和追踪。

### 12.3 权限映射与组同步
- 支持AD组与系统角色/权限的灵活映射，支持多组叠加，优先级可配置（如IT Administrators > Operation Engineers > General Users）。
- 支持动态变更：AD组变更后，用户下次登录或同步时自动刷新本地权限。
- 支持自定义映射规则，便于未来扩展。

### 12.4 前端联调与用户体验
- 登录页支持AD与本地账号切换，切换时清空表单并区分错误提示（如"AD账号或密码错误""AD服务不可用"）。
- AD用户首次登录时可弹窗引导补全手机号、邮箱等本地必填信息。
- 支持AD账号锁定、登录失败次数限制，前端需展示锁定提示。

### 12.5 安全与合规
- AD连接配置（如密码、密钥）加密存储，敏感信息前端脱敏展示。
- 所有AD相关操作（登录、同步、配置变更）需记录操作日志。
- 强制使用LDAPS（SSL）连接，禁止明文LDAP。
- 本地不存储AD用户密码，所有认证均实时转发AD。

### 12.6 接口/配置/文档补充
- 后端API需返回AD相关字段（如isAdUser、adAccount、adSyncStatus、adErrorMsg等），便于前端判断和展示。
- 前端可通过接口获取当前系统是否启用AD集成（如GET /api/v1/system/ad-enabled）。
- AD同步任务需有状态监控接口，便于运维查看同步进度和异常。

### 12.7 其他注意事项
- AD账号生命周期管理：支持离职、变更、权限回收，定期同步。
- 多AD域/多租户支持：预留扩展点，当前版本仅支持单域。
- AD账号与本地账号冲突（如用户名重复）时，需有冲突检测和处理策略（如加前缀、提示管理员）。
- 大批量同步时需分批处理，保证幂等性和性能。
- 登录失败、同步失败等关键事件需有安全告警和审计。