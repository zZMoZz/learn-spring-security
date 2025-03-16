package com.zzz.spring_security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // Declare some values for readability
        int status = HttpStatus.FORBIDDEN.value();
        String path = request.getRequestURI(); // path that user try to request.
        String error = HttpStatus.FORBIDDEN.getReasonPhrase(); // give us the name of status as string.
        LocalDateTime currentDate = LocalDateTime.now();
        String errorMessage = (accessDeniedException != null && accessDeniedException.getMessage() != null) ?
                accessDeniedException.getMessage() : "Authorization failed"; // try to fetch the message error of the exception, otherwise; return "Unauthorized"

        // Set Header and Status
        response.setStatus(status);
        response.setHeader("denied-reason", "Authorization failed"); // You can provide any key:value as you want.
        // or override exist key with a new value.

        // Set body content type (without it, body response format will be plain text not json)
        response.setContentType("application/json;charset=UTF-8");

        // Form JSON response
        String jsonResponse = String.format(
                "{\"timestamp\": \"%s\", \"status\": %d, \"error\": \"%s\", \"message\": \"%s\", \"path\": \"%s\"}",
                currentDate, status, error, errorMessage, path);

        // Set the response body content
        response.getWriter().write(jsonResponse);
    }
}
