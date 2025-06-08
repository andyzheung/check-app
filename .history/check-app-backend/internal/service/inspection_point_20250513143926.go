package service

import (
	"check-app-backend/internal/model"
	"check-app-backend/internal/repository"
)

// GetInspectionPointByCode 根据编号获取巡检点信息
func GetInspectionPointByCode(code string) (*model.InspectionPoint, error) {
	var point model.InspectionPoint
	err := repository.DB.Where("code = ?", code).First(&point).Error
	if err != nil {
		return nil, err
	}
	return &point, nil
}

// CreateInspectionPoint 创建巡检点
func CreateInspectionPoint(point *model.InspectionPoint) error {
	return repository.DB.Create(point).Error
}

// UpdateInspectionPoint 更新巡检点
func UpdateInspectionPoint(point *model.InspectionPoint) error {
	return repository.DB.Save(point).Error
}

// DeleteInspectionPoint 删除巡检点
func DeleteInspectionPoint(id uint) error {
	return repository.DB.Delete(&model.InspectionPoint{}, id).Error
}

// GetInspectionPoints 获取巡检点列表
func GetInspectionPoints(page, pageSize int) ([]model.InspectionPoint, int64, error) {
	var points []model.InspectionPoint
	var total int64

	err := repository.DB.Model(&model.InspectionPoint{}).Count(&total).Error
	if err != nil {
		return nil, 0, err
	}

	err = repository.DB.Offset((page - 1) * pageSize).Limit(pageSize).Find(&points).Error
	if err != nil {
		return nil, 0, err
	}

	return points, total, nil
}
