package service

import (
	"check-app-backend/internal/model"
	"check-app-backend/internal/repository"
	"strconv"
	"time"
)

// CreateRecord 创建巡检记录
func CreateRecord(record *model.InspectionRecord) error {
	record.InspectionTime = time.Now()
	return repository.DB.Create(record).Error
}

// GetRecords 获取巡检记录列表
func GetRecords(startTime, endTime, status, page, pageSize string) ([]model.InspectionRecord, int64, error) {
	var records []model.InspectionRecord
	var total int64
	query := repository.DB.Model(&model.InspectionRecord{})

	// 添加时间范围条件
	if startTime != "" && endTime != "" {
		query = query.Where("inspection_time BETWEEN ? AND ?", startTime, endTime)
	}

	// 添加状态条件
	if status != "" {
		statusInt, _ := strconv.Atoi(status)
		query = query.Where("status = ?", statusInt)
	}

	// 获取总数
	err := query.Count(&total).Error
	if err != nil {
		return nil, 0, err
	}

	// 分页查询
	pageInt, _ := strconv.Atoi(page)
	pageSizeInt, _ := strconv.Atoi(pageSize)
	err = query.Preload("Point").Preload("Inspector").
		Offset((pageInt - 1) * pageSizeInt).
		Limit(pageSizeInt).
		Order("inspection_time DESC").
		Find(&records).Error

	if err != nil {
		return nil, 0, err
	}

	return records, total, nil
}

// GetRecordByID 获取巡检记录详情
func GetRecordByID(id string) (*model.InspectionRecord, error) {
	var record model.InspectionRecord
	err := repository.DB.Preload("Point").Preload("Inspector").
		First(&record, id).Error
	if err != nil {
		return nil, err
	}
	return &record, nil
}

// UpdateRecord 更新巡检记录
func UpdateRecord(id string, record *model.InspectionRecord) error {
	return repository.DB.Model(&model.InspectionRecord{}).
		Where("id = ?", id).
		Updates(record).Error
}
