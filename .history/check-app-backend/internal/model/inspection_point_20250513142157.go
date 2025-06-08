package model

import (
	"time"

	"gorm.io/gorm"
)

// InspectionPoint 巡检点模型
type InspectionPoint struct {
	ID          uint           `gorm:"primarykey" json:"id"`
	Code        string         `gorm:"uniqueIndex;size:50" json:"code"` // 巡检点编号
	Name        string         `gorm:"size:100" json:"name"`            // 巡检点名称
	Location    string         `gorm:"size:200" json:"location"`        // 位置信息
	Description string         `gorm:"size:500" json:"description"`     // 描述信息
	Status      int            `gorm:"default:1" json:"status"`         // 状态：1-正常，0-停用
	CreatedAt   time.Time      `json:"created_at"`
	UpdatedAt   time.Time      `json:"updated_at"`
	DeletedAt   gorm.DeletedAt `gorm:"index" json:"-"`
}
