package controllers

import (
	"context"
	"time"

	"whatisthis/pkg/exceptions"
	"whatisthis/pkg/jwt"
	"whatisthis/src/models"

	"github.com/gofiber/fiber/v2"
	"github.com/pkg/errors"
	"golang.org/x/crypto/bcrypt"
	"gorm.io/gorm"
)

type AuthController interface {
	Profile(ctx *fiber.Ctx) error
	Register(ctx *fiber.Ctx) error
	Login(ctx *fiber.Ctx) error
	GenerateToken(ctx context.Context, userId string) (string, error)
}

type authController struct {
	db  *gorm.DB
	jwt *jwt.JWT
}

func NewAuthController(db *gorm.DB, jwt *jwt.JWT) AuthController {
	return &authController{db: db, jwt: jwt}
}

func (s *authController) Profile(c *fiber.Ctx) error {
	claims := c.Locals("claims").(*jwt.Claims)
	user := models.User{}
	s.db.Where("id = ?", claims.Subject).First(&user)

	return c.Status(fiber.StatusOK).JSON(user)
}

type RegisterRequest struct {
	Name     string `json:"name" validate:"required"`
	Username string `json:"username" validate:"required"`
	Password string `json:"password" validate:"required"`
	Email    string `json:"email" validate:"required,email"`
}

func (s *authController) Register(c *fiber.Ctx) error {
	db := s.db
	body := new(RegisterRequest)
	if err := c.BodyParser(body); err != nil {
		return exceptions.UnprocessableEntity(c, "Invalid JSON provided")
	}

	var user models.User
	exists := db.Where("email = ?", body.Email).First(&user).RowsAffected
	if exists > 0 {
		return exceptions.BadRequest(c, "Email already taken")
	}

	hashedPassword, err := bcrypt.GenerateFromPassword([]byte(body.Password), bcrypt.DefaultCost)
	if err != nil {
		return exceptions.InternalServerError(c, "failed to hash password")
	}

	user = models.User{
		Username: body.Username,
		Password: string(hashedPassword),
		Email:    body.Email,
		Name:     body.Name,
	}

	db.Create(&user)

	token, err := s.GenerateToken(context.Background(), user.ID.String())
	if err != nil {
		return exceptions.InternalServerError(c, "failed to generate JWT token")
	}

	return c.Status(fiber.StatusOK).JSON(fiber.Map{
		"token": token,
	})
}

type LoginRequest struct {
	Email    string `json:"email" validate:"required"`
	Password string `json:"password" validate:"required"`
}

func (s *authController) Login(c *fiber.Ctx) error {
	db := s.db
	body := new(LoginRequest)
	if err := c.BodyParser(body); err != nil {
		return exceptions.UnprocessableEntity(c, "Invalid JSON provided")
	}

	var user models.User
	db.Where("email = ?", body.Email).First(&user)

	err := bcrypt.CompareHashAndPassword([]byte(user.Password), []byte(body.Password))
	if err != nil {
		return exceptions.Unauthorized(c)
	}

	token, err := s.GenerateToken(context.Background(), user.ID.String())
	if err != nil {
		return exceptions.InternalServerError(c, "failed to generate JWT token")
	}

	return c.Status(fiber.StatusOK).JSON(fiber.Map{
		"token": token,
	})
}

func (s *authController) GenerateToken(ctx context.Context, userId string) (string, error) {
	token, err := s.jwt.GenToken(userId, time.Now().Add(time.Hour*24*90)) // 90 days
	if err != nil {
		return "", errors.Wrap(err, "failed to generate JWT token")
	}

	return token, nil
}
