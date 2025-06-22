# 多阶段构建 - Stage 1: 构建阶段
FROM node:18-alpine AS builder

# 设置工作目录
WORKDIR /app

# 复制package文件并安装依赖
COPY package*.json ./
RUN npm ci --only=production --silent

# 复制源代码并构建
COPY . .
RUN npm run build

# 多阶段构建 - Stage 2: 生产阶段
FROM nginx:alpine

# 创建非root用户
RUN addgroup -g 1001 -S nginx && \
    adduser -S -D -H -u 1001 -h /var/cache/nginx -s /sbin/nologin -G nginx -g nginx nginx

# 安装必要的工具
RUN apk add --no-cache curl

# 复制自定义nginx配置
COPY deploy/nginx.conf /etc/nginx/nginx.conf
COPY deploy/default.conf /etc/nginx/conf.d/default.conf

# 从构建阶段复制静态文件
COPY --from=builder /app/dist /usr/share/nginx/html

# 创建健康检查文件
RUN echo '{"status":"ok","timestamp":"'$(date -Iseconds)'"}' > /usr/share/nginx/html/health.json

# 创建必要的目录并设置权限
RUN mkdir -p /var/cache/nginx /var/log/nginx /var/run && \
    chown -R nginx:nginx /var/cache/nginx /var/log/nginx /var/run /usr/share/nginx/html && \
    chmod -R 755 /var/cache/nginx /var/log/nginx /var/run

# 暴露端口
EXPOSE 80

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost/health || exit 1

# 切换到非root用户
USER nginx

# 启动nginx
CMD ["nginx", "-g", "daemon off;"] 