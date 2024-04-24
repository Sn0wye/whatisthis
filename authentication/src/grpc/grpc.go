package grpc

import (
	grpc "google.golang.org/grpc"
)

func RegisterAllServices(s *grpc.Server) {
	RegisterAuthService(s)
	RegisterUserService(s)
}
