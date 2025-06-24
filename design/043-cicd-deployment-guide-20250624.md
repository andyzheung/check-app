# 多工程 CI/CD 部署指南

**文档编号**: 043  
**创建日期**: 2025-06-24  
**更新日期**: 2025-06-24  
**版本**: v1.0  

## 📋 概述

本指南详细说明如何配置和使用巡检系统的多工程 CI/CD 流水线，包括后端服务、管理后台前端和 Android 应用的独立构建部署。

### 支持的工程
- **check-app-server**: Spring Boot 后端服务
- **admin-web**: Vue.js 管理后台前端
- **App/inspectionapp**: Android 移动应用

## 🛠️ 环境准备

### GitLab 环境要求
- GitLab CE/EE 13.0+
- GitLab Runner 支持 Docker
- Kubernetes 集群访问权限
- Harbor 镜像仓库访问权限

### 必需的 GitLab Variables

#### 镜像仓库配置
```bash
# 镜像仓库地址
REGISTRY_ADDRESS=harbor-internal.ppp.com

# 镜像仓库认证
REGISTRY_USERNAME=your-username
REGISTRY_PASSWORD=your-password

# 自签名证书 (如果需要)
SELF_CA=-----BEGIN CERTIFICATE-----...-----END CERTIFICATE-----
```

#### Kubernetes 集群配置
```bash
# 测试环境 Kubernetes 配置 (Base64 编码)
UNIOPS_TEST_KUBECONFIG=LS0tLS1CRUdJTi...

# 生产环境 Kubernetes 配置 (Base64 编码)
UNIOPS_KUBECONFIG=LS0tLS1CRUdJTi...
```

#### Android 签名配置 (可选)
```bash
# Android 密钥库文件 (Base64 编码)
ANDROID_KEYSTORE_BASE64=UEsDBAoAAAAAAP...

# 密钥库密码
ANDROID_KEYSTORE_PASSWORD=your-keystore-password

# 密钥密码
ANDROID_KEY_PASSWORD=your-key-password

# 密钥别名
ANDROID_KEY_ALIAS=your-key-alias
```

## 📁 项目结构

```
check-app/
├── .gitlab-ci.yml                    # 主 CI 配置文件
├── check-app-server/                 # 后端服务
│   ├── .gitlab-ci-backend.yml        # 后端 CI 配置
│   ├── Dockerfile                    # 后端构建镜像
│   ├── helm/                         # Helm Chart
│   └── pom.xml                       # Maven 配置
├── admin-web/                        # 管理后台前端
│   ├── .gitlab-ci-frontend.yml       # 前端 CI 配置
│   ├── Dockerfile                    # 前端构建镜像
│   ├── deploy/                       # Helm Chart
│   └── package.json                  # NPM 配置
└── App/inspectionapp/                # Android 应用
    ├── .gitlab-ci-android.yml        # Android CI 配置
    ├── Dockerfile.android            # Android 构建环境
    ├── app/                          # Android 源码
    └── build.gradle                  # Gradle 配置
```

## 🚀 部署流程

### 1. 初始化配置

#### 1.1 配置 GitLab Variables
在 GitLab 项目设置中添加所有必需的环境变量：

1. 进入项目 → Settings → CI/CD → Variables
2. 添加上述所有变量
3. 标记敏感变量为 "Protected" 和 "Masked"

#### 1.2 验证 Runner 配置
确保 GitLab Runner 支持：
- Docker-in-Docker (dind)
- Kubernetes 访问
- 足够的资源 (CPU: 4核, 内存: 8GB)

### 2. 工程构建流程

#### 2.1 自动触发构建
当以下路径有代码变更时，会自动触发对应的构建：

```yaml
# 后端构建触发条件
changes:
  - "check-app-server/**/*"

# 前端构建触发条件  
changes:
  - "admin-web/**/*"

# Android 构建触发条件
changes:
  - "App/inspectionapp/**/*"
```

#### 2.2 构建阶段说明

**Build 阶段**:
- 代码编译和打包
- Docker 镜像构建
- 单元测试执行
- 代码质量检查

**Package 阶段**:
- Helm Chart 打包
- Android APK 签名
- 制品归档

**Deploy 阶段**:
- 测试环境部署 (手动触发)
- 生产环境部署 (手动触发)

### 3. 部署操作步骤

#### 3.1 后端服务部署

**测试环境部署**:
1. 代码提交到 `dev` 或 `master` 分支
2. 等待构建完成
3. 在 GitLab Pipeline 页面点击 "deploy_backend_to_test"
4. 确认部署参数后执行
5. 验证服务状态: `https://check-app-server-test.example.com`

**生产环境部署**:
1. 确保测试环境部署成功
2. 在 GitLab Pipeline 页面点击 "deploy_backend_to_prod"
3. 确认部署参数后执行
4. 验证服务状态: `https://check-app-server.example.com`

#### 3.2 前端管理后台部署

**测试环境部署**:
1. 代码提交到 `dev` 或 `master` 分支
2. 等待构建完成
3. 在 GitLab Pipeline 页面点击 "deploy_frontend_to_test"
4. 确认部署参数后执行
5. 验证服务状态: `https://admin-web-test.example.com`

**生产环境部署**:
1. 确保测试环境部署成功
2. 在 GitLab Pipeline 页面点击 "deploy_frontend_to_prod"
3. 确认部署参数后执行
4. 验证服务状态: `https://admin-web.example.com`

#### 3.3 Android 应用发布

**测试版本发布**:
1. 代码提交到 `dev` 或 `master` 分支
2. 等待构建完成
3. 在 GitLab Pipeline 页面点击 "deploy_android_to_test"
4. 获取 APK 下载链接进行测试

**生产版本发布**:
1. 确保测试版本验证通过
2. 在 GitLab Pipeline 页面点击 "deploy_android_to_prod"
3. 获取签名 APK 进行正式发布

## 🔧 配置详解

### Helm Chart 配置

#### 后端服务 values.yaml
```yaml
# check-app-server/helm/values_test.yaml
image:
  repository: harbor-internal.ppp.com/check-app/check-app-server
  tag: latest
  pullPolicy: Always

service:
  type: ClusterIP
  port: 8080

ingress:
  enabled: true
  hosts:
    - host: check-app-server-test.example.com
      paths: ["/"]

resources:
  limits:
    cpu: 2000m
    memory: 2Gi
  requests:
    cpu: 500m
    memory: 1Gi
```

#### 前端服务 values.yaml
```yaml
# admin-web/deploy/values_test.yaml
image:
  repository: harbor-internal.ppp.com/check-app/admin-web
  tag: latest
  pullPolicy: Always

service:
  type: ClusterIP
  port: 80

ingress:
  enabled: true
  hosts:
    - host: admin-web-test.example.com
      paths: ["/"]

resources:
  limits:
    cpu: 500m
    memory: 512Mi
  requests:
    cpu: 100m
    memory: 128Mi
```

### Docker 镜像优化

#### 后端 Dockerfile 优化建议
```dockerfile
# 多阶段构建
FROM maven:3.8-openjdk-11 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

#### 前端 Dockerfile 优化建议
```dockerfile
# 多阶段构建
FROM node:18-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

## 📊 监控和日志

### 构建监控

#### 查看构建状态
1. 进入项目 → CI/CD → Pipelines
2. 查看每个 Pipeline 的执行状态
3. 点击具体 Job 查看详细日志

#### 常见构建问题排查
```bash
# 镜像拉取失败
Error: failed to pull image

# 解决方案: 检查镜像仓库凭据
echo "$REGISTRY_PASSWORD" | docker login $REGISTRY_ADDRESS -u "$REGISTRY_USERNAME"

# Kubernetes 部署失败
Error: connection refused

# 解决方案: 检查 KUBECONFIG 配置
kubectl cluster-info --kubeconfig=/tmp/kubeconfig
```

### 应用监控

#### 健康检查端点
```bash
# 后端服务健康检查
curl https://check-app-server.example.com/actuator/health

# 前端服务健康检查
curl https://admin-web.example.com/health
```

#### 日志查看
```bash
# 查看 Pod 日志
kubectl logs -f deployment/check-app-server -n check-app

# 查看 Ingress 日志
kubectl logs -f deployment/nginx-ingress-controller -n ingress-nginx
```

## 🔒 安全最佳实践

### 密钥管理
1. **使用 GitLab Protected Variables**: 敏感信息必须标记为 Protected
2. **定期轮换密钥**: 每季度更换一次密钥
3. **最小权限原则**: 只授予必要的权限

### 镜像安全
1. **基础镜像选择**: 使用官方或可信的基础镜像
2. **漏洞扫描**: 定期扫描镜像漏洞
3. **镜像签名**: 生产环境使用签名镜像

### 网络安全
1. **TLS 加密**: 所有外部通信使用 HTTPS
2. **网络策略**: 配置 Kubernetes NetworkPolicy
3. **访问控制**: 使用 RBAC 控制访问权限

## 🚨 故障排除

### 常见问题及解决方案

#### 1. 构建失败
```bash
# 问题: Maven 构建失败
Error: Could not resolve dependencies

# 解决方案: 清理缓存重新构建
mvn clean install -U
```

#### 2. 部署失败
```bash
# 问题: Helm 部署失败
Error: release failed

# 解决方案: 检查 values 文件配置
helm template check-app-server ./helm --values ./helm/values_test.yaml
```

#### 3. Android 构建失败
```bash
# 问题: Android SDK 许可证未接受
Error: Failed to install the following Android SDK packages

# 解决方案: 在 Dockerfile 中添加许可证接受
RUN yes | sdkmanager --licenses
```

### 回滚操作

#### Kubernetes 服务回滚
```bash
# 查看部署历史
helm history check-app-server -n check-app

# 回滚到上一个版本
helm rollback check-app-server 1 -n check-app
```

#### 镜像版本回滚
```bash
# 使用之前的镜像版本重新部署
helm upgrade check-app-server ./helm \
  --set image.tag=previous-version \
  --namespace check-app
```

## 📈 性能优化

### 构建性能优化

#### 1. 缓存策略
```yaml
# Maven 缓存
cache:
  key: maven-$CI_PROJECT_ID
  paths:
    - .m2/repository/

# NPM 缓存
cache:
  key: npm-$CI_PROJECT_ID
  paths:
    - node_modules/
    - .npm/

# Gradle 缓存
cache:
  key: gradle-$CI_PROJECT_ID
  paths:
    - .gradle/
    - build/
```

#### 2. 并行构建
```yaml
# 使用 needs 关键字实现并行构建
deploy_backend_to_test:
  needs: ["build_backend", "package_backend_chart"]
  
deploy_frontend_to_test:
  needs: ["build_frontend", "package_frontend_chart"]
```

### 运行时性能优化

#### 1. 资源配置
```yaml
# 合理设置资源限制
resources:
  limits:
    cpu: 2000m
    memory: 2Gi
  requests:
    cpu: 500m
    memory: 1Gi
```

#### 2. 健康检查配置
```yaml
# 配置健康检查
livenessProbe:
  httpGet:
    path: /actuator/health
    port: 8080
  initialDelaySeconds: 60
  periodSeconds: 30

readinessProbe:
  httpGet:
    path: /actuator/health
    port: 8080
  initialDelaySeconds: 30
  periodSeconds: 10
```

## 📋 检查清单

### 部署前检查
- [ ] 所有环境变量已配置
- [ ] GitLab Runner 运行正常
- [ ] Kubernetes 集群可访问
- [ ] 镜像仓库凭据有效
- [ ] Helm Chart 配置正确

### 部署后验证
- [ ] 服务健康检查通过
- [ ] API 接口响应正常
- [ ] 前端页面加载正常
- [ ] Android APK 可正常安装
- [ ] 日志输出正常

### 生产环境检查
- [ ] 负载均衡配置正确
- [ ] SSL 证书有效
- [ ] 监控告警配置
- [ ] 备份策略执行
- [ ] 安全扫描通过

## 🔄 维护和更新

### 定期维护任务
1. **每周**: 检查构建状态和失败率
2. **每月**: 更新基础镜像和依赖
3. **每季度**: 审核和更新密钥
4. **每半年**: 评估和优化流水线性能

### 版本升级
1. **测试环境验证**: 先在测试环境验证新版本
2. **灰度发布**: 逐步推广到生产环境
3. **监控观察**: 密切监控系统指标
4. **回滚准备**: 准备快速回滚方案

---

**联系方式**: 如有问题请联系 DevOps 团队  
**文档维护**: 本文档随系统更新同步维护 