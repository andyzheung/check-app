package main

import (
	"log"

	"check-app-backend/internal/handler"
	"check-app-backend/internal/repository"
)

func main() {
	// 初始化数据库连接
	if err := repository.InitDB(); err != nil {
		log.Fatalf("数据库连接失败: %v", err)
	}

	// 设置路由
	r := handler.SetupRouter()

	// 启动服务器
	log.Println("服务器启动在 :8080 端口...")
	if err := r.Run(":8080"); err != nil {
		log.Fatalf("服务器启动失败: %v", err)
	}
}
