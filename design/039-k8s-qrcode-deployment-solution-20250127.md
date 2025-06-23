# K8s容器化部署二维码功能解决方案

**文档编号**: 039  
**创建时间**: 2025-01-27  
**文档版本**: v1.0  
**作者**: Claude Code  

---

## 📋 **问题背景**

在K8s容器化部署环境中，前端Vue应用和后端Spring Boot应用运行在不同的Pod中，需要解决二维码静态资源的访问问题。

### **开发环境 vs 生产环境差异**

| 环境 | 前端 | 后端 | 静态资源访问方式 |
|------|------|------|------------------|
| **开发环境** | localhost:8082 (Vue DevServer) | localhost:8080 (Spring Boot) | Vue代理转发 |
| **生产环境** | Pod (Nginx) | Pod (Spring Boot) | Nginx反向代理 |

---

## 🎯 **解决方案架构**

### **网络拓扑**
```
Internet
    ↓
[Ingress/LoadBalancer]
    ↓
[admin-web Pod - Nginx]
    ↓ (代理)
[check-app-server Pod - Spring Boot]
    ↓ (挂载)
[PersistentVolume - 存储二维码文件]
```

### **关键组件**

1. **Nginx反向代理** - 统一处理静态资源和API请求
2. **PersistentVolume** - 持久化存储二维码文件
3. **Service网络** - Pod间通信

---

## 🔧 **实施方案**

### **1. 前端Nginx配置修改**

**文件**: `admin-web/deploy/default.conf`

```nginx
# 二维码静态资源代理到后端服务
location /qrcode/ {
    proxy_pass http://check-app-server:8080/qrcode/;
    proxy_set_header Host $host;
    proxy_set_header X-Real-IP $remote_addr;
    proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    proxy_set_header X-Forwarded-Proto $scheme;
    
    # 静态资源缓存策略
    expires 30d;
    add_header Cache-Control "public, immutable";
}

# 上传文件静态资源代理到后端服务  
location /uploads/ {
    proxy_pass http://check-app-server:8080/uploads/;
    # ... 相同的代理配置
    expires 7d;
    add_header Cache-Control "public";
}
```

### **2. 后端持久化存储配置**

**文件**: `check-app-server/helm/values.yaml`

```yaml
# 持久化存储配置
persistence:
  enabled: true
  storageClass: ""  # 根据集群配置
  size: 10Gi
  accessMode: ReadWriteOnce
  mountPath: /app/data
  
# 应用配置
config:
  qrcode:
    output-dir: /app/data/qrcode
  file:
    upload-dir: /app/data/uploads
```

### **3. Deployment配置**

**文件**: `check-app-server/helm/templates/deployment.yaml`

```yaml
# 环境变量
env:
  - name: QRCODE_OUTPUT_DIR
    value: /app/data/qrcode
  - name: FILE_UPLOAD_DIR
    value: /app/data/uploads

# 存储卷挂载
volumeMounts:
  - name: data-storage
    mountPath: /app/data

# 存储卷
volumes:
  - name: data-storage
    persistentVolumeClaim:
      claimName: check-app-server-pvc
```

### **4. PVC配置**

**文件**: `check-app-server/helm/templates/pvc.yaml`

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: check-app-server-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi
```

---

## 🚀 **部署流程**

### **1. 构建镜像**

```bash
# 构建后端镜像
cd check-app-server
docker build -t check-app-server:1.0.0 .

# 构建前端镜像
cd admin-web
npm run build
docker build -t admin-web:1.0.0 .
```

### **2. 部署到K8s**

```bash
# 部署后端服务
cd check-app-server/helm
helm install check-app-server . \
  --set image.tag=1.0.0 \
  --set persistence.enabled=true

# 部署前端服务
cd admin-web/deploy
helm install admin-web . \
  --set image.tag=1.0.0
```

### **3. 验证部署**

```bash
# 检查Pod状态
kubectl get pods

# 检查PVC状态
kubectl get pvc

# 检查服务
kubectl get svc

# 测试二维码功能
curl -X POST http://admin-web/api/v1/areas/1/qrcode
curl http://admin-web/qrcode/DC001_xxx.png
```

---

## 📊 **优势对比**

### **❌ 错误方案（开发环境思维）**
```javascript
// Vue.config.js - 仅开发环境有效
proxy: {
  '/qrcode': {
    target: 'http://localhost:8080'  // 硬编码端口
  }
}
```

### **✅ 正确方案（生产环境）**
```nginx
# Nginx配置 - 生产环境通用
location /qrcode/ {
  proxy_pass http://check-app-server:8080/qrcode/;  # Service名称
}
```

---

## 🛡️ **最佳实践**

### **1. 存储策略**
- **二维码文件**: 30天缓存，不可变内容
- **上传文件**: 7天缓存，可能更新
- **PV大小**: 根据业务量评估，建议10Gi起步

### **2. 安全配置**
```nginx
# 安全头
add_header X-Content-Type-Options nosniff;
add_header X-Frame-Options DENY;

# 访问控制
location ~ /\. {
    deny all;  # 禁止访问隐藏文件
}
```

### **3. 监控指标**
- PV使用率
- 静态资源访问成功率
- 缓存命中率
- 文件生成耗时

### **4. 扩展性考虑**
- **多副本**: 使用ReadWriteMany存储类
- **CDN**: 对外暴露时考虑CDN加速
- **备份**: 定期备份PV数据

---

## 🔍 **故障排查**

### **常见问题**

1. **二维码显示404**
   ```bash
   # 检查Nginx代理配置
   kubectl logs deployment/admin-web
   
   # 检查后端服务状态
   kubectl logs deployment/check-app-server
   ```

2. **文件无法保存**
   ```bash
   # 检查PVC状态
   kubectl describe pvc check-app-server-pvc
   
   # 检查挂载权限
   kubectl exec -it pod/check-app-server-xxx -- ls -la /app/data
   ```

3. **跨Pod访问失败**
   ```bash
   # 检查Service配置
   kubectl get svc check-app-server -o yaml
   
   # 测试Pod间网络
   kubectl exec -it pod/admin-web-xxx -- curl http://check-app-server:8080/health
   ```

---

## 📈 **性能优化**

### **1. 缓存策略**
```nginx
# 二维码长期缓存
location /qrcode/ {
    expires 30d;
    add_header Cache-Control "public, immutable";
}
```

### **2. 压缩配置**
```nginx
# 启用gzip压缩
gzip on;
gzip_types image/png image/jpeg;
```

### **3. 存储优化**
- 使用SSD存储类提高IO性能
- 定期清理过期文件
- 考虑对象存储（如MinIO）替代本地存储

---

## 🎯 **总结**

### **核心原则**
1. **环境分离**: 开发环境用Vue代理，生产环境用Nginx代理
2. **服务发现**: 使用K8s Service名称而非IP地址
3. **持久化存储**: 使用PV确保文件不丢失
4. **统一入口**: 通过前端Nginx统一处理所有请求

### **关键配置**
- ✅ Nginx反向代理配置
- ✅ PersistentVolume挂载
- ✅ 环境变量注入
- ✅ Service网络配置

这个方案是**生产级别的标准做法**，适用于所有K8s容器化部署场景。 