package repository

import (
	"inspection-app-backend/model"
	"inspection-app-backend/pkg/database"
)

type AreaRepository struct{}

func NewAreaRepository() *AreaRepository {
	return &AreaRepository{}
}

// Create 创建区域
func (r *AreaRepository) Create(area *model.Area) error {
	return database.DB.Create(area).Error
}

// GetByID 根据ID获取区域
func (r *AreaRepository) GetByID(id uint) (*model.Area, error) {
	var area model.Area
	err := database.DB.First(&area, id).Error
	return &area, err
}

// GetByCode 根据编码获取区域
func (r *AreaRepository) GetByCode(code string) (*model.Area, error) {
	var area model.Area
	err := database.DB.Where("code = ?", code).First(&area).Error
	return &area, err
}

// Update 更新区域
func (r *AreaRepository) Update(area *model.Area) error {
	return database.DB.Save(area).Error
}

// Delete 删除区域
func (r *AreaRepository) Delete(id uint) error {
	return database.DB.Delete(&model.Area{}, id).Error
}

// List 获取区域列表
func (r *AreaRepository) List(page, pageSize int) ([]model.Area, int64, error) {
	var areas []model.Area
	var total int64

	err := database.DB.Model(&model.Area{}).Count(&total).Error
	if err != nil {
		return nil, 0, err
	}

	err = database.DB.Where("is_active = ?", true).
		Offset((page - 1) * pageSize).Limit(pageSize).
		Find(&areas).Error
	return areas, total, err
}

// ListAll 获取所有区域（不分页）
func (r *AreaRepository) ListAll() ([]model.Area, error) {
	var areas []model.Area
	err := database.DB.Where("is_active = ?", true).Find(&areas).Error
	return areas, err
}
