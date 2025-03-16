package com.zzz.spring_security.config;

import com.zzz.spring_security.exception.CustomAccessDeniedHandler;
import com.zzz.spring_security.exception.CustomBasicAuthenticationEntryPoint;
import com.zzz.spring_security.filter.JwtTokenGenerator;
import com.zzz.spring_security.filter.JwtTokenValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // write some authorization rules for endpoints
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/test1").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/test2").hasRole("ADMIN")
                .requestMatchers("/test3", "/test4", "/error", "/register").permitAll() // without permit "/error", the error details will not appear for the client.
                .requestMatchers("/test5").denyAll());

        // disable session creation
        http.sessionManagement(smc -> smc
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // add jwt filters
        http.addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class);
        http.addFilterAfter(new JwtTokenGenerator(), BasicAuthenticationFilter.class);

        // allow the client to read Authorization header
        // <== add this step in CORS commit ==>

        // apply http basic with adding exception handler for `AuthenticationException` for http basic only not globally
        http.httpBasic(hpc -> hpc
                .authenticationEntryPoint(new CustomBasicAuthenticationEntryPoint()));

        // you can only delete the method to deactivate it, but to be make sure, explicitly disable it
        http.formLogin(flc -> flc.disable());

        // disable csrf
        http.csrf(csrf -> csrf.disable());

        // add exception handler for `AccessDeniedException` globally
        http.exceptionHandling(ehc -> ehc
                .accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }

    /*
    * this is the configuration that used by the authentication provider by default.
    * to use it in your project files, you must create a bean of this password encoder.
    * because authentication provider uses it locally not a bean.
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }



}
