package service

import (
	"errors"
	"time"

	"check-app-backend/model"
	"check-app-backend/repository"
)

type InspectionService struct {
	inspectionRepo *repository.InspectionRepository
	areaRepo       *repository.AreaRepository
}

func NewInspectionService() *InspectionService {
	return &InspectionService{
		inspectionRepo: repository.NewInspectionRepository(),
		areaRepo:       repository.NewAreaRepository(),
	}
}

// CreateInspection 创建巡检记录
func (s *InspectionService) CreateInspection(inspection *model.Inspection) error {
	// 验证区域是否存在
	area, err := s.areaRepo.GetByID(inspection.AreaID)
	if err != nil {
		return errors.New("巡检区域不存在")
	}
	if !area.IsActive {
		return errors.New("巡检区域已停用")
	}

	// 设置巡检时间
	inspection.InspectTime = time.Now()

	// 根据巡检项目状态设置整体状态
	inspection.Status = s.determineOverallStatus(inspection.Items)

	return s.inspectionRepo.Create(inspection)
}

// GetInspection 获取巡检记录
func (s *InspectionService) GetInspection(id uint) (*model.Inspection, error) {
	return s.inspectionRepo.GetByID(id)
}

// UpdateInspection 更新巡检记录
func (s *InspectionService) UpdateInspection(id uint, inspection *model.Inspection) error {
	existing, err := s.inspectionRepo.GetByID(id)
	if err != nil {
		return errors.New("巡检记录不存在")
	}

	// 只允许更新特定字段
	existing.Status = s.determineOverallStatus(inspection.Items)
	existing.Remarks = inspection.Remarks
	existing.Items = inspection.Items

	return s.inspectionRepo.Update(existing)
}

// DeleteInspection 删除巡检记录
func (s *InspectionService) DeleteInspection(id uint) error {
	return s.inspectionRepo.Delete(id)
}

// ListInspections 获取巡检记录列表
func (s *InspectionService) ListInspections(page, pageSize int, userID uint, isAdmin bool) ([]model.Inspection, int64, error) {
	return s.inspectionRepo.List(page, pageSize)
}

// GetInspectionsByArea 获取指定区域的巡检记录
func (s *InspectionService) GetInspectionsByArea(areaID uint, startTime, endTime time.Time) ([]model.Inspection, error) {
	return s.inspectionRepo.GetByArea(areaID, startTime, endTime)
}

// GetInspectionsByUser 获取指定用户的巡检记录
func (s *InspectionService) GetInspectionsByUser(userID uint, startTime, endTime time.Time) ([]model.Inspection, error) {
	return s.inspectionRepo.GetByUser(userID, startTime, endTime)
}

// determineOverallStatus 根据巡检项目状态确定整体状态
func (s *InspectionService) determineOverallStatus(items []model.InspectionItem) string {
	for _, item := range items {
		if item.Status == "abnormal" {
			return "abnormal"
		}
	}
	return "normal"
}
