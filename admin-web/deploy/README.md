# Admin-Web Kubernetes 部署指南

## 📋 **部署概述**

这是巡检系统管理后台的Kubernetes部署配置，基于Helm Chart管理。

### 🏗️ **架构组件**
- **Docker镜像**: 多阶段构建，包含Nginx + Vue.js应用
- **Kubernetes资源**: Deployment, Service, Ingress, ConfigMap等
- **Helm Chart**: 参数化配置，支持多环境部署
- **API代理**: Nginx反向代理到check-app-server后端

## 🚀 **快速部署**

### 1. 构建Docker镜像
```bash
# 在admin-web目录下执行
docker build -t admin-web:latest .

# 推送到镜像仓库（可选）
docker tag admin-web:latest your-registry.com/admin-web:v1.0.0
docker push your-registry.com/admin-web:v1.0.0
```

### 2. 部署到Kubernetes
```bash
# 开发环境部署
helm install admin-web ./deploy

# 生产环境部署
helm install admin-web ./deploy -f ./deploy/values-prod.yaml

# 升级部署
helm upgrade admin-web ./deploy

# 卸载
helm uninstall admin-web
```

## ⚙️ **配置说明**

### 核心配置文件
- `values.yaml`: 默认配置
- `values-prod.yaml`: 生产环境配置
- `Chart.yaml`: Helm Chart元数据

### 重要配置项
```yaml
# 镜像配置
image:
  repository: admin-web
  tag: latest

# 后端服务配置
backend:
  service: check-app-server  # K8s服务名
  port: 8080

# Ingress配置
ingress:
  enabled: true
  host: admin.yourdomain.com
```

## 🔧 **环境配置**

### 开发环境
```bash
helm install admin-web ./deploy \
  --set image.tag=dev \
  --set ingress.hosts[0].host=admin-dev.local
```

### 测试环境
```bash
helm install admin-web ./deploy \
  --set image.tag=test \
  --set ingress.hosts[0].host=admin-test.yourdomain.com \
  --set replicaCount=1
```

### 生产环境
```bash
helm install admin-web ./deploy \
  -f ./deploy/values-prod.yaml \
  --set image.tag=v1.0.0
```

## 🔍 **健康检查**

### 检查Pod状态
```bash
kubectl get pods -l app.kubernetes.io/name=admin-web
kubectl describe pod <pod-name>
kubectl logs <pod-name>
```

### 检查服务状态
```bash
kubectl get svc admin-web
kubectl get ingress admin-web
```

### 健康检查端点
- `GET /health`: 存活检查
- `GET /ready`: 就绪检查

## 🐛 **故障排查**

### 常见问题

#### 1. Pod启动失败
```bash
# 查看Pod事件
kubectl describe pod <pod-name>

# 查看容器日志
kubectl logs <pod-name> -c admin-web
```

#### 2. 无法访问后端API
- 检查backend.service配置是否正确
- 确认check-app-server服务是否运行
- 验证网络策略配置

#### 3. Ingress无法访问
- 检查Ingress Controller是否安装
- 验证域名解析
- 检查TLS证书配置

### 调试命令
```bash
# 进入容器调试
kubectl exec -it <pod-name> -- /bin/sh

# 测试API连接
kubectl exec -it <pod-name> -- curl http://check-app-server:8080/api/health

# 查看Nginx配置
kubectl exec -it <pod-name> -- cat /etc/nginx/conf.d/default.conf
```

## 📊 **监控和日志**

### 日志收集
```bash
# 查看实时日志
kubectl logs -f deployment/admin-web

# 查看历史日志
kubectl logs deployment/admin-web --previous
```

### 监控指标
- CPU使用率
- 内存使用率
- 请求响应时间
- 错误率

## 🔄 **更新和回滚**

### 更新应用
```bash
# 更新镜像版本
helm upgrade admin-web ./deploy --set image.tag=v1.1.0

# 更新配置
helm upgrade admin-web ./deploy -f new-values.yaml
```

### 回滚操作
```bash
# 查看发布历史
helm history admin-web

# 回滚到上一版本
helm rollback admin-web

# 回滚到指定版本
helm rollback admin-web 1
```

## 🔐 **安全配置**

### 镜像安全
- 使用非root用户运行
- 最小权限原则
- 定期更新基础镜像

### 网络安全
- 配置网络策略
- 启用TLS加密
- 限制API访问

### 运行时安全
- 只读文件系统
- 资源限制
- 安全上下文配置

## 📋 **生产环境检查清单**

### 部署前检查
- [ ] 镜像已构建并推送到仓库
- [ ] 配置文件已更新
- [ ] 域名和证书已准备
- [ ] 后端服务已部署

### 部署后验证
- [ ] Pod状态正常
- [ ] 服务可访问
- [ ] API代理正常
- [ ] 健康检查通过
- [ ] 监控指标正常

## 🛠️ **开发和测试**

### 本地开发
```bash
# 使用minikube测试
minikube start
helm install admin-web ./deploy --set ingress.hosts[0].host=admin.local

# 端口转发测试
kubectl port-forward svc/admin-web 8080:80
```

### 模板验证
```bash
# 验证Helm模板
helm template admin-web ./deploy > rendered.yaml

# 干运行测试
helm install admin-web ./deploy --dry-run --debug
```

## 📞 **支持和联系**

如遇到问题，请联系：
- 技术支持: tech-support@example.com
- 运维团队: devops@example.com

---

**注意**: 请根据实际环境调整配置参数，确保安全性和性能要求。 