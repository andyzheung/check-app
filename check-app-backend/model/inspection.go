package model

import (
	"time"

	"gorm.io/gorm"
)

// Inspection 巡检记录模型
type Inspection struct {
	ID        uint           `gorm:"primarykey" json:"id"`
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `gorm:"index" json:"-"`

	AreaID      uint             `gorm:"not null" json:"area_id"`              // 巡检区域ID
	Area        Area             `gorm:"foreignKey:AreaID" json:"area"`        // 巡检区域
	UserID      uint             `gorm:"not null" json:"user_id"`              // 巡检人ID
	User        User             `gorm:"foreignKey:UserID" json:"user"`        // 巡检人
	InspectTime time.Time        `gorm:"not null" json:"inspect_time"`         // 巡检时间
	Status      string           `gorm:"size:20;not null" json:"status"`       // 状态：normal/abnormal
	Remarks     string           `gorm:"size:500" json:"remarks"`              // 备注
	Items       []InspectionItem `gorm:"foreignKey:InspectionID" json:"items"` // 巡检项目
}

// InspectionItem 巡检项目模型
type InspectionItem struct {
	ID        uint           `gorm:"primarykey" json:"id"`
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `gorm:"index" json:"-"`

	InspectionID uint   `gorm:"not null" json:"inspection_id"`    // 巡检记录ID
	Category     string `gorm:"size:50;not null" json:"category"` // 类别：environment/safety
	Name         string `gorm:"size:100;not null" json:"name"`    // 项目名称
	Status       string `gorm:"size:20;not null" json:"status"`   // 状态：normal/abnormal
	Value        string `gorm:"size:100" json:"value"`            // 检查值
	Remarks      string `gorm:"size:500" json:"remarks"`          // 备注
}

// TableName 指定表名
func (Inspection) TableName() string {
	return "inspections"
}

func (InspectionItem) TableName() string {
	return "inspection_items"
}
