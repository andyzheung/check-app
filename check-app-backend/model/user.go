package model

import (
	"time"

	"gorm.io/gorm"
)

// User 用户模型
type User struct {
	ID        uint           `gorm:"primarykey" json:"id"`
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `gorm:"index" json:"-"`

	Username    string `gorm:"size:50;not null;unique" json:"username"`     // AD账号
	DisplayName string `gorm:"size:50;not null" json:"display_name"`        // 显示名称
	Email       string `gorm:"size:100" json:"email"`                       // 邮箱
	Department  string `gorm:"size:50" json:"department"`                   // 部门
	Role        string `gorm:"size:20;not null;default:'user'" json:"role"` // 角色：admin/user
	IsActive    bool   `gorm:"default:true" json:"is_active"`               // 是否激活
}

// TableName 指定表名
func (User) TableName() string {
	return "users"
}
