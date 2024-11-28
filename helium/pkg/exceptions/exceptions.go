package exceptions

import (
	"github.com/gofiber/fiber/v2"
)

type Error struct {
	StatusCode int    `json:"status_code"`
	Message    string `json:"message"`
}

func writeError(c *fiber.Ctx, message string, statusCode int) error {
	resp := Error{
		StatusCode: statusCode,
		Message:    message,
	}

	return c.Status(statusCode).JSON(resp)
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
