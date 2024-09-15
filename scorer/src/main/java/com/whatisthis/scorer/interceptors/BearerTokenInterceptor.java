package com.whatisthis.scorer.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.whatisthis.scorer.services.GrpcAuthService;
import com.whatisthis.scorer.utils.ErrorResponseUtil;
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

                if (validatedToken == null) {
                    System.err.println("Token validation returned null");
                    ErrorResponseUtil.writeUnauthorizedErrorResponse(response, "Invalid token");
                    return false;
                }

                request.setAttribute("bearerToken", bearerToken);
                request.setAttribute("userId", validatedToken.getSub());
                request.setAttribute("token", validatedToken);
            } catch (Exception e) {
                System.err.println("JWT parsing or validation failed: " + e.getMessage());
                ErrorResponseUtil.writeUnauthorizedErrorResponse(response, "JWT parsing or validation failed");
                return false;
            }
        } else {
            ErrorResponseUtil.writeUnauthorizedErrorResponse(response, "Unauthorized");
            return false;
        }
        return true;
    }

}