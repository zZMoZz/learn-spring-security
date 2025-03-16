package com.zzz.spring_security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.LocalDateTime;

public class CustomBasicAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Declare some attributes
        int status = HttpStatus.UNAUTHORIZED.value(); // value() return the status code as int
        String path = request.getRequestURI(); // path that user try to request.
        String error = HttpStatus.UNAUTHORIZED.getReasonPhrase(); // gives us the name of status as string.
        LocalDateTime currentDate = LocalDateTime.now();
        String errorMessage = (authException != null && authException.getMessage() != null) ?
                authException.getMessage() : "Unauthorized"; // try to fetch the message error of the exception, otherwise; return "Unauthorized"

        // Set Status and Header
        response.setStatus(status);
        response.setHeader("error-reason", "Authentication failed"); // You can provide any key:value as you want.
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
