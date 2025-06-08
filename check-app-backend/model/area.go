package model

import (
	"time"

	"gorm.io/gorm"
)

// Area 巡检区域模型
type Area struct {
	ID        uint           `gorm:"primarykey" json:"id"`
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `gorm:"index" json:"-"`

	Code        string `gorm:"size:50;not null;unique" json:"code"` // 区域编码
	Name        string `gorm:"size:100;not null" json:"name"`       // 区域名称
	Description string `gorm:"size:500" json:"description"`         // 区域描述
	Location    string `gorm:"size:200" json:"location"`            // 位置信息
	QRCode      string `gorm:"size:200" json:"qr_code"`             // 二维码内容
	IsActive    bool   `gorm:"default:true" json:"is_active"`       // 是否激活
}

// TableName 指定表名
func (Area) TableName() string {
	return "areas"
}
