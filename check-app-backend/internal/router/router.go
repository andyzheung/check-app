package router

import (
	"github.com/gin-gonic/gin"

	"check-app-backend/internal/handler"
	"check-app-backend/internal/middleware"
)

// SetupRouter 设置路由
func SetupRouter() *gin.Engine {
	r := gin.New()

	// 使用中间件
	r.Use(middleware.Logger())
	r.Use(middleware.Recovery())
	r.Use(middleware.CORSMiddleware())

	// API 路由组
	api := r.Group("/api/v1")
	{
		// 认证相关路由
		auth := api.Group("/auth")
		{
			userHandler := handler.NewUserHandler()
			auth.POST("/login", userHandler.Login)
			auth.POST("/logout", middleware.Auth(), userHandler.Logout)
		}

		// 用户相关路由
		users := api.Group("/users")
		users.Use(middleware.Auth())
		{
			userHandler := handler.NewUserHandler()
			users.GET("/profile", userHandler.GetProfile)
			users.PUT("/profile", userHandler.UpdateProfile)
			users.GET("/statistics", userHandler.GetStatistics)
			users.GET("", middleware.AdminRequired(), userHandler.ListUsers)
		}

		// 巡检相关路由
		inspections := api.Group("/inspections")
		inspections.Use(middleware.Auth())
		{
			inspectionHandler := handler.NewInspectionHandler()
			inspections.POST("/scan", inspectionHandler.ScanPoint)
			inspections.POST("/records", inspectionHandler.CreateRecord)
			inspections.GET("/records", inspectionHandler.GetRecords)
			inspections.GET("/records/:id", inspectionHandler.GetRecord)
			inspections.PUT("/records/:id", inspectionHandler.UpdateRecord)
			inspections.GET("/area/:areaID", inspectionHandler.GetByArea)
			inspections.GET("/user/:userID", inspectionHandler.GetByUser)
		}
	}

	return r
}
