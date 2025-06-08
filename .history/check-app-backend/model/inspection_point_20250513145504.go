package model

import (
	"time"

	"gorm.io/gorm"
)

// InspectionPoint 巡检点模型
type InspectionPoint struct {
	ID        uint           `gorm:"primarykey" json:"id"`
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `gorm:"index" json:"-"`

	Code        string `gorm:"size:50;not null;unique" json:"code"`             // 巡检点编码
	Name        string `gorm:"size:100;not null" json:"name"`                   // 巡检点名称
	Description string `gorm:"size:500" json:"description"`                     // 描述
	Location    string `gorm:"size:200" json:"location"`                        // 位置
	AreaID      uint   `gorm:"not null" json:"area_id"`                         // 所属区域ID
	Status      string `gorm:"size:20;not null;default:'active'" json:"status"` // 状态：active/inactive
}

// TableName 指定表名
func (InspectionPoint) TableName() string {
	return "inspection_points"
}
