package com.zzz.spring_security.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtTokenGenerator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // retrieve the authentication object from the security context holder
        // and generate a token if the authentication object is not null
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            // fetch the secret key and prepare it
            Environment env = getEnvironment(); // fetch all environments variables
            String secretValue = env.getProperty("JWT_SECRET");
            if (secretValue == null) {
                throw new IllegalStateException("Internal Server Error: Unable to authentication");
            }
            SecretKey secretKey = Keys.hmacShaKeyFor(secretValue.getBytes(StandardCharsets.UTF_8));

            // build jwt token
            String jwt = Jwts.builder()
                    .issuer("Mohsen").subject("JWT TOKEN")
                    .claim("username", auth.getName())
                    .claim("authorities", auth.getAuthorities()
                            .stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.joining(",")))
                    .issuedAt(new Date())
                    .expiration(new Date((new Date()).getTime() + 3600000))
                    .signWith(secretKey) // specify the secret key
                    .compact(); // finalize jwt creation and returned it in compact form

            // send jwt token through "Authorization" header in the response
            response.setHeader("Authorization", "Bearer " + jwt);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return !request.getServletPath().equals("/user");
    }
}


