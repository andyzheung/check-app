package repository

import (
	"inspection-app-backend/model"
	"inspection-app-backend/pkg/database"
)

type UserRepository struct{}

func NewUserRepository() *UserRepository {
	return &UserRepository{}
}

// Create 创建用户
func (r *UserRepository) Create(user *model.User) error {
	return database.DB.Create(user).Error
}

// GetByID 根据ID获取用户
func (r *UserRepository) GetByID(id uint) (*model.User, error) {
	var user model.User
	err := database.DB.First(&user, id).Error
	return &user, err
}

// GetByUsername 根据用户名获取用户
func (r *UserRepository) GetByUsername(username string) (*model.User, error) {
	var user model.User
	err := database.DB.Where("username = ?", username).First(&user).Error
	return &user, err
}

// Update 更新用户
func (r *UserRepository) Update(user *model.User) error {
	return database.DB.Save(user).Error
}

// Delete 删除用户
func (r *UserRepository) Delete(id uint) error {
	return database.DB.Delete(&model.User{}, id).Error
}

// List 获取用户列表
func (r *UserRepository) List(page, pageSize int) ([]model.User, int64, error) {
	var users []model.User
	var total int64

	err := database.DB.Model(&model.User{}).Count(&total).Error
	if err != nil {
		return nil, 0, err
	}

	err = database.DB.Offset((page - 1) * pageSize).Limit(pageSize).Find(&users).Error
	return users, total, err
}
