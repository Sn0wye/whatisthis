package controllers

import (
	"encoding/json"
	"fmt"
	"time"

	"whatisthis/pkg/exceptions"
	"whatisthis/pkg/jwt"
	"whatisthis/pkg/messaging"
	"whatisthis/src/dto"
	"whatisthis/src/models"
	"whatisthis/src/utils"

	"github.com/gofiber/fiber/v2"
	"github.com/pkg/errors"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/gorm"
)

type AuthController interface {
	Profile(ctx *fiber.Ctx) error
	Register(ctx *fiber.Ctx) error
	Login(ctx *fiber.Ctx) error
	GenerateToken(userId string) (string, error)
}

type authController struct {
	db  *gorm.DB
	jwt *jwt.JWT
	rmq *messaging.MessagingService
}

func NewAuthController(db *gorm.DB, jwt *jwt.JWT, rmq *messaging.MessagingService) AuthController {
	return &authController{
		db:  db,
		jwt: jwt,
		rmq: rmq,
	}
}

func (s *authController) Profile(c *fiber.Ctx) error {
	claims := c.Locals("claims").(*jwt.Claims)
	user := models.User{}

	result := s.db.Where("id = ?", claims.Subject).First(&user)
	if result.Error != nil {
		if errors.Is(result.Error, gorm.ErrRecordNotFound) {
			return exceptions.Unauthorized(c)
		}
		return exceptions.InternalServerError(c, "failed to fetch user")
	}

	return c.Status(fiber.StatusOK).JSON(user)
}

func (s *authController) Register(c *fiber.Ctx) error {
	db := s.db
	body := new(dto.RegisterRequest)
	if err := utils.ParseRequest(c, body); err != nil {
		return err
	}

	var user models.User
	exists := db.Where("email = ?", body.Email).First(&user).RowsAffected
	if exists > 0 {
		return exceptions.UnprocessableEntity(c, "Email already taken")
	}

	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(body.Password), bcrypt.DefaultCost)
	if err != nil {
		return exceptions.InternalServerError(c, "failed to hash password")
	}

	user = models.User{
		Username:     body.Username,
		Password:     string(hashedPassword),
		Email:        body.Email,
		Name:         body.Name,
		AnnualIncome: body.AnnualIncome,
		Debt:         body.Debt,
		AssetsValue:  body.AssetsValue,
	}

	db.Create(&user)

	data := map[string]interface{}{
		"userId":      user.ID.String(),
		"income":      user.AnnualIncome,
		"debt":        user.Debt,
		"assetsValue": user.AssetsValue,
	}

	jsonData, err := json.Marshal(data)

	if err != nil {
		return exceptions.InternalServerError(c, "failed to marshal data")
	}

	s.rmq.Produce("calculate-score", string(jsonData))

	token, err := s.GenerateToken(user.ID.String())
	if err != nil {
		return exceptions.InternalServerError(c, "failed to generate JWT token")
	}

	return c.Status(fiber.StatusOK).JSON(fiber.Map{
		"token": token,
	})
}

func (s *authController) Login(c *fiber.Ctx) error {
	db := s.db

	body := new(dto.LoginRequest)

	if err := utils.ParseRequest(c, body); err != nil {
		fmt.Println(err)

		return err
	}

	var user models.User
	db.Where("email = ?", body.Email).First(&user)

	if err := bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(body.Password)); err != nil {
		return exceptions.Unauthorized(c)
	}

	token, err := s.GenerateToken(user.ID.String())
	if err != nil {
		return exceptions.InternalServerError(c, "failed to generate JWT token")
	}

	return c.Status(fiber.StatusOK).JSON(fiber.Map{
		"token": token,
	})
}

func (s *authController) GenerateToken(userId string) (string, error) {
	token, err := s.jwt.GenToken(userId, time.Now().Add(time.Hour*24*90)) // 90 days
	if err != nil {
		return "", errors.Wrap(err, "failed to generate JWT token")
	}

	return token, nil
}