# 巡检App一体化项目说明

## 项目简介

本项目为企业级巡检管理系统，包含移动端App、Web端管理后台、后端服务、设计文档、UI原型及产品需求文档等。系统实现了巡检任务的排班、执行、数据采集、统计分析、权限管理等全流程数字化，支持AD域账号集成，适用于大中型企业的设备巡检、运维管理场景。

---

## 目录结构总览

```
├── App/                # 移动端与Web端App源码
│   ├── inspectionapp/  # Android原生移动端App（Kotlin/Java，Gradle项目）
│   ├── web/            # H5/PC端Web App（Vue3+Vite）
│   └── Arch/           # App架构相关文档
├── admin-web/          # 后台管理系统（Vue3 + Ant Design Vue）
├── check-app-server/   # 后端服务（Spring Boot，Java）
├── design/             # 详细设计文档（数据库、API、系统架构、流程等）
├── Admin-UI/           # 后台管理UI高保真原型（HTML静态页面）
├── UI原型/             # App端UI原型（HTML静态页面）
├── PRD/                # 产品需求文档（PRD）
└── ...                 # 其他工程/配置/工具目录
```

---

## 关键工程目录说明

### 1. App/

- **inspectionapp/**：Android原生App源码，负责巡检任务执行、扫码、拍照、数据采集、离线缓存等。采用Kotlin/Java开发，Gradle构建。
- **web/**：H5/PC端Web App，支持部分巡检功能的Web访问，基于Vue3+Vite开发，适配移动端和桌面端浏览器。
- **Arch/**：App端架构设计、技术选型、流程说明等文档。

### 2. admin-web/

- 后台管理系统前端，基于Vue3、Ant Design Vue、Pinia等技术栈。
- 实现排班管理、用户与角色管理、权限分配、巡检记录、统计报表、系统配置（含AD域集成）等功能。
- 目录结构清晰，支持模块化开发与权限控制。

### 3. check-app-server/

- 后端服务，采用Spring Boot开发，RESTful API风格。
- 负责用户认证（含AD域）、排班、巡检任务、数据存储、权限校验、统计分析等。
- 支持MySQL数据库，包含Dockerfile、Helm部署脚本，便于容器化与云原生部署。

### 4. design/

- 详细设计文档，包括：
  - 排班系统、用户角色管理、AD集成、数据库结构、API接口、系统架构、流程设计等。
  - 设计文档均为Markdown格式，便于查阅和维护。

### 5. Admin-UI/

- 后台管理系统的高保真UI原型，HTML静态页面，覆盖用户、排班、记录、问题、仪表盘等核心模块。
- 便于前端开发参考和交互细节确认。

### 6. UI原型/

- App端（移动端）UI原型，HTML静态页面，包含扫码、巡检表单、主界面、个人中心等。
- 用于产品设计、开发和测试阶段的交互参考。

### 7. PRD/

- 产品需求文档（Product Requirement Document），详细描述业务背景、核心需求、功能模块、用户角色、流程等。
- 便于团队理解全局目标和业务边界。

---

## 技术栈

- **前端**：Vue3、Vite、Ant Design Vue、Pinia、ESLint
- **移动端**：Kotlin/Java（Android）、H5（Vue3）
- **后端**：Spring Boot、MySQL、JWT、LDAP（AD域集成）、Docker、Helm
- **文档/原型**：Markdown、HTML

---

## 典型业务流程

1. **用户登录**：支持本地账号和AD域账号登录，权限自动分配。
2. **排班管理**：管理员通过后台为运维人员排班，支持日历/列表视图、冲突检测、统计分析。
3. **巡检执行**：运维人员在App端接收任务，扫码/拍照/填写表单，数据实时/离线上传。
4. **数据管理**：后台可查看、导出巡检记录，处理异常，生成统计报表。
5. **权限管理**：支持多角色（管理员/运维/普通用户）、细粒度权限分配，AD组自动映射。

---

## 目录结构示意

```
check-app/
├── App/
│   ├── inspectionapp/      # Android原生App
│   ├── web/                # H5/PC端Web App
│   └── Arch/               # App架构文档
├── admin-web/              # 后台管理前端
├── check-app-server/       # 后端服务
├── design/                 # 设计文档
├── Admin-UI/               # 后台UI原型
├── UI原型/                 # App UI原型
├── PRD/                    # 产品需求文档
└── ...
```

---

## 快速开始

1. **后端服务启动**
   - 进入 `check-app-server` 目录，配置数据库连接（`src/main/resources/application.yml`），确保MySQL已启动。
   - 本地开发环境启动：
     ```bash
     cd check-app-server
     mvn spring-boot:run
     ```
   - Docker容器运行：
     ```bash
     cd check-app-server
     docker build -t check-app-server .
     docker run -p 8080:8080 check-app-server
     ```
   - 访问接口文档或健康检查：
     - http://localhost:8080/actuator/health
     - 具体API见 `design/api-interface-design.md`

2. **后台管理前端启动**
   - 进入 `admin-web` 目录，安装依赖并启动开发服务器：
     ```bash
     cd admin-web
     npm install
     npm run dev
     ```
   - 默认端口为3000，浏览器访问：http://localhost:3000
   - 如需打包生产环境：
     ```bash
     npm run build
     ```

3. **App端开发/调试**
   - **Android原生App**：
     - 用Android Studio打开 `App/inspectionapp` 目录，连接设备或模拟器，点击运行。
     - 也可命令行编译：
       ```bash
       cd App/inspectionapp
       ./gradlew assembleDebug
       # 生成的apk在 app/build/outputs/apk/debug/
       ```
   - **Web App（H5/PC）**：
     - 进入 `App/web` 目录，安装依赖并启动：
       ```bash
       cd App/web
       npm install
       npm run dev
       ```
     - 默认端口为5173，浏览器访问：http://localhost:5173

4. **设计文档与原型查看**
   - 设计文档：用Markdown阅读器打开 `design/` 下的 `.md` 文件
   - 后台UI原型：用浏览器打开 `Admin-UI/` 下的 `.html` 文件
   - App UI原型：用浏览器打开 `UI原型/` 下的 `.html` 文件
   - PRD文档：用Markdown阅读器打开 `PRD/巡检App-PRD.md`

---

## 其他说明

- **AD域集成**：详见 `design/ad-integration-design.md`，支持企业统一身份认证。
- **排班与权限**：详见 `design/scheduling-system-design.md`、`design/user-role-management.md`。
- **API接口**：详见 `design/api-interface-design.md`。
- **如需定制开发、二次集成或技术支持，请联系项目维护人。**

---
