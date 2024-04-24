package routes

import (
	"whatisthis/pkg/config"
	"whatisthis/pkg/jwt"
	"whatisthis/pkg/logger"
	"whatisthis/src/controllers"
	"whatisthis/src/db"

	"github.com/gin-gonic/gin"
)

func BindAuthRoutes(r *gin.Engine, jwtMiddleware gin.HandlerFunc, log *logger.Logger) {
	db := db.GetDB()
	conf := config.GetConfig()
	jwt := jwt.NewJwt(conf)

	router := r.Group("/auth")
	controller := controllers.NewAuthController(db, jwt)

	router.POST("/login", controller.Login)
	router.POST("/register", controller.Register)

	router.Use(jwtMiddleware)
	router.GET("/profile", controller.Profile)
}
