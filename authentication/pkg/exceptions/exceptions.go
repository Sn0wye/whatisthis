package exceptions

import (
	"github.com/gofiber/fiber/v2"
)

type Error struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
}

func writeError(c *fiber.Ctx, message string, code int) error {
	resp := Error{
		Code:    code,
		Message: message,
	}

	return c.Status(code).JSON(resp)
}

var (
	BadRequest = func(c *fiber.Ctx, message string) error {
		return writeError(c, message, fiber.StatusBadRequest)
	}
	InternalServerError = func(c *fiber.Ctx, message string) error {
		return writeError(c, message, fiber.StatusInternalServerError)
	}
	Unauthorized = func(c *fiber.Ctx) error {
		return writeError(c, "Unauthorized", fiber.StatusUnauthorized)
	}
	NotFound = func(c *fiber.Ctx, message string) error {
		return writeError(c, message, fiber.StatusNotFound)
	}
	UnprocessableEntity = func(c *fiber.Ctx, message string) error {
		return writeError(c, message, fiber.StatusUnprocessableEntity)
	}
)
