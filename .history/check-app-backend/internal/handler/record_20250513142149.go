package handler

import (
	"check-app-backend/internal/model"
	"check-app-backend/internal/service"
	"net/http"

	"github.com/gin-gonic/gin"
)

// CreateRecord 创建巡检记录
func CreateRecord(c *gin.Context) {
	var record model.InspectionRecord
	if err := c.ShouldBindJSON(&record); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "无效的请求数据",
		})
		return
	}

	if err := service.CreateRecord(&record); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "创建巡检记录失败",
		})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"data": record,
	})
}

// GetRecords 获取巡检记录列表
func GetRecords(c *gin.Context) {
	// 获取查询参数
	startTime := c.Query("start_time")
	endTime := c.Query("end_time")
	status := c.Query("status")
	page := c.DefaultQuery("page", "1")
	pageSize := c.DefaultQuery("page_size", "10")

	records, total, err := service.GetRecords(startTime, endTime, status, page, pageSize)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "获取巡检记录失败",
		})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"data":  records,
		"total": total,
	})
}

// GetRecordDetail 获取巡检记录详情
func GetRecordDetail(c *gin.Context) {
	id := c.Param("id")

	record, err := service.GetRecordByID(id)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "获取巡检记录详情失败",
		})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"data": record,
	})
}

// UpdateRecord 更新巡检记录
func UpdateRecord(c *gin.Context) {
	id := c.Param("id")
	var record model.InspectionRecord
	if err := c.ShouldBindJSON(&record); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "无效的请求数据",
		})
		return
	}

	if err := service.UpdateRecord(id, &record); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "更新巡检记录失败",
		})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"data": record,
	})
}
