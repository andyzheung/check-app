package handler

import (
	"net/http"
	"strconv"
	"time"

	"github.com/gin-gonic/gin"

	"check-app-backend/model"
	"check-app-backend/service"
)

type InspectionHandler struct {
	inspectionService *service.InspectionService
}

func NewInspectionHandler() *InspectionHandler {
	return &InspectionHandler{
		inspectionService: service.NewInspectionService(),
	}
}

// CreateInspection 创建巡检记录
func (h *InspectionHandler) CreateInspection(c *gin.Context) {
	var inspection model.Inspection
	if err := c.ShouldBindJSON(&inspection); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的请求参数"})
		return
	}

	// 设置当前用户ID
	inspection.UserID = c.GetUint("userID")

	if err := h.inspectionService.CreateInspection(&inspection); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, inspection)
}

// GetInspection 获取巡检记录
func (h *InspectionHandler) GetInspection(c *gin.Context) {
	id, err := strconv.ParseUint(c.Param("id"), 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的ID"})
		return
	}

	inspection, err := h.inspectionService.GetInspection(uint(id))
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "巡检记录不存在"})
		return
	}

	c.JSON(http.StatusOK, inspection)
}

// UpdateInspection 更新巡检记录
func (h *InspectionHandler) UpdateInspection(c *gin.Context) {
	id, err := strconv.ParseUint(c.Param("id"), 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的ID"})
		return
	}

	var inspection model.Inspection
	if err := c.ShouldBindJSON(&inspection); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的请求参数"})
		return
	}

	if err := h.inspectionService.UpdateInspection(uint(id), &inspection); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "更新成功"})
}

// DeleteInspection 删除巡检记录
func (h *InspectionHandler) DeleteInspection(c *gin.Context) {
	id, err := strconv.ParseUint(c.Param("id"), 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的ID"})
		return
	}

	if err := h.inspectionService.DeleteInspection(uint(id)); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "删除成功"})
}

// ListInspections 获取巡检记录列表
func (h *InspectionHandler) ListInspections(c *gin.Context) {
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("page_size", "10"))
	userID := c.GetUint("userID")
	isAdmin := c.GetString("role") == "admin"

	inspections, total, err := h.inspectionService.ListInspections(page, pageSize, userID, isAdmin)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"total": total,
		"data":  inspections,
	})
}

// GetInspectionsByArea 获取指定区域的巡检记录
func (h *InspectionHandler) GetInspectionsByArea(c *gin.Context) {
	areaID, err := strconv.ParseUint(c.Param("area_id"), 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的区域ID"})
		return
	}

	startTime, _ := time.Parse("2006-01-02", c.DefaultQuery("start_time", time.Now().AddDate(0, 0, -7).Format("2006-01-02")))
	endTime, _ := time.Parse("2006-01-02", c.DefaultQuery("end_time", time.Now().Format("2006-01-02")))

	records, err := h.inspectionService.GetInspectionsByArea(uint(areaID), startTime, endTime)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, records)
}

// GetInspectionsByUser 获取指定用户的巡检记录
func (h *InspectionHandler) GetInspectionsByUser(c *gin.Context) {
	userID, err := strconv.ParseUint(c.Param("user_id"), 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的用户ID"})
		return
	}

	startTime, _ := time.Parse("2006-01-02", c.DefaultQuery("start_time", time.Now().AddDate(0, 0, -7).Format("2006-01-02")))
	endTime, _ := time.Parse("2006-01-02", c.DefaultQuery("end_time", time.Now().Format("2006-01-02")))

	records, err := h.inspectionService.GetInspectionsByUser(uint(userID), startTime, endTime)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, records)
}

// GetInspectionPoint 获取巡检点信息
func (h *InspectionHandler) GetInspectionPoint(c *gin.Context) {
	code := c.Param("code")
	point, err := h.inspectionService.GetInspectionPointByCode(code)
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "巡检点不存在"})
		return
	}
	c.JSON(http.StatusOK, point)
}

// ScanPoint 扫描巡检点
func (h *InspectionHandler) ScanPoint(c *gin.Context) {
	var req struct {
		Code string `json:"code" binding:"required"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的请求参数"})
		return
	}

	point, err := h.inspectionService.GetInspectionPointByCode(req.Code)
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "巡检点不存在"})
		return
	}

	c.JSON(http.StatusOK, point)
}

// CreateRecord 创建巡检记录
func (h *InspectionHandler) CreateRecord(c *gin.Context) {
	var req struct {
		PointID uint   `json:"point_id" binding:"required"`
		Status  string `json:"status" binding:"required"`
		Result  string `json:"result"`
		Remark  string `json:"remark"`
		Images  string `json:"images"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的请求参数"})
		return
	}

	userID := c.GetUint("userID")
	record := &model.InspectionRecord{
		UserID:            userID,
		InspectionPointID: req.PointID,
		Status:            req.Status,
		Result:            req.Result,
		Remark:            req.Remark,
		Images:            req.Images,
	}

	if err := h.inspectionService.CreateRecord(record); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "创建成功"})
}

// GetRecords 获取巡检记录列表
func (h *InspectionHandler) GetRecords(c *gin.Context) {
	userID := c.GetUint("userID")
	page, _ := strconv.Atoi(c.DefaultQuery("page", "1"))
	pageSize, _ := strconv.Atoi(c.DefaultQuery("page_size", "10"))

	records, total, err := h.inspectionService.GetRecordsByUser(userID, page, pageSize)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"records": records,
		"total":   total,
		"page":    page,
		"size":    pageSize,
	})
}

// GetRecord 获取巡检记录详情
func (h *InspectionHandler) GetRecord(c *gin.Context) {
	id, err := strconv.ParseUint(c.Param("id"), 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的记录ID"})
		return
	}

	record, err := h.inspectionService.GetRecordByID(uint(id))
	if err != nil {
		c.JSON(http.StatusNotFound, gin.H{"error": "记录不存在"})
		return
	}

	c.JSON(http.StatusOK, record)
}

// UpdateRecord 更新巡检记录
func (h *InspectionHandler) UpdateRecord(c *gin.Context) {
	id, err := strconv.ParseUint(c.Param("id"), 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的记录ID"})
		return
	}

	var req struct {
		Status string `json:"status"`
		Result string `json:"result"`
		Remark string `json:"remark"`
		Images string `json:"images"`
	}

	if err := c.ShouldBindJSON(&req); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的请求参数"})
		return
	}

	record := &model.InspectionRecord{
		ID:     uint(id),
		Status: req.Status,
		Result: req.Result,
		Remark: req.Remark,
		Images: req.Images,
	}

	if err := h.inspectionService.UpdateRecord(record); err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, gin.H{"message": "更新成功"})
}

// GetByArea 获取指定区域的巡检记录
func (h *InspectionHandler) GetByArea(c *gin.Context) {
	areaID, err := strconv.ParseUint(c.Param("areaID"), 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的区域ID"})
		return
	}

	records, err := h.inspectionService.GetRecordsByArea(uint(areaID))
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, records)
}

// GetByUser 获取指定用户的巡检记录
func (h *InspectionHandler) GetByUser(c *gin.Context) {
	userID, err := strconv.ParseUint(c.Param("userID"), 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": "无效的用户ID"})
		return
	}

	records, err := h.inspectionService.GetRecordsByUser(uint(userID))
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
		return
	}

	c.JSON(http.StatusOK, records)
}
