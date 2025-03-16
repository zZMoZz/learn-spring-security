package com.zzz.spring_security.filter;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtTokenValidator extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // fetch the token from the request
        String jwt = request.getHeader("Authorization");

        // validate logic
        if (jwt != null && jwt.startsWith("Bearer ")) {
            // fetch the token alone, remove "Bearer " prefix
            jwt = jwt.substring(7);

            // fetch the secret key and prepare it
            Environment env = getEnvironment(); // fetch all environments variables
            String secretValue = env.getProperty("JWT_SECRET");
            if (secretValue == null) {
                throw new IllegalStateException("Internal Server Error: Unable to authentication");
            }
            SecretKey secretKey = Keys.hmacShaKeyFor(secretValue.getBytes(StandardCharsets.UTF_8));

            try {
                // build jwt parser, parse and validate the token, extract the payloads
                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();

                // fetch the needed user information
                String username = String.valueOf(claims.get("username"));
                String authorities = String.valueOf(claims.get("authorities"));

                // Also to inform spring security that the user already authenticated
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

                // hold these information in the context holder to can retrieve it later
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                throw new BadCredentialsException("Invalid Token received!");
            }

            // if Authorization header empty, makes filter continue.
            // Don't worry the authentication filter will stop the request because it is not authenticated yet.
            filterChain.doFilter(request, response);
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/user");
    }
}
