package handler

import (
	"net/http"
	"strconv"

	"inspection-app-backend/service"

	"github.com/gin-gonic/gin"
)

type AreaHandler struct {
	areaService *service.AreaService
}

func NewAreaHandler() *AreaHandler {
	return &AreaHandler{
		areaService: service.NewAreaService(),
	}
}

// GetAreaByQRCode 根据二维码获取区域信息
func (h *AreaHandler) GetAreaByQRCode(c *gin.Context) {
	qrCode := c.Query("qr_code")
	if qrCode == "" {
		c.JSON(http.StatusBadRequest, gin.H{"error": "二维码不能为空"})
		return
	}

	area, err := h.areaService.GetByQRCode(qrCode)
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "区域不存在"})
		return
	}

	c.JSON(http.StatusOK, area)
}

// GetAreaList 获取区域列表
func (h *AreaHandler) GetAreaList(c *gin.Context) {
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("page_size", "10"))

	areas, total, err := h.areaService.List(page, pageSize)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"total": total,
		"data":  areas,
	})
}

// GetAreaDetail 获取区域详情
func (h *AreaHandler) GetAreaDetail(c *gin.Context) {
	id, err := strconv.ParseUint(c.Param("id"), 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的ID"})
		return
	}

	area, err := h.areaService.GetByID(uint(id))
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "区域不存在"})
		return
	}

	c.JSON(http.StatusOK, area)
}
