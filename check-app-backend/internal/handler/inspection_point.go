package handler

import (
	"check-app-backend/internal/service"
	"net/http"

	"github.com/gin-gonic/gin"
)

// GetInspectionPoint 获取巡检点信息
func GetInspectionPoint(c *gin.Context) {
	code := c.Param("code")

	point, err := service.GetInspectionPointByCode(code)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "获取巡检点信息失败",
		})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"data": point,
	})
}
