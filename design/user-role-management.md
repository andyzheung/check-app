# 用户角色管理系统设计

## 1. 概述

用户角色管理系统是巡检App管理后台的基础功能模块，用于管理不同类型的用户及其权限。通过该系统，可以创建、编辑和管理用户账户，分配不同的角色和权限，确保每个用户只能访问其职责范围内的功能和数据。

## 2. 用户角色定义

### 2.1 管理员（Admin）
- **职责**：系统管理和配置，全局数据访问和管理
- **权限**：
  - 用户管理（创建、编辑、删除、权限分配）
  - 排班管理（创建、编辑、删除排班计划）
  - 查看所有巡检记录和统计信息
  - 系统配置管理
  - 区域和巡检项配置

### 2.2 运维人员（Operator）
- **职责**：执行巡检任务，处理异常情况，查看自己的巡检数据
- **权限**：
  - 查看自己的排班计划
  - 执行巡检任务
  - 查看和处理自己负责区域的巡检异常
  - 查看自己的巡检记录和统计信息

### 2.3 普通用户（User）
- **职责**：基本信息浏览
- **权限**：
  - 首页访问权限
  - 查看公共信息

## 3. 功能模块

### 3.1 用户管理
- 用户列表：分页展示所有用户，支持筛选和排序
- 新增用户：创建新用户账户，设置基本信息和角色
- 编辑用户：修改用户信息，包括基本信息和角色
- 删除用户：注销或禁用用户账户
- 密码重置：管理员可以重置用户密码

### 3.2 角色管理
- 角色列表：展示所有角色及其关联的权限
- 新增角色：创建自定义角色（扩展功能）
- 编辑角色：修改角色关联的权限（扩展功能）
- 删除角色：删除自定义角色（扩展功能）

### 3.3 权限管理
- 权限分配：为用户分配具体权限
- 权限查看：查看用户已有的权限
- 权限撤销：撤销用户的特定权限

### 3.4 部门管理
- 部门列表：展示所有部门
- 新增部门：创建新部门
- 编辑部门：修改部门信息
- 删除部门：删除部门

## 4. 数据库设计

### 4.1 现有表结构

#### 4.1.1 用户表（t_user）
```sql
CREATE TABLE IF NOT EXISTS t_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    department_id BIGINT COMMENT '部门ID',
    role VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '角色：admin-管理员，user-普通用户',
    status VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '状态：active-活跃，inactive-未激活，locked-已锁定',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(255) COMMENT '头像',
    last_login_time DATETIME COMMENT '最后登录时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

#### 4.1.2 用户权限表（t_user_permission）
```sql
CREATE TABLE IF NOT EXISTS t_user_permission (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    permission_code VARCHAR(50) NOT NULL COMMENT '权限代码',
    permission_name VARCHAR(100) COMMENT '权限名称',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_permission (user_id, permission_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户权限表';
```

#### 4.1.3 部门表（t_department）
```sql
CREATE TABLE IF NOT EXISTS t_department (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
    name VARCHAR(100) NOT NULL COMMENT '部门名称',
    code VARCHAR(50) NOT NULL COMMENT '部门编码',
    parent_id BIGINT COMMENT '父部门ID',
    sort INT DEFAULT 0 COMMENT '排序',
    status INT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';
```

### 4.2 表结构修改

需要对用户表进行修改，增加对运维人员角色的支持：

```sql
ALTER TABLE t_user 
MODIFY COLUMN role VARCHAR(20) NOT NULL DEFAULT 'user' 
COMMENT '角色：admin-管理员，operator-运维人员，user-普通用户';
```

## 5. 权限代码定义

系统将使用以下权限代码来控制用户访问：

### 5.1 基础权限
- `dashboard`: 仪表盘查看权限
- `records_view`: 巡检记录查看权限
- `records_all`: 查看所有巡检记录（否则只能查看自己的）
- `records_export`: 巡检记录导出权限
- `issues_view`: 问题列表查看权限
- `issues_edit`: 问题处理权限
- `user_manage`: 用户管理权限
- `system_config`: 系统配置权限

### 5.2 新增权限
- `schedule_view`: 排班查看权限
- `schedule_edit`: 排班编辑权限
- `schedule_all`: 查看所有排班（否则只能查看自己的）
- `area_manage`: 区域管理权限
- `statistics_view`: 统计信息查看权限

### 5.3 角色默认权限

#### 管理员（Admin）
- 所有权限

#### 运维人员（Operator）
- `dashboard`
- `records_view`
- `issues_view`
- `issues_edit`
- `schedule_view`
- `statistics_view`

#### 普通用户（User）
- `dashboard`

## 6. API设计

### 6.1 用户管理接口

#### 6.1.1 获取用户列表
- 请求方式：GET
- 接口路径：/api/v1/users
- 请求参数：
  ```json
  {
    "username": "",
    "department": "",
    "role": "",
    "status": "",
    "pageNum": 1,
    "pageSize": 10
  }
  ```
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "total": 100,
      "list": [
        {
          "id": 1,
          "username": "admin",
          "realName": "系统管理员",
          "department": "IT部门",
          "role": "admin",
          "status": "active",
          "createTime": "2025-06-01 10:00:00"
        }
      ]
    }
  }
  ```

#### 6.1.2 获取用户详情
- 请求方式：GET
- 接口路径：/api/v1/users/{id}
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 1,
      "username": "admin",
      "realName": "系统管理员",
      "departmentId": 1,
      "departmentName": "IT部门",
      "role": "admin",
      "status": "active",
      "phone": "13800138000",
      "email": "admin@example.com",
      "avatar": "http://example.com/avatar/1.jpg",
      "lastLoginTime": "2025-06-10 08:30:00",
      "createTime": "2025-06-01 10:00:00"
    }
  }
  ```

#### 6.1.3 创建用户
- 请求方式：POST
- 接口路径：/api/v1/users
- 请求参数：
  ```json
  {
    "username": "zhangsan",
    "password": "123456",
    "realName": "张三",
    "departmentId": 2,
    "role": "operator",
    "status": "active",
    "phone": "13900139000",
    "email": "zhangsan@example.com"
  }
  ```
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 2
    }
  }
  ```

#### 6.1.4 更新用户
- 请求方式：PUT
- 接口路径：/api/v1/users/{id}
- 请求参数：
  ```json
  {
    "realName": "张三",
    "departmentId": 2,
    "role": "operator",
    "status": "active",
    "phone": "13900139001",
    "email": "zhangsan@example.com"
  }
  ```
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

#### 6.1.5 删除用户
- 请求方式：DELETE
- 接口路径：/api/v1/users/{id}
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

#### 6.1.6 重置密码
- 请求方式：POST
- 接口路径：/api/v1/users/{id}/reset-password
- 请求参数：
  ```json
  {
    "password": "123456"
  }
  ```
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

### 6.2 权限管理接口

#### 6.2.1 获取用户权限
- 请求方式：GET
- 接口路径：/api/v1/users/{id}/permissions
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "code": "dashboard",
        "name": "仪表盘查看权限"
      },
      {
        "code": "records_view",
        "name": "巡检记录查看权限"
      }
    ]
  }
  ```

#### 6.2.2 更新用户权限
- 请求方式：PUT
- 接口路径：/api/v1/users/{id}/permissions
- 请求参数：
  ```json
  {
    "permissions": ["dashboard", "records_view", "issues_view"]
  }
  ```
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

#### 6.2.3 获取所有权限
- 请求方式：GET
- 接口路径：/api/v1/permissions
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "code": "dashboard",
        "name": "仪表盘查看权限",
        "description": "允许用户查看系统仪表盘"
      },
      {
        "code": "records_view",
        "name": "巡检记录查看权限",
        "description": "允许用户查看巡检记录"
      }
    ]
  }
  ```

### 6.3 部门管理接口

#### 6.3.1 获取部门列表
- 请求方式：GET
- 接口路径：/api/v1/departments
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": [
      {
        "id": 1,
        "name": "IT部门",
        "code": "it",
        "parentId": null,
        "status": 1,
        "children": []
      },
      {
        "id": 2,
        "name": "运维部门",
        "code": "ops",
        "parentId": null,
        "status": 1,
        "children": []
      }
    ]
  }
  ```

#### 6.3.2 创建部门
- 请求方式：POST
- 接口路径：/api/v1/departments
- 请求参数：
  ```json
  {
    "name": "网络安全部",
    "code": "security",
    "parentId": 1,
    "status": 1,
    "sort": 1
  }
  ```
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": {
      "id": 3
    }
  }
  ```

#### 6.3.3 更新部门
- 请求方式：PUT
- 接口路径：/api/v1/departments/{id}
- 请求参数：
  ```json
  {
    "name": "网络安全部",
    "code": "security",
    "parentId": 1,
    "status": 1,
    "sort": 2
  }
  ```
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

#### 6.3.4 删除部门
- 请求方式：DELETE
- 接口路径：/api/v1/departments/{id}
- 响应数据：
  ```json
  {
    "code": 200,
    "message": "success",
    "data": null
  }
  ```

## 7. 前端设计

### 7.1 用户管理页面

#### 7.1.1 用户列表
- 顶部：筛选条件（用户名、部门、角色、状态）
- 中间：用户列表表格
  - 列：用户名、姓名、部门、角色、状态、创建时间、操作
  - 操作：编辑、权限、删除
- 右上角：新增用户按钮

#### 7.1.2 新增/编辑用户弹窗
- 表单字段：
  - 用户名（新增时必填，编辑时只读）
  - 姓名
  - 密码（新增时必填，编辑时选填）
  - 部门（下拉选择）
  - 角色（下拉选择：管理员、运维人员、普通用户）
  - 状态（下拉选择：活跃、未激活、已锁定）
  - 手机号
  - 邮箱

#### 7.1.3 用户权限弹窗
- 用户基本信息显示
- 权限列表（复选框）：
  - 基础权限组
  - 巡检记录权限组
  - 问题管理权限组
  - 系统管理权限组

### 7.2 部门管理页面

#### 7.2.1 部门列表
- 部门树形结构
- 右侧：操作按钮（新增、编辑、删除）

#### 7.2.2 新增/编辑部门弹窗
- 表单字段：
  - 部门名称
  - 部门编码
  - 上级部门（下拉选择）
  - 排序
  - 状态（启用/禁用）

## 8. 实现计划

### 8.1 数据库修改
- 更新用户表的角色字段，增加运维人员角色

### 8.2 后端开发
- 增强UserController，完善所有API接口
- 实现DepartmentController，支持部门管理
- 实现角色与权限相关的业务逻辑
- 增加权限校验中间件

### 8.3 前端开发
- 实现用户管理页面
- 实现部门管理页面
- 实现权限分配功能
- 集成到系统菜单中

## 9. 其他考虑

### 9.1 安全考虑
- 密码加密存储（BCrypt）
- 敏感操作日志记录
- 权限严格校验
- 防止越权访问

### 9.2 扩展性考虑
- 角色系统设计为可扩展的，未来可增加更多角色类型
- 权限粒度可进一步细化
- 可扩展支持更复杂的组织结构

### 9.3 用户体验考虑
- 权限变更即时生效
- 清晰的错误提示
- 操作流程简化

## 10. AD域账号与用户角色管理增量补充

### 10.1 AD组与角色/权限映射
- 支持AD组与系统角色/权限的灵活映射，组与角色可多对多配置。
- AD组变更后，用户下次登录或同步时自动刷新本地角色和权限。
- 支持自定义映射规则，优先级可配置（如IT Administrators > 其他组）。

### 10.2 权限动态刷新与冲突处理
- 用户可因属于多个AD组而拥有多重权限，系统自动合并并去重。
- 若AD组与本地角色冲突，优先以AD组为准，管理员可手动干预。
- 支持批量权限刷新接口，便于运维一键同步所有AD用户权限。

### 10.3 AD用户状态与生命周期管理
- AD用户被禁用/离职时，系统自动同步本地账号状态为禁用。
- 支持AD账号与本地账号冲突检测（如用户名重复），系统提示管理员处理。
- AD用户首次登录时可引导补全本地必填信息（如手机号、邮箱）。

### 10.4 审计与安全
- 所有AD相关的角色/权限变更、同步、异常均需记录审计日志。
- 支持权限变更通知，管理员可订阅关键变更事件。 