package grpc

import (
	"context"
	"whatisthis/pb"
	"whatisthis/pkg/config"
	"whatisthis/pkg/jwt"

	"google.golang.org/grpc"
)

type authService struct {
	pb.UnimplementedAuthServiceServer
}

func (s *authService) ValidateToken(ctx context.Context, req *pb.TokenRequest) (*pb.TokenResponse, error) {
	conf := config.GetConfig()
	jwt := jwt.NewJwt(conf)

	_, err := jwt.ParseToken(req.Token)
	if err != nil {
		return &pb.TokenResponse{Valid: false}, err
	}

	return &pb.TokenResponse{Valid: true}, nil
}

func RegisterAuthService(s *grpc.Server) {
	pb.RegisterAuthServiceServer(s, &authService{})
}
