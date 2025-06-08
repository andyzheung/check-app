package model

import (
	"time"

	"gorm.io/gorm"
)

// InspectionRecord 巡检记录模型
type InspectionRecord struct {
	ID        uint           `gorm:"primarykey" json:"id"`
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `gorm:"index" json:"-"`

	UserID            uint   `gorm:"not null" json:"user_id"`             // 用户ID
	InspectionPointID uint   `gorm:"not null" json:"inspection_point_id"` // 巡检点ID
	Status            string `gorm:"size:20;not null" json:"status"`      // 状态：pending/completed
	Result            string `gorm:"size:20" json:"result"`               // 结果：normal/abnormal
	Remark            string `gorm:"size:500" json:"remark"`              // 备注
	Images            string `gorm:"size:1000" json:"images"`             // 图片URL，多个用逗号分隔
}

// TableName 指定表名
func (InspectionRecord) TableName() string {
	return "inspection_records"
}
