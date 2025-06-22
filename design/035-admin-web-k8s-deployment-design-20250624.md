# Admin-Web Kubernetes部署设计方案

**文档创建时间**: 2025-06-24  
**设计范围**: admin-web前端项目的容器化部署  
**目标**: 基于Helm的Kubernetes部署，集成Nginx反向代理

## 🎯 **部署目标**

### 核心需求
1. **容器化部署**: 将Vue.js前端项目打包为Docker镜像
2. **Nginx集成**: 在容器内集成Nginx作为Web服务器和反向代理
3. **Kubernetes部署**: 使用Helm Chart管理Kubernetes资源
4. **后端代理**: 通过CoreDNS域名访问check-app-server后端服务
5. **生产优化**: 静态资源优化、缓存策略、健康检查

## 🏗️ **架构设计**

### 整体架构图
```
┌─────────────────────────────────────────────────────────────┐
│                     Kubernetes Cluster                      │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────────┐    ┌─────────────────┐                │
│  │   admin-web     │    │ check-app-server│                │
│  │   ┌─────────┐   │    │                 │                │
│  │   │  Nginx  │   │◄───┤   Spring Boot   │                │
│  │   └─────────┘   │    │                 │                │
│  │   ┌─────────┐   │    └─────────────────┘                │
│  │   │Vue.js App│   │                                       │
│  │   └─────────┘   │                                       │
│  └─────────────────┘                                       │
│           ▲                                                 │
│           │                                                 │
│  ┌─────────────────┐                                       │
│  │    Ingress      │                                       │
│  └─────────────────┘                                       │
└─────────────────────────────────────────────────────────────┘
           ▲
           │
    ┌─────────────┐
    │   Internet  │
    └─────────────┘
```

### 组件说明
- **Nginx**: 静态文件服务 + API反向代理
- **Vue.js App**: 构建后的静态资源
- **Ingress**: 外部访问入口
- **Service**: Kubernetes服务发现
- **ConfigMap**: Nginx配置文件管理

## 📦 **Docker镜像设计**

### 多阶段构建策略
```dockerfile
# Stage 1: Build
FROM node:18-alpine AS builder
# 构建Vue.js应用

# Stage 2: Production
FROM nginx:alpine
# 集成Nginx和静态文件
```

### 镜像特性
- **基础镜像**: nginx:alpine (轻量级)
- **构建优化**: 多阶段构建，减少镜像大小
- **配置管理**: 支持环境变量配置
- **健康检查**: 内置健康检查端点

## 🔧 **Nginx配置设计**

### 核心配置
1. **静态文件服务**: 高效服务Vue.js构建产物
2. **API代理**: 转发/api请求到后端服务
3. **缓存策略**: 静态资源长期缓存，API请求无缓存
4. **压缩**: Gzip压缩减少传输大小
5. **安全头**: 添加安全相关HTTP头

### 代理规则
```nginx
# API代理到后端服务
location /api/ {
    proxy_pass http://check-app-server:8080/api/;
    # 代理配置
}

# 静态文件服务
location / {
    try_files $uri $uri/ /index.html;
    # 缓存配置
}
```

## ☸️ **Kubernetes资源设计**

### Helm Chart结构
```
admin-web/
├── deploy/
│   ├── Chart.yaml
│   ├── values.yaml
│   ├── values-prod.yaml
│   └── templates/
│       ├── deployment.yaml
│       ├── service.yaml
│       ├── ingress.yaml
│       ├── configmap.yaml
│       └── _helpers.tpl
└── Dockerfile
```

### 资源配置
- **Deployment**: Pod副本管理，滚动更新
- **Service**: 内部服务发现
- **Ingress**: 外部访问路由
- **ConfigMap**: Nginx配置文件
- **HPA**: 水平自动扩缩容（可选）

## 🌐 **网络配置**

### 服务发现
- **后端服务**: `check-app-server.default.svc.cluster.local:8080`
- **DNS简化**: `check-app-server:8080` (同命名空间)
- **健康检查**: `/actuator/health`

### 端口规划
- **容器端口**: 80 (Nginx)
- **服务端口**: 80
- **Ingress端口**: 80/443

## 🔒 **安全配置**

### 容器安全
- **非root用户**: Nginx以非特权用户运行
- **只读文件系统**: 除必要目录外设为只读
- **资源限制**: CPU和内存限制

### 网络安全
- **HTTPS**: Ingress层面配置TLS
- **安全头**: X-Frame-Options, X-XSS-Protection等
- **CORS**: 配置跨域访问策略

## 📊 **监控和日志**

### 健康检查
- **Liveness Probe**: `/health`
- **Readiness Probe**: `/ready`
- **Startup Probe**: 启动检查

### 日志配置
- **访问日志**: Nginx访问日志
- **错误日志**: 应用错误日志
- **日志格式**: JSON格式便于解析

## 🚀 **部署流程**

### 构建流程
1. **代码构建**: `npm run build`
2. **镜像构建**: `docker build`
3. **镜像推送**: 推送到镜像仓库
4. **Helm部署**: `helm upgrade --install`

### 环境管理
- **开发环境**: values-dev.yaml
- **测试环境**: values-test.yaml
- **生产环境**: values-prod.yaml

## 📋 **配置参数**

### 关键配置
```yaml
# values.yaml示例
image:
  repository: admin-web
  tag: latest
  pullPolicy: IfNotPresent

service:
  type: ClusterIP
  port: 80

ingress:
  enabled: true
  host: admin.example.com
  tls: true

backend:
  service: check-app-server
  port: 8080

resources:
  limits:
    cpu: 200m
    memory: 256Mi
  requests:
    cpu: 100m
    memory: 128Mi
```

## 🔧 **运维考虑**

### 扩展性
- **水平扩展**: 支持多副本部署
- **资源调整**: 根据负载调整资源配置
- **版本管理**: 支持蓝绿部署和金丝雀发布

### 故障处理
- **健康检查**: 自动重启故障Pod
- **日志收集**: 集中化日志管理
- **监控告警**: 关键指标监控

## 📝 **实施计划**

### 阶段1: 基础容器化 (0.5天)
- [ ] 创建Dockerfile
- [ ] 配置Nginx
- [ ] 本地测试验证

### 阶段2: Helm Chart开发 (0.5天)
- [ ] 创建Helm模板
- [ ] 配置Kubernetes资源
- [ ] 参数化配置

### 阶段3: 部署测试 (0.5天)
- [ ] 集群部署测试
- [ ] 网络连通性验证
- [ ] 性能测试

### 验收标准
- [ ] 容器正常启动
- [ ] 前端页面正常访问
- [ ] API代理功能正常
- [ ] 健康检查通过
- [ ] Helm部署成功

## 🎯 **预期效果**

### 技术收益
- **部署自动化**: 一键部署和更新
- **环境一致性**: 开发、测试、生产环境统一
- **运维简化**: 标准化的运维流程
- **扩展性**: 支持水平扩展和负载均衡

### 业务价值
- **高可用**: 多副本部署保障服务可用性
- **快速交付**: 自动化部署提升交付效率
- **成本优化**: 资源按需分配，降低成本
- **安全性**: 容器化隔离和安全配置

---

**总结**: 该方案采用现代化的容器部署架构，通过Nginx + Kubernetes + Helm的组合，实现admin-web的高效、安全、可扩展的部署方案。 