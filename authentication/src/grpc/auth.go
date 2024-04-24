package grpc

import (
	"context"
	"whatisthis/pb"

	"google.golang.org/grpc"
)

type authService struct {
	pb.UnimplementedAuthServiceServer
}

func (s *authService) ValidateToken(ctx context.Context, req *pb.TokenRequest) (*pb.TokenResponse, error) {
	return &pb.TokenResponse{Valid: true}, nil
}

func RegisterAuthService(s *grpc.Server) {
	pb.RegisterAuthServiceServer(s, &authService{})
}
