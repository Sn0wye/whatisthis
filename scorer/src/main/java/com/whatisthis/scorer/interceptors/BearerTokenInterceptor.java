package com.whatisthis.scorer.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.whatisthis.scorer.services.GrpcAuthService;
import pb.Auth;

@Component
public class BearerTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private GrpcAuthService grpcAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String bearerToken = authorizationHeader.substring(7); // Remove "Bearer " prefix
            try {
                Auth.ParseTokenResponse validatedToken = grpcAuthService.parseToken(bearerToken);
                request.setAttribute("bearerToken", bearerToken);
                request.setAttribute("userId", validatedToken.getSub());
                request.setAttribute("token", validatedToken);
            } catch (Exception e) {
                System.out.println("JWT parsing or validation failed: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }
        return true;
    }
}

