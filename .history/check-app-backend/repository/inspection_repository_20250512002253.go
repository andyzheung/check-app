package repository

import (
	"inspection-app-backend/model"
	"inspection-app-backend/pkg/database"
	"time"
)

type InspectionRepository struct{}

func NewInspectionRepository() *InspectionRepository {
	return &InspectionRepository{}
}

// Create 创建巡检记录
func (r *InspectionRepository) Create(inspection *model.Inspection) error {
	return database.DB.Create(inspection).Error
}

// GetByID 根据ID获取巡检记录
func (r *InspectionRepository) GetByID(id uint) (*model.Inspection, error) {
	var inspection model.Inspection
	err := database.DB.Preload("Area").Preload("User").Preload("Items").First(&inspection, id).Error
	return &inspection, err
}

// Update 更新巡检记录
func (r *InspectionRepository) Update(inspection *model.Inspection) error {
	return database.DB.Save(inspection).Error
}

// Delete 删除巡检记录
func (r *InspectionRepository) Delete(id uint) error {
	return database.DB.Delete(&model.Inspection{}, id).Error
}

// List 获取巡检记录列表
func (r *InspectionRepository) List(page, pageSize int, userID uint, isAdmin bool) ([]model.Inspection, int64, error) {
	var inspections []model.Inspection
	var total int64
	query := database.DB.Model(&model.Inspection{})

	// 如果不是管理员，只能查看自己的记录
	if !isAdmin {
		query = query.Where("user_id = ?", userID)
	}

	err := query.Count(&total).Error
	if err != nil {
		return nil, 0, err
	}

	err = query.Preload("Area").Preload("User").Preload("Items").
		Offset((page - 1) * pageSize).Limit(pageSize).
		Order("inspect_time DESC").
		Find(&inspections).Error
	return inspections, total, err
}

// GetByAreaID 获取指定区域的巡检记录
func (r *InspectionRepository) GetByAreaID(areaID uint, startTime, endTime time.Time) ([]model.Inspection, error) {
	var inspections []model.Inspection
	err := database.DB.Where("area_id = ? AND inspect_time BETWEEN ? AND ?", areaID, startTime, endTime).
		Preload("User").Preload("Items").
		Order("inspect_time DESC").
		Find(&inspections).Error
	return inspections, err
}

// GetByUserID 获取指定用户的巡检记录
func (r *InspectionRepository) GetByUserID(userID uint, startTime, endTime time.Time) ([]model.Inspection, error) {
	var inspections []model.Inspection
	err := database.DB.Where("user_id = ? AND inspect_time BETWEEN ? AND ?", userID, startTime, endTime).
		Preload("Area").Preload("Items").
		Order("inspect_time DESC").
		Find(&inspections).Error
	return inspections, err
}
