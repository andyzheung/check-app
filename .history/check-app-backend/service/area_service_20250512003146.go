package service

import (
	"errors"
	"inspection-app-backend/model"
	"inspection-app-backend/repository"
)

type AreaService struct {
	areaRepo *repository.AreaRepository
}

func NewAreaService() *AreaService {
	return &AreaService{
		areaRepo: repository.NewAreaRepository(),
	}
}

// GetByQRCode 根据二维码获取区域信息
func (s *AreaService) GetByQRCode(qrCode string) (*model.Area, error) {
	return s.areaRepo.GetByQRCode(qrCode)
}

// GetByID 根据ID获取区域信息
func (s *AreaService) GetByID(id uint) (*model.Area, error) {
	return s.areaRepo.GetByID(id)
}

// List 获取区域列表
func (s *AreaService) List(page, pageSize int) ([]model.Area, int64, error) {
	return s.areaRepo.List(page, pageSize)
}

// Create 创建区域
func (s *AreaService) Create(area *model.Area) error {
	// 验证区域编码是否已存在
	existing, err := s.areaRepo.GetByCode(area.Code)
	if err == nil && existing != nil {
		return errors.New("区域编码已存在")
	}

	return s.areaRepo.Create(area)
}

// Update 更新区域
func (s *AreaService) Update(area *model.Area) error {
	// 验证区域是否存在
	existing, err := s.areaRepo.GetByID(area.ID)
	if err != nil {
		return errors.New("区域不存在")
	}

	// 如果修改了编码，需要验证新编码是否已存在
	if existing.Code != area.Code {
		codeExists, err := s.areaRepo.GetByCode(area.Code)
		if err == nil && codeExists != nil {
			return errors.New("区域编码已存在")
		}
	}

	return s.areaRepo.Update(area)
}

// Delete 删除区域
func (s *AreaService) Delete(id uint) error {
	return s.areaRepo.Delete(id)
}
