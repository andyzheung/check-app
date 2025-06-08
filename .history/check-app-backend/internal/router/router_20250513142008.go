package router

import (
	"check-app-backend/internal/handler"
	"check-app-backend/internal/middleware"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
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

	// API路由组
	api := r.Group("/api")
	{
		// 巡检点相关接口
		inspectionPoints := api.Group("/inspection-points")
		{
			inspectionPoints.GET("/:code", handler.GetInspectionPoint) // 获取巡检点信息
		}

		// 巡检记录相关接口
		records := api.Group("/records")
		{
			records.POST("", handler.CreateRecord)       // 创建巡检记录
			records.GET("", handler.GetRecords)          // 获取巡检记录列表
			records.GET("/:id", handler.GetRecordDetail) // 获取巡检记录详情
			records.PUT("/:id", handler.UpdateRecord)    // 更新巡检记录
		}

		// 用户相关接口
		users := api.Group("/users")
		{
			users.GET("/profile", handler.GetUserProfile)       // 获取用户信息
			users.PUT("/profile", handler.UpdateUserProfile)    // 更新用户信息
			users.GET("/statistics", handler.GetUserStatistics) // 获取用户统计信息
		}
	}

	return r
}
