package handler

import (
	"github.com/gin-gonic/gin"
)

// RegisterRoutes 注册所有路由
func RegisterRoutes(r *gin.Engine) {
	// API 版本分组
	v1 := r.Group("/api/v1")
	{
		// 认证相关路由
		auth := v1.Group("/auth")
		{
			auth.POST("/login", Login)
			auth.POST("/logout", Logout)
		}

		// 巡检相关路由
		inspection := v1.Group("/inspection")
		{
			inspection.GET("/list", ListInspections)
			inspection.POST("/create", CreateInspection)
			inspection.GET("/:id", GetInspection)
			inspection.PUT("/:id", UpdateInspection)
			inspection.DELETE("/:id", DeleteInspection)
		}

		// 用户相关路由
		user := v1.Group("/user")
		{
			user.GET("/profile", GetUserProfile)
			user.PUT("/profile", UpdateUserProfile)
		}
	}
}
