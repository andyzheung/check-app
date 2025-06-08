package router

import (
	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"

	"check-app-backend/internal/handler"
	"check-app-backend/internal/middleware"
)

func SetupRouter() *gin.Engine {
	r := gin.Default()

	// 中间件
	r.Use(cors.New(cors.Config{
		AllowOrigins:     []string{"*"},
		AllowMethods:     []string{"GET", "POST", "PUT", "DELETE", "OPTIONS"},
		AllowHeaders:     []string{"Origin", "Content-Type", "Authorization"},
		ExposeHeaders:    []string{"Content-Length"},
		AllowCredentials: true,
	}))
	r.Use(middleware.Logger())
	r.Use(middleware.Recovery())

	// 创建处理器实例
	userHandler := handler.NewUserHandler()
	inspectionHandler := handler.NewInspectionHandler()

	// API路由组
	api := r.Group("/api")
	{
		// 认证相关路由
		auth := api.Group("/auth")
		{
			auth.POST("/login", userHandler.Login)
			auth.POST("/logout", middleware.AuthRequired(), userHandler.Logout)
		}

		// 用户相关路由
		users := api.Group("/users")
		users.Use(middleware.AuthRequired())
		{
			users.GET("/profile", userHandler.GetUserProfile)
			users.PUT("/profile", userHandler.UpdateUserProfile)
			users.GET("/statistics", userHandler.GetUserStatistics)
		}

		// 巡检点相关接口
		inspectionPoints := api.Group("/inspection-points")
		{
			inspectionPoints.GET("/:code", inspectionHandler.GetInspectionPoint)
		}

		// 巡检记录相关接口
		records := api.Group("/records")
		records.Use(middleware.AuthRequired())
		{
			records.POST("", inspectionHandler.CreateRecord)
			records.GET("", inspectionHandler.GetRecords)
			records.GET("/:id", inspectionHandler.GetRecordDetail)
			records.PUT("/:id", inspectionHandler.UpdateRecord)
		}
	}

	return r
}
