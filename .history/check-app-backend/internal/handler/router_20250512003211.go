package handler

import (
	"inspection-app-backend/internal/middleware"

	"github.com/gin-gonic/gin"
)

// RegisterRoutes 注册所有路由
func RegisterRoutes(r *gin.Engine) {
	// 创建处理器实例
	userHandler := NewUserHandler()
	inspectionHandler := NewInspectionHandler()
	areaHandler := NewAreaHandler()

	// API 版本分组
	v1 := r.Group("/api/v1")
	{
		// 认证相关路由
		auth := v1.Group("/auth")
		{
			auth.POST("/login", userHandler.Login)
			auth.POST("/logout", middleware.AuthRequired(), userHandler.Logout)
		}

		// 用户相关路由
		user := v1.Group("/user")
		user.Use(middleware.AuthRequired())
		{
			user.GET("/profile", userHandler.GetUserProfile)
			user.PUT("/profile", userHandler.UpdateUserProfile)
			user.GET("/list", middleware.AdminRequired(), userHandler.ListUsers)
		}

		// 区域相关路由
		area := v1.Group("/area")
		{
			area.GET("/qr", areaHandler.GetAreaByQRCode) // 扫码查询区域
			area.GET("/list", middleware.AuthRequired(), areaHandler.GetAreaList)
			area.GET("/:id", middleware.AuthRequired(), areaHandler.GetAreaDetail)
		}

		// 巡检相关路由
		inspection := v1.Group("/inspection")
		inspection.Use(middleware.AuthRequired())
		{
			inspection.GET("/list", inspectionHandler.ListInspections)
			inspection.POST("/create", inspectionHandler.CreateInspection)
			inspection.GET("/:id", inspectionHandler.GetInspection)
			inspection.PUT("/:id", inspectionHandler.UpdateInspection)
			inspection.DELETE("/:id", inspectionHandler.DeleteInspection)
			inspection.GET("/area/:area_id", inspectionHandler.GetInspectionsByArea)
			inspection.GET("/user/:user_id", inspectionHandler.GetInspectionsByUser)
		}
	}
}
