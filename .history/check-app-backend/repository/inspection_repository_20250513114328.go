package repository

import (
	"check-app-backend/model"
	"check-app-backend/pkg/database"
	"time"

	"gorm.io/gorm"
)

type InspectionRepository struct {
	db *gorm.DB
}

func NewInspectionRepository() *InspectionRepository {
	return &InspectionRepository{
		db: database.GetDB(),
	}
}

// Create 创建巡检记录
func (r *InspectionRepository) Create(inspection *model.Inspection) error {
	return r.db.Create(inspection).Error
}

// GetByID 根据ID获取巡检记录
func (r *InspectionRepository) GetByID(id uint) (*model.Inspection, error) {
	var inspection model.Inspection
	err := r.db.First(&inspection, id).Error
	return &inspection, err
}

// Update 更新巡检记录
func (r *InspectionRepository) Update(inspection *model.Inspection) error {
	return r.db.Save(inspection).Error
}

// Delete 删除巡检记录
func (r *InspectionRepository) Delete(id uint) error {
	return r.db.Delete(&model.Inspection{}, id).Error
}

// List 获取巡检记录列表
func (r *InspectionRepository) List(page, pageSize int) ([]model.Inspection, int64, error) {
	var inspections []model.Inspection
	var total int64

	offset := (page - 1) * pageSize

	err := r.db.Model(&model.Inspection{}).Count(&total).Error
	if err != nil {
		return nil, 0, err
	}

	err = r.db.Offset(offset).Limit(pageSize).Find(&inspections).Error
	if err != nil {
		return nil, 0, err
	}

	return inspections, total, nil
}

// GetByArea 获取指定区域的巡检记录
func (r *InspectionRepository) GetByArea(areaID uint, startTime, endTime time.Time) ([]model.Inspection, error) {
	var inspections []model.Inspection
	err := r.db.Where("area_id = ? AND created_at BETWEEN ? AND ?", areaID, startTime, endTime).
		Find(&inspections).Error
	return inspections, err
}

// GetByUser 获取指定用户的巡检记录
func (r *InspectionRepository) GetByUser(userID uint, startTime, endTime time.Time) ([]model.Inspection, error) {
	var inspections []model.Inspection
	err := r.db.Where("user_id = ? AND created_at BETWEEN ? AND ?", userID, startTime, endTime).
		Find(&inspections).Error
	return inspections, err
}
