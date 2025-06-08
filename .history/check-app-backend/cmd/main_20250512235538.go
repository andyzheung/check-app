package main

import (
	"log"

	"github.com/gin-gonic/gin"

	"check-app-backend/config"
	"check-app-backend/internal/handler"
	"check-app-backend/internal/middleware"
	"check-app-backend/pkg/database"
)

func main() {
	// 加载配置
	if err := config.Load(); err != nil {
		log.Fatalf("Failed to load config: %v", err)
	}

	// 初始化数据库连接
	if err := database.Init(); err != nil {
		log.Fatalf("Failed to initialize database: %v", err)
	}

	// 创建Gin引擎
	r := gin.Default()

	// 注册中间件
	r.Use(middleware.Cors())
	r.Use(middleware.Logger())
	r.Use(middleware.Recovery())

	// 注册路由
	handler.RegisterRoutes(r)

	// 启动服务器
	if err := r.Run(":8080"); err != nil {
		log.Fatalf("Failed to start server: %v", err)
	}
}
