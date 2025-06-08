package repository

import (
	"gorm.io/gorm"

	"check-app-backend/model"
	"check-app-backend/pkg/database"
)

type AreaRepository struct {
	db *gorm.DB
}

func NewAreaRepository() *AreaRepository {
	return &AreaRepository{
		db: database.GetDB(),
	}
}

// Create 创建区域
func (r *AreaRepository) Create(area *model.Area) error {
	return r.db.Create(area).Error
}

// GetByID 根据ID获取区域
func (r *AreaRepository) GetByID(id uint) (*model.Area, error) {
	var area model.Area
	err := r.db.First(&area, id).Error
	return &area, err
}

// GetByCode 根据编码获取区域
func (r *AreaRepository) GetByCode(code string) (*model.Area, error) {
	var area model.Area
	err := r.db.Where("code = ?", code).First(&area).Error
	return &area, err
}

// Update 更新区域
func (r *AreaRepository) Update(area *model.Area) error {
	return r.db.Save(area).Error
}

// Delete 删除区域
func (r *AreaRepository) Delete(id uint) error {
	return r.db.Delete(&model.Area{}, id).Error
}

// List 获取区域列表
func (r *AreaRepository) List(page, pageSize int) ([]model.Area, int64, error) {
	var areas []model.Area
	var total int64

	offset := (page - 1) * pageSize

	err := r.db.Model(&model.Area{}).Count(&total).Error
	if err != nil {
		return nil, 0, err
	}

	err = r.db.Offset(offset).Limit(pageSize).Find(&areas).Error
	if err != nil {
		return nil, 0, err
	}

	return areas, total, nil
}

// ListAll 获取所有区域（不分页）
func (r *AreaRepository) ListAll() ([]model.Area, error) {
	var areas []model.Area
	err := r.db.Where("is_active = ?", true).Find(&areas).Error
	return areas, err
}

// GetByQRCode 根据二维码获取区域信息
func (r *AreaRepository) GetByQRCode(qrCode string) (*model.Area, error) {
	var area model.Area
	err := r.db.Where("qr_code = ?", qrCode).First(&area).Error
	if err != nil {
		return nil, err
	}
	return &area, nil
}
