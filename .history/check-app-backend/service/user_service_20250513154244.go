package service

import (
	"errors"

	"check-app-backend/model"
	"check-app-backend/pkg/utils"
	"check-app-backend/repository"
)

type UserService struct {
	userRepo *repository.UserRepository
}

func NewUserService() *UserService {
	return &UserService{
		userRepo: repository.NewUserRepository(),
	}
}

// Login 用户登录
func (s *UserService) Login(username, password string) (string, error) {
	user, err := s.userRepo.GetByUsername(username)
	if err != nil {
		return "", errors.New("用户不存在")
	}

	// TODO: 实现AD认证
	// 这里暂时使用模拟的AD认证
	if !s.mockADAuth(username, password) {
		return "", errors.New("用户名或密码错误")
	}

	// 生成JWT token
	token, err := utils.GenerateToken(user.ID, user.Username, user.Role)
	if err != nil {
		return "", err
	}

	return token, nil
}

// GetUserProfile 获取用户信息
func (s *UserService) GetUserProfile(userID uint) (*model.User, error) {
	return s.userRepo.GetByID(userID)
}

// UpdateUser 更新用户信息
func (s *UserService) UpdateUser(user *model.User) error {
	// 验证用户是否存在
	existingUser, err := s.userRepo.GetByID(user.ID)
	if err != nil {
		return errors.New("用户不存在")
	}

	// 只允许更新特定字段
	existingUser.DisplayName = user.DisplayName
	existingUser.Email = user.Email
	existingUser.Department = user.Department

	return s.userRepo.Update(existingUser)
}

// ListUsers 获取用户列表（仅管理员）
func (s *UserService) ListUsers(page, pageSize int) ([]model.User, int64, error) {
	return s.userRepo.List(page, pageSize)
}

// mockADAuth 模拟AD认证
func (s *UserService) mockADAuth(username, password string) bool {
	// TODO: 实现真实的AD认证
	return true
}

// Authenticate 用户认证，返回用户信息
func (s *UserService) Authenticate(username, password string) (*model.User, error) {
	user, err := s.userRepo.GetByUsername(username)
	if err != nil {
		return nil, errors.New("用户不存在")
	}
	if !s.mockADAuth(username, password) {
		return nil, errors.New("用户名或密码错误")
	}
	return user, nil
}

// GetUserByID 根据ID获取用户
func (s *UserService) GetUserByID(userID uint) (*model.User, error) {
	return s.userRepo.GetByID(userID)
}

// GetUserStatistics 获取用户统计信息
func (s *UserService) GetUserStatistics(userID uint) (map[string]interface{}, error) {
	// 获取用户信息
	user, err := s.userRepo.GetByID(userID)
	if err != nil {
		return nil, err
	}

	// 获取用户的巡检记录
	records, err := s.userRepo.GetUserRecords(userID)
	if err != nil {
		return nil, err
	}

	// 计算统计数据
	totalRecords := len(records)
	completedRecords := 0
	for _, record := range records {
		if record.Status == "completed" || record.Status == "normal" {
			completedRecords++
		}
	}

	completionRate := 0.0
	if totalRecords > 0 {
		completionRate = float64(completedRecords) / float64(totalRecords) * 100
	}

	return map[string]interface{}{
		"user": user,
		"statistics": map[string]interface{}{
			"total_records":     totalRecords,
			"completed_records": completedRecords,
			"completion_rate":   completionRate,
		},
	}, nil
}
