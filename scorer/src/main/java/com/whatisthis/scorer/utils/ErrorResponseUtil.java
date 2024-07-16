package com.whatisthis.scorer.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whatisthis.scorer.errors.UnauthorizedError;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ErrorResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeUnauthorizedErrorResponse(HttpServletResponse response, String message) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        UnauthorizedError error = new UnauthorizedError(message);
        try {
            String jsonResponse = objectMapper.writeValueAsString(error);
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            System.err.println("Error writing unauthorized response: " + e.getMessage());
            fallbackErrorResponse(response, message);
        }
    }

    private static void fallbackErrorResponse(HttpServletResponse response, String message) {
        try {
            String jsonResponse = String.format("{\"code\": %d, \"message\": \"%s\"}", HttpServletResponse.SC_UNAUTHORIZED, message);
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            System.err.println("Error writing fallback unauthorized response: " + e.getMessage());
            // As a last resort, set a plain text response
            response.setContentType("text/plain");
            try {
                response.getWriter().write("Unauthorized: " + message);
            } catch (IOException ex) {
                System.err.println("Error writing plain text unauthorized response: " + ex.getMessage());
            }
        }
    }
}
