# 巡检App架构设计文档

## 1. 系统架构概述

### 整体架构
巡检App采用前后端分离的架构设计，包含以下主要组件：

1. **移动端应用**：
   - 基于Android WebView的混合应用
   - 内嵌H5页面，使用Vue 3前端框架

2. **后端服务**：
   - 基于Spring Boot的RESTful API服务
   - 使用MySQL数据库存储数据
   - JWT Token认证

3. **系统集成**：
   - 支持AD域集成
   - 二维码生成与识别
   - 文件上传

### 技术栈

#### 前端
- Vue 3 + Vite
- Vant UI组件库
- Axios请求库
- Pinia状态管理

#### 后端
- Spring Boot 2.x
- MyBatis-Plus
- Spring Security
- JWT认证
- MySQL数据库

#### 移动端
- Android原生
- WebView
- JavaScript Bridge

---

## 2. 前端架构设计

### 目录结构
```
App/web/
├── src/
│   ├── api/            # API请求封装
│   ├── assets/         # 静态资源
│   ├── components/     # 公共组件
│   ├── router.js       # 路由配置
│   ├── stores/         # Pinia状态管理
│   ├── utils/          # 工具类
│   ├── views/          # 页面组件
│   ├── App.vue         # 根组件
│   └── main.js         # 入口文件
├── index.html          # HTML模板
└── vite.config.js      # Vite配置
```

### 核心设计

#### 1. 组件设计
- 基于Vue 3组合式API
- 使用Vant UI提供移动端组件
- 组件按功能分类组织

#### 2. 状态管理
- 使用Pinia管理全局状态
- 将用户信息、认证Token等存储在状态中

#### 3. 路由设计
- 基于Vue Router
- 路由守卫验证认证状态
- 权限控制和页面访问控制

#### 4. API请求封装
- 使用Axios封装HTTP请求
- 按功能模块组织API
- 请求拦截器添加Token
- 响应拦截器处理错误

---

## 3. 后端架构设计

### 目录结构
```
check-app-server/
├── src/main/
│   ├── java/com/pensun/checkapp/
│   │   ├── common/         # 公共类
│   │   ├── config/         # 配置类
│   │   ├── controller/     # 控制器层
│   │   ├── dto/            # 数据传输对象
│   │   ├── entity/         # 实体类
│   │   ├── exception/      # 异常处理
│   │   ├── interceptor/    # 拦截器
│   │   ├── mapper/         # MyBatis接口
│   │   ├── service/        # 服务层
│   │   ├── util/           # 工具类
│   │   └── CheckAppApplication.java  # 启动类
│   └── resources/
│       ├── mapper/         # MyBatis XML映射
│       ├── db/             # 数据库脚本
│       └── application.yml # 应用配置
└── pom.xml                # Maven配置
```

### 核心设计

#### 1. 分层架构
- **Controller层**：处理HTTP请求，参数验证，返回结果
- **Service层**：业务逻辑处理
- **Mapper/DAO层**：数据访问
- **Entity层**：领域模型
- **DTO层**：数据传输对象，API请求和响应

#### 2. 安全设计
- 基于Spring Security和JWT的认证授权
- 接口权限控制
- 跨域配置
- 输入验证和参数检查

#### 3. 数据库设计
- 使用MySQL关系型数据库
- 实体关系：用户、区域、巡检点、巡检记录等
- 使用Flyway管理数据库版本

#### 4. 异常处理
- 全局异常处理
- 统一响应格式
- 错误日志记录

---

## 4. 移动端架构设计

### Android应用结构
```
App/inspectionapp/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/pensun/inspection_app/
│   │   │   │   ├── bridge/        # JS桥接
│   │   │   │   ├── scan/          # 扫描功能
│   │   │   │   └── MainActivity.java
│   │   │   ├── res/               # 资源文件
│   │   │   ├── assets/            # 前端打包文件
│   │   │   └── AndroidManifest.xml
│   │   └── test/                  # 测试代码
│   └── build.gradle               # 构建脚本
└── gradle/                        # Gradle配置
```

### 核心设计

#### 1. WebView集成
- 使用Android WebView加载前端页面
- 配置WebView设置和权限
- 处理返回按钮和导航

#### 2. JavaScript Bridge
- 提供原生功能给前端JS调用
- 支持扫码、拍照等原生功能
- 安全控制和权限管理

#### 3. 扫描功能
- 实现二维码扫描
- 结果传递给前端页面
- 支持闪光灯和相机控制

---

## 5. 数据流程

### 用户认证流程
1. 用户输入账号密码
2. 前端调用登录API
3. 后端验证凭据并生成JWT Token
4. 前端存储Token并进入应用
5. 后续请求自动携带Token

### 巡检流程
1. 用户扫描区域二维码
2. 获取区域信息和巡检模板
3. 填写巡检表单
4. 提交巡检记录
5. 查看历史记录

---

## 6. 部署架构

### 开发环境
- 前端：本地开发服务器 (Vite)
- 后端：本地Spring Boot应用
- 数据库：本地MySQL

### 生产环境
- 前端：打包到Android应用assets目录
- 后端：Docker容器部署
- 数据库：独立MySQL服务器
- 反向代理：Nginx

---

## 7. 安全考虑

1. **认证与授权**：
   - JWT Token认证
   - 角色基础访问控制
   - 令牌过期管理

2. **数据安全**：
   - HTTPS传输
   - 敏感数据加密
   - 输入验证和过滤

3. **移动端安全**：
   - WebView安全配置
   - JavaScript Bridge安全控制
   - 应用权限最小化

4. **服务器安全**：
   - 防火墙设置
   - 定期更新和补丁
   - 日志监控和告警

---

## 8. 扩展性设计

1. **模块化设计**：
   - 功能模块独立
   - 接口标准化
   - 依赖注入便于扩展

2. **配置外部化**：
   - 环境特定配置
   - 功能开关
   - 外部化参数

3. **微服务准备**：
   - 服务边界清晰
   - API版本控制
   - 独立部署能力

---

## 9. 存在的问题和改进方向

1. **架构问题**：
   - 前端API调用分散，缺乏统一管理
   - 后端登录接口重复，认证入口不统一
   - 移动端原生功能与前端集成不充分

2. **技术改进**：
   - 引入API网关统一管理接口
   - 完善缓存策略
   - 添加消息队列处理异步任务
   - 完善监控和日志系统

3. **文档改进**：
   - 完善API文档
   - 添加架构图
   - 补充部署文档
   - 增加用户指南 