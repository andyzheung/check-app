// API基础配置
const config = {
    // 开发环境
    development: {
        baseURL: '/api'  // 使用Vite代理
    },
    // 生产环境
    production: {
        baseURL: '/api'  // Android中会使用相对路径
    }
}

export default config[import.meta.env.MODE] 