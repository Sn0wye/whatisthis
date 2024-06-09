package main

import (
	"flag"
	"fmt"
	"log"
	"net"
	"net/http"

	"whatisthis/pkg/config"
	"whatisthis/pkg/logger"
	"whatisthis/src/db"
	grpcs "whatisthis/src/grpc"
	"whatisthis/src/middleware"
	"whatisthis/src/migration"
	"whatisthis/src/models"
	"whatisthis/src/routes"

	"github.com/gin-gonic/gin"
	"github.com/spf13/viper"
	"google.golang.org/grpc"
)

func main() {
	flag.Parse()

	conf := config.GetConfig()
	logger := logger.NewLog(conf)

	migrateDB(logger)
	startHTTPServer(conf, logger)
	startGRPCServer(conf)
}

func migrateDB(logger *logger.Logger) {
	db := db.GetDB()

	db.AutoMigrate(
		models.RetrieveAll()...,
	)

	migrate := migration.NewMigrate(
		db,
		logger,
	)

	migrate.Run()
}

func startHTTPServer(conf *viper.Viper, logger *logger.Logger) {
	router := gin.Default()
	router.Use(middleware.CORSMiddleware())
	jwt := middleware.JWTMiddleware(conf, logger)

	routes.BindAuthRoutes(router, jwt, logger)

	port := conf.Get("http.port")
	formattedPort := fmt.Sprintf(":%d", port)

	go func() {
		fmt.Printf("HTTP server is running on port %d\n", port)
		if err := http.ListenAndServe(formattedPort, router); err != nil {
			log.Fatalf("failed to start HTTP server: %v", err)
		}
	}()
}

func startGRPCServer(conf *viper.Viper) {
	grpcPort := conf.Get("grpc.port")
	grpcFormattedPort := fmt.Sprintf(":%d", grpcPort)

	lis, err := net.Listen("tcp", grpcFormattedPort)
	if err != nil {
		log.Fatalf("failed to listen: %v", err)
	}

	s := grpc.NewServer()
	grpcs.RegisterAllServices(s)
	log.Printf("gRPC server is running on port %d\n", grpcPort)
	if err := s.Serve(lis); err != nil {
		log.Fatalf("failed to serve gRPC server: %v", err)
	}
}
