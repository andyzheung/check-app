package handler

import (
	"github.com/gin-gonic/gin"

	"check-app-backend/internal/middleware"
)

// SetupRouter 设置路由
func SetupRouter() *gin.Engine {
	r := gin.New()

	// 使用中间件
	r.Use(middleware.Logger())
	r.Use(middleware.Recovery())
	r.Use(middleware.CORSMiddleware())

	// 创建处理器
	userHandler := NewUserHandler()
	inspectionHandler := NewInspectionHandler()

	// API路由组
	api := r.Group("/api")
	{
		// 用户相关路由
		api.POST("/login", userHandler.Login)
		api.POST("/logout", middleware.Auth(), userHandler.Logout)
		api.GET("/profile", middleware.Auth(), userHandler.GetProfile)
		api.PUT("/profile", middleware.Auth(), userHandler.UpdateProfile)
		api.GET("/statistics", middleware.Auth(), userHandler.GetStatistics)
		api.GET("/users", middleware.Auth(), middleware.AdminRequired(), userHandler.ListUsers)

		// 巡检相关路由
		api.POST("/inspection/scan", middleware.Auth(), inspectionHandler.ScanPoint)
		api.POST("/inspection/record", middleware.Auth(), inspectionHandler.CreateRecord)
		api.GET("/inspection/records", middleware.Auth(), inspectionHandler.GetRecords)
		api.GET("/inspection/record/:id", middleware.Auth(), inspectionHandler.GetRecord)
		api.PUT("/inspection/record/:id", middleware.Auth(), inspectionHandler.UpdateRecord)
		api.GET("/inspection/point/:code", middleware.Auth(), inspectionHandler.GetInspectionPoint)
		api.GET("/inspection/area/:area_id", middleware.Auth(), inspectionHandler.GetInspectionsByArea)
		api.GET("/inspection/user/:user_id", middleware.Auth(), inspectionHandler.GetInspectionsByUser)
	}

	return r
}
