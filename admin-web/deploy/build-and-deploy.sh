#!/bin/bash

# Admin-Web 构建和部署脚本
# 使用方法: ./build-and-deploy.sh [环境] [版本]

set -e

# 默认参数
ENVIRONMENT=${1:-dev}
VERSION=${2:-latest}
REGISTRY=${DOCKER_REGISTRY:-"localhost:5000"}
NAMESPACE=${KUBERNETES_NAMESPACE:-"default"}

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

# 日志函数
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查依赖
check_dependencies() {
    log_info "检查依赖工具..."
    
    if ! command -v docker &> /dev/null; then
        log_error "Docker 未安装"
        exit 1
    fi
    
    if ! command -v helm &> /dev/null; then
        log_error "Helm 未安装"
        exit 1
    fi
    
    if ! command -v kubectl &> /dev/null; then
        log_error "kubectl 未安装"
        exit 1
    fi
    
    log_success "依赖检查完成"
}

# 构建Docker镜像
build_image() {
    log_info "构建Docker镜像..."
    
    IMAGE_NAME="${REGISTRY}/admin-web:${VERSION}"
    
    # 切换到项目根目录
    cd "$(dirname "$0")/.."
    
    # 构建镜像
    docker build -t "${IMAGE_NAME}" .
    
    log_success "镜像构建完成: ${IMAGE_NAME}"
    
    # 推送镜像（如果不是本地registry）
    if [[ "${REGISTRY}" != "localhost:5000" ]]; then
        log_info "推送镜像到仓库..."
        docker push "${IMAGE_NAME}"
        log_success "镜像推送完成"
    fi
}

# 部署到Kubernetes
deploy_to_k8s() {
    log_info "部署到Kubernetes环境: ${ENVIRONMENT}"
    
    # 切换到deploy目录
    cd "$(dirname "$0")"
    
    # 设置Helm参数
    HELM_ARGS=(
        --set "image.repository=${REGISTRY}/admin-web"
        --set "image.tag=${VERSION}"
        --namespace "${NAMESPACE}"
        --create-namespace
    )
    
    # 根据环境选择配置文件
    case "${ENVIRONMENT}" in
        "dev")
            HELM_ARGS+=(-f "values.yaml")
            HELM_ARGS+=(--set "ingress.hosts[0].host=admin-dev.local")
            ;;
        "test")
            HELM_ARGS+=(-f "values.yaml")
            HELM_ARGS+=(--set "ingress.hosts[0].host=admin-test.yourdomain.com")
            HELM_ARGS+=(--set "replicaCount=1")
            ;;
        "prod")
            HELM_ARGS+=(-f "values-prod.yaml")
            ;;
        *)
            log_error "未知环境: ${ENVIRONMENT}"
            exit 1
            ;;
    esac
    
    # 检查是否已存在部署
    if helm list -n "${NAMESPACE}" | grep -q "admin-web"; then
        log_info "更新现有部署..."
        helm upgrade admin-web . "${HELM_ARGS[@]}"
    else
        log_info "创建新部署..."
        helm install admin-web . "${HELM_ARGS[@]}"
    fi
    
    log_success "部署完成"
}

# 验证部署
verify_deployment() {
    log_info "验证部署状态..."
    
    # 等待Pod就绪
    kubectl wait --for=condition=ready pod -l app.kubernetes.io/name=admin-web -n "${NAMESPACE}" --timeout=300s
    
    # 检查Pod状态
    kubectl get pods -l app.kubernetes.io/name=admin-web -n "${NAMESPACE}"
    
    log_success "部署验证完成"
}

# 主函数
main() {
    log_info "开始构建和部署 Admin-Web"
    log_info "环境: ${ENVIRONMENT}, 版本: ${VERSION}"
    
    check_dependencies
    build_image
    deploy_to_k8s
    verify_deployment
    
    log_success "构建和部署完成！"
}

# 执行主函数
main "$@" 