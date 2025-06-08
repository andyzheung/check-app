package service

import (
	"errors"
	"inspection-app-backend/model"
	"inspection-app-backend/pkg/utils"
	"inspection-app-backend/repository"
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

// UpdateUserProfile 更新用户信息
func (s *UserService) UpdateUserProfile(userID uint, profile *model.User) error {
	// 验证用户是否存在
	existingUser, err := s.userRepo.GetByID(userID)
	if err != nil {
		return errors.New("用户不存在")
	}

	// 只允许更新特定字段
	existingUser.DisplayName = profile.DisplayName
	existingUser.Email = profile.Email
	existingUser.Department = profile.Department

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
