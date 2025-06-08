package model

import (
	"time"

	"gorm.io/gorm"
)

// InspectionRecord 巡检记录模型
type InspectionRecord struct {
	ID             uint            `gorm:"primarykey" json:"id"`
	PointID        uint            `json:"point_id"`                                // 巡检点ID
	Point          InspectionPoint `gorm:"foreignKey:PointID" json:"point"`         // 巡检点信息
	InspectorID    uint            `json:"inspector_id"`                            // 巡检员ID
	Inspector      User            `gorm:"foreignKey:InspectorID" json:"inspector"` // 巡检员信息
	Status         int             `gorm:"default:0" json:"status"`                 // 状态：0-待处理，1-正常，2-异常
	AbnormalDesc   string          `gorm:"size:500" json:"abnormal_desc"`           // 异常描述
	Images         string          `gorm:"size:1000" json:"images"`                 // 图片URL，多个用逗号分隔
	Remarks        string          `gorm:"size:500" json:"remarks"`                 // 备注
	InspectionTime time.Time       `json:"inspection_time"`                         // 巡检时间
	CreatedAt      time.Time       `json:"created_at"`
	UpdatedAt      time.Time       `json:"updated_at"`
	DeletedAt      gorm.DeletedAt  `gorm:"index" json:"-"`
}

// User 用户模型
type User struct {
	ID        uint           `gorm:"primarykey" json:"id"`
	Username  string         `gorm:"uniqueIndex;size:50" json:"username"` // 用户名
	Name      string         `gorm:"size:50" json:"name"`                 // 姓名
	Phone     string         `gorm:"size:20" json:"phone"`                // 手机号
	Email     string         `gorm:"size:100" json:"email"`               // 邮箱
	Status    int            `gorm:"default:1" json:"status"`             // 状态：1-正常，0-停用
	CreatedAt time.Time      `json:"created_at"`
	UpdatedAt time.Time      `json:"updated_at"`
	DeletedAt gorm.DeletedAt `gorm:"index" json:"-"`
}
