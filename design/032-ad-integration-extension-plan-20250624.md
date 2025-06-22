# AD域集成扩展方案设计

**文档创建时间**: 2025-06-24  
**设计范围**: AD域账号集成的完整扩展方案  
**目的**: 提供AD域集成的技术方案和实施指南

## 📍 **当前AD域代码现状**

### 🔍 **已有的AD域相关代码**

#### 1. 核心服务接口
**位置**: `check-app-server/src/main/java/com/pensun/checkapp/service/AdService.java`
```java
public interface AdService {
    List<AdUserDTO> searchUsers(String keyword);
    boolean validateUser(String username, String password);
    List<AdUserDTO> getAllUsers();
    AdUserDTO getUserByUsername(String username);
    Long syncUserToSystem(String adUsername, String systemRole);
}
```

#### 2. 服务实现类
**位置**: `check-app-server/src/main/java/com/pensun/checkapp/service/impl/AdServiceImpl.java`
```java
@Service
public class AdServiceImpl implements AdService {
    // 当前为模拟实现，需要替换为真实LDAP连接
}
```

#### 3. 管理接口
**位置**: `check-app-server/src/main/java/com/pensun/checkapp/controller/AdminController.java`
```java
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @GetMapping("/ad/users/search")  // 搜索AD用户
    @GetMapping("/ad/users")         // 获取所有AD用户
    @PostMapping("/ad/sync")         // 同步AD用户到系统
}
```

#### 4. 数据库支持
**表结构**: `t_user`表已包含AD域相关字段
```sql
CREATE TABLE `t_user` (
  `ad_username` varchar(100) DEFAULT NULL COMMENT 'AD域账号',
  `is_ad_user` tinyint DEFAULT '0' COMMENT '是否AD用户：0-本地用户，1-AD用户',
  `ad_sync_time` datetime DEFAULT NULL COMMENT 'AD同步时间'
);
```

### 🎯 **当前实现的功能**
- ✅ AD用户搜索接口（模拟实现）
- ✅ AD用户认证接口（模拟实现）
- ✅ AD用户同步到本地系统
- ✅ 用户管理界面支持AD用户标识
- ✅ 数据库表结构支持AD域字段

### ⚠️ **当前的限制**
- ❌ 没有真实的LDAP连接实现
- ❌ 没有AD域配置管理界面
- ❌ 没有AD域连接测试功能
- ❌ 没有AD用户组织架构同步

## 🚀 **扩展方案设计**

### 📋 **Phase 1: LDAP连接实现 (1-2天)**

#### 1.1 添加LDAP依赖
**文件**: `check-app-server/pom.xml`
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-ldap</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.ldap</groupId>
    <artifactId>spring-ldap-core</artifactId>
</dependency>
```

#### 1.2 LDAP配置类
**新增文件**: `check-app-server/src/main/java/com/pensun/checkapp/config/LdapConfig.java`
```java
@Configuration
@EnableLdap
public class LdapConfig {
    
    @Value("${ldap.url}")
    private String ldapUrl;
    
    @Value("${ldap.base}")
    private String ldapBase;
    
    @Value("${ldap.username}")
    private String ldapUsername;
    
    @Value("${ldap.password}")
    private String ldapPassword;
    
    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(ldapUrl);
        contextSource.setBase(ldapBase);
        contextSource.setUserDn(ldapUsername);
        contextSource.setPassword(ldapPassword);
        return contextSource;
    }
    
    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }
}
```

#### 1.3 真实AdService实现
**修改文件**: `check-app-server/src/main/java/com/pensun/checkapp/service/impl/AdServiceImpl.java`
```java
@Service
public class AdServiceImpl implements AdService {
    
    @Autowired
    private LdapTemplate ldapTemplate;
    
    @Override
    public boolean validateUser(String username, String password) {
        try {
            String userDn = "cn=" + username + ",ou=users,dc=company,dc=com";
            return ldapTemplate.authenticate(userDn, "(objectclass=person)", password);
        } catch (Exception e) {
            log.error("AD认证失败: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public List<AdUserDTO> searchUsers(String keyword) {
        String filter = "(|(cn=*" + keyword + "*)(sAMAccountName=*" + keyword + "*))";
        return ldapTemplate.search("ou=users", filter, new AdUserAttributesMapper());
    }
    
    // 其他方法的真实实现...
}
```

### 📋 **Phase 2: AD域配置管理 (1天)**

#### 2.1 系统参数扩展
**数据库**: 在`t_system_param`表中添加AD域配置参数
```sql
INSERT INTO t_system_param (param_key, param_value, param_desc) VALUES
('ad.server.url', 'ldap://dc.company.com:389', 'AD域服务器地址'),
('ad.base.dn', 'dc=company,dc=com', 'AD域基础DN'),
('ad.admin.username', 'admin@company.com', 'AD域管理员账号'),
('ad.admin.password', 'encrypted_password', 'AD域管理员密码（加密）'),
('ad.user.search.base', 'ou=users,dc=company,dc=com', '用户搜索基础路径'),
('ad.group.search.base', 'ou=groups,dc=company,dc=com', '组搜索基础路径'),
('ad.sync.enabled', 'true', '是否启用AD同步'),
('ad.sync.interval', '3600', 'AD同步间隔（秒）');
```

#### 2.2 AD配置管理接口
**新增文件**: `check-app-server/src/main/java/com/pensun/checkapp/controller/ADConfigController.java`
```java
@RestController
@RequestMapping("/api/v1/admin/ad/config")
public class ADConfigController {
    
    @GetMapping
    public Result<Map<String, String>> getAdConfig() {
        // 获取AD域配置参数
    }
    
    @PutMapping
    public Result<Void> updateAdConfig(@RequestBody Map<String, String> config) {
        // 更新AD域配置参数
    }
    
    @PostMapping("/test")
    public Result<String> testAdConnection(@RequestBody AdConfigDTO config) {
        // 测试AD域连接
    }
}
```

#### 2.3 前端AD配置页面增强
**修改文件**: `admin-web/src/views/system/ADConfig.vue`
```vue
<template>
  <div class="ad-config">
    <!-- AD服务器配置 -->
    <a-card title="AD服务器配置" style="margin-bottom: 16px;">
      <a-form :model="adConfig" layout="vertical">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-form-item label="服务器地址">
              <a-input v-model:value="adConfig.serverUrl" placeholder="ldap://dc.company.com:389" />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="基础DN">
              <a-input v-model:value="adConfig.baseDn" placeholder="dc=company,dc=com" />
            </a-form-item>
          </a-col>
        </a-row>
        <!-- 更多配置项... -->
        <a-form-item>
          <a-button type="primary" @click="testConnection">测试连接</a-button>
          <a-button style="margin-left: 8px;" @click="saveConfig">保存配置</a-button>
        </a-form-item>
      </a-form>
    </a-card>
    
    <!-- AD用户同步 -->
    <a-card title="用户同步管理">
      <!-- 同步配置和手动同步功能 -->
    </a-card>
  </div>
</template>
```

### 📋 **Phase 3: 高级功能扩展 (2-3天)**

#### 3.1 组织架构同步
**新增功能**: 同步AD域的组织架构到本地部门表
```java
@Service
public class AdOrgSyncService {
    
    public void syncDepartments() {
        // 从AD域同步组织架构到t_department表
    }
    
    public void syncUserDepartments() {
        // 同步用户的部门关联关系
    }
}
```

#### 3.2 定时同步任务
**新增文件**: `check-app-server/src/main/java/com/pensun/checkapp/task/AdSyncTask.java`
```java
@Component
public class AdSyncTask {
    
    @Scheduled(fixedRateString = "${ad.sync.interval:3600}000")
    public void syncAdUsers() {
        if (isAdSyncEnabled()) {
            // 执行AD用户同步
        }
    }
}
```

#### 3.3 AD用户权限映射
**新增功能**: 根据AD域用户组自动分配系统权限
```java
@Service
public class AdPermissionMappingService {
    
    public void mapAdGroupsToSystemRoles(String adUsername) {
        // 根据AD用户组映射系统角色
    }
}
```

#### 3.4 单点登录(SSO)支持
**可选扩展**: 支持AD域单点登录
```java
@Configuration
public class SsoConfig {
    // 配置SAML或OAuth2.0单点登录
}
```

## 🗂️ **需要扩展的文件清单**

### 📁 **后端文件**
```
check-app-server/
├── pom.xml                                          # 添加LDAP依赖
├── src/main/java/com/pensun/checkapp/
│   ├── config/
│   │   └── LdapConfig.java                         # 新增：LDAP配置
│   ├── controller/
│   │   └── ADConfigController.java                 # 新增：AD配置管理
│   ├── service/
│   │   ├── AdService.java                          # 扩展：添加新方法
│   │   └── AdOrgSyncService.java                   # 新增：组织架构同步
│   ├── service/impl/
│   │   └── AdServiceImpl.java                      # 修改：真实LDAP实现
│   ├── task/
│   │   └── AdSyncTask.java                         # 新增：定时同步任务
│   ├── dto/
│   │   ├── AdConfigDTO.java                        # 新增：AD配置DTO
│   │   └── AdGroupDTO.java                         # 新增：AD组DTO
│   └── mapper/
│       └── AdUserAttributesMapper.java             # 新增：LDAP属性映射
└── src/main/resources/
    └── application.yml                              # 修改：添加LDAP配置
```

### 📁 **前端文件**
```
admin-web/
├── src/
│   ├── api/
│   │   └── ad.js                                   # 扩展：AD配置API
│   ├── views/system/
│   │   └── ADConfig.vue                           # 修改：增强AD配置页面
│   └── views/users/
│       └── Users.vue                              # 修改：增强AD用户管理
```

## 🔧 **配置文件模板**

### application.yml配置
```yaml
# LDAP配置
spring:
  ldap:
    urls: ${AD_SERVER_URL:ldap://dc.company.com:389}
    base: ${AD_BASE_DN:dc=company,dc=com}
    username: ${AD_ADMIN_USERNAME:admin@company.com}
    password: ${AD_ADMIN_PASSWORD:password}

# AD域相关配置
ad:
  sync:
    enabled: ${AD_SYNC_ENABLED:true}
    interval: ${AD_SYNC_INTERVAL:3600}
  user:
    search-base: ${AD_USER_SEARCH_BASE:ou=users,dc=company,dc=com}
    search-filter: ${AD_USER_SEARCH_FILTER:(objectClass=person)}
  group:
    search-base: ${AD_GROUP_SEARCH_BASE:ou=groups,dc=company,dc=com}
    search-filter: ${AD_GROUP_SEARCH_FILTER:(objectClass=group)}
```

## 📊 **实施计划**

### 🎯 **Phase 1: 基础LDAP连接 (1-2天)**
- **Day 1**: 添加LDAP依赖，实现基础连接和认证
- **Day 2**: 完成用户搜索和同步功能

### 🎯 **Phase 2: 配置管理界面 (1天)**
- **Day 3**: 实现AD配置管理接口和前端界面

### 🎯 **Phase 3: 高级功能 (2-3天)**
- **Day 4-5**: 组织架构同步、定时任务
- **Day 6**: 权限映射、SSO支持（可选）

## 🔒 **安全考虑**

### 🛡️ **密码安全**
- AD管理员密码必须加密存储
- 使用Spring Security的PasswordEncoder
- 支持密码轮换机制

### 🛡️ **连接安全**
- 支持LDAPS（SSL/TLS加密）
- 连接池管理，避免连接泄露
- 超时和重试机制

### 🛡️ **权限控制**
- AD配置管理仅限超级管理员
- 用户同步操作需要审计日志
- 敏感信息脱敏显示

## 📈 **监控和日志**

### 📊 **监控指标**
- AD连接状态监控
- 用户同步成功率
- 认证成功率统计

### 📝 **日志记录**
- AD连接日志
- 用户同步日志
- 认证失败日志
- 配置变更日志

## 🎯 **验收标准**

### ✅ **功能验收**
- [ ] AD用户认证成功
- [ ] AD用户搜索正常
- [ ] 用户同步功能正常
- [ ] AD配置管理界面完整
- [ ] 连接测试功能正常

### ✅ **性能验收**
- [ ] AD认证响应时间 < 3秒
- [ ] 用户同步性能满足要求
- [ ] 系统稳定性不受影响

### ✅ **安全验收**
- [ ] 密码加密存储
- [ ] 连接安全可靠
- [ ] 权限控制完善

## 💡 **最佳实践建议**

1. **渐进式实施**: 先实现基础功能，再逐步添加高级特性
2. **配置灵活性**: 支持多种AD域环境的配置
3. **错误处理**: 完善的异常处理和用户提示
4. **测试覆盖**: 单元测试和集成测试覆盖
5. **文档完善**: 详细的部署和使用文档 