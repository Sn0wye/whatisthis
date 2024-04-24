package models

import (
	"time"
	"whatisthis/pb"

	"github.com/google/uuid"
	"gorm.io/gorm"
)

type User struct {
	ID        uuid.UUID `gorm:"type:char(36); primaryKey" json:"id"`
	Name      string    `gorm:"type:varchar(255); not null" json:"name"`
	Username  string    `gorm:"type:varchar(255); not null" json:"username"`
	Email     string    `gorm:"type:varchar(255); unique; not null" json:"email"`
	Password  string    `gorm:"type:varchar(255); not null" json:"-"`
	CreatedAt time.Time `gorm:"autoCreateTime" json:"-"`
	UpdatedAt time.Time `gorm:"autoCreateTime" json:"-"`
}

type JSONUser struct {
	ID       uuid.UUID `json:"id"`
	Name     string    `json:"name"`
	Username string    `json:"username"`
	Email    string    `json:"email"`
}

func (u *User) BeforeCreate(tx *gorm.DB) error {
	u.ID = uuid.New()

	return nil
}

func (u *User) ToGRPC() *pb.User {
	return &pb.User{
		Id:        u.ID.String(),
		Name:      u.Name,
		Username:  u.Username,
		Email:     u.Email,
		Password:  u.Password,
		CreatedAt: u.CreatedAt.Format(time.RFC3339),
		UpdatedAt: u.CreatedAt.Format(time.RFC3339),
	}
}
