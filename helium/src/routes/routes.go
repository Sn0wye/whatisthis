package routes

import (
	"whatisthis/pkg/config"
	"whatisthis/pkg/jwt"
	"whatisthis/pkg/logger"
	"whatisthis/pkg/messaging"
	"whatisthis/src/controllers"
	"whatisthis/src/db"

	"github.com/gofiber/fiber/v2"
)

func BindAuthRoutes(app *fiber.App, jwtMiddleware fiber.Handler, log *logger.Logger, rmq *messaging.MessagingService) {
	db := db.GetDB()
	conf := config.GetConfig()
	jwt := jwt.NewJwt(conf)

	router := app.Group("/auth")

	controller := controllers.NewAuthController(db, jwt, rmq)

	router.Post("/login", controller.Login)
	router.Post("/register", controller.Register)

	router.Get("/profile", jwtMiddleware, controller.Profile)
}
