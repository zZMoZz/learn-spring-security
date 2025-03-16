package com.zzz.spring_security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class UserPwdAnyAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // fetch coming username and password from authentication object
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString(); // getCredentials can return multiple types. so, we need to convert it to String

        // fetch user details to get the roles and make sure that received username is valid
        UserDetails userDetails = userDetailsService.loadUserByUsername(username); // we already handled the error internally.

        // means that authentication is success, in login form, you can provide any password, but you must provide accurate username
        return new UsernamePasswordAuthenticationToken(username, pwd, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
