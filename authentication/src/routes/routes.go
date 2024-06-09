package routes

import (
	"whatisthis/pkg/config"
	"whatisthis/pkg/jwt"
	"whatisthis/pkg/logger"
	"whatisthis/src/controllers"
	"whatisthis/src/db"

	"github.com/gin-gonic/gin"
)

func BindHTTPRoutes(r *gin.Engine, jwtMiddleware gin.HandlerFunc, log *logger.Logger) {
	db := db.GetDB()
	conf := config.GetConfig()
	jwt := jwt.NewJwt(conf)

	controller := controllers.NewAuthController(db, jwt)

	r.POST("/login", controller.Login)
	r.POST("/register", controller.Register)

	r.Use(jwtMiddleware)
	r.GET("/profile", controller.Profile)
}
