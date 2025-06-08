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
	areaHandler := NewAreaHandler()

	// API 版本分组
	v1 := r.Group("/api/v1")
	{
		// 认证相关路由
		auth := v1.Group("/auth")
		{
			auth.POST("/login", userHandler.Login)
			auth.POST("/logout", middleware.Auth(), userHandler.Logout)
		}

		// 用户相关路由
		user := v1.Group("/user")
		user.Use(middleware.Auth())
		{
			user.GET("/profile", userHandler.GetProfile)
			user.PUT("/profile", userHandler.UpdateProfile)
			user.GET("/statistics", userHandler.GetStatistics)
			user.GET("/list", middleware.AdminRequired(), userHandler.ListUsers)
		}

		// 区域相关路由
		area := v1.Group("/area")
		{
			area.GET("/qr", areaHandler.GetAreaByQRCode) // 扫码查询区域
			area.GET("/list", middleware.Auth(), areaHandler.GetAreaList)
			area.GET("/:id", middleware.Auth(), areaHandler.GetAreaDetail)
		}

		// 巡检相关路由
		inspection := v1.Group("/inspection")
		inspection.Use(middleware.Auth())
		{
			// 巡检点相关
			point := inspection.Group("/point")
			{
				point.GET("/:code", inspectionHandler.GetInspectionPoint)
				point.POST("/scan", inspectionHandler.ScanPoint)
			}

			// 巡检记录相关
			record := inspection.Group("/record")
			{
				record.POST("", inspectionHandler.CreateRecord)
				record.GET("", inspectionHandler.GetRecords)
				record.GET("/:id", inspectionHandler.GetRecord)
				record.PUT("/:id", inspectionHandler.UpdateRecord)
			}

			// 区域巡检相关
			area := inspection.Group("/area")
			{
				area.GET("/:area_id", inspectionHandler.GetInspectionsByArea)
			}

			// 用户巡检相关
			user := inspection.Group("/user")
			{
				user.GET("/:user_id", inspectionHandler.GetInspectionsByUser)
			}
		}
	}

	return r
}
