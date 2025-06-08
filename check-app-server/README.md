# Check App Server

## 项目介绍
Check App Server是一个基于Spring Boot的后端服务，提供用户管理、权限管理等功能。

## 技术栈
- Spring Boot 2.7.5
- MyBatis Plus 3.5.2
- MySQL 8.0
- JWT 0.9.1
- Maven 3.8+

## 开发环境
- JDK 1.8
- MySQL 8.0
- Maven 3.8+

## 快速开始

### 1. 克隆项目
```bash
git clone https://github.com/your-username/check-app.git
cd check-app/check-app-server
```

### 2. 创建数据库
```sql
CREATE DATABASE check_app DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```

### 3. 初始化数据库
```bash
mysql -u root -p check_app < src/main/resources/db/init.sql
```

### 4. 修改配置
修改`src/main/resources/application.yml`中的数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/check_app?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai
    username: your-username
    password: your-password
```

### 5. 启动项目
```bash
mvn spring-boot:run
```

## 接口文档

### 用户管理
- 登录：POST /api/users/login
- 获取当前用户：GET /api/users/current
- 分页查询用户：GET /api/users/page
- 创建用户：POST /api/users
- 更新用户：PUT /api/users
- 删除用户：DELETE /api/users/{id}

### 权限管理
- 获取用户权限：GET /api/users/{userId}/permissions
- 更新用户权限：PUT /api/users/permissions

## 默认账号
- 用户名：admin
- 密码：123456 