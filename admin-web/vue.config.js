module.exports = {
  devServer: {
    port: 8082,
    open: true,
    proxy: {
      '/api/v1': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      // 二维码静态资源代理
      '/qrcode': {
        target: 'http://localhost:8080',
        changeOrigin: true
      },
      // 上传文件静态资源代理
      '/uploads': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
} 