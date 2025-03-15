package com.zzz.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        // write some authorization rules for endpoints
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/test1", "/test2").authenticated()
                .requestMatchers("/test3", "/test4", "/error", "/register").permitAll() // without permit "/error", the error details will not appear for the client.
                .requestMatchers("/test5").denyAll());

        // apply http basic with default configuration
        http.httpBasic(Customizer.withDefaults());
        // you can only delete the method to deactivate it, but to be make sure, explicitly disable it
        http.formLogin(flc -> flc.disable())
        // disable csrf
        .csrf(csrf -> csrf.disable());
        return http.build();
    }

    /*
    * I add this bean because the "AuthController" class say
    * I can't autowired PasswordEncoder as no bean of it.
    *  You may say but, there is passwordEncoder where it used in login operation.
    * You alright but the Authentication provider creates an instance of it locally it is
    not a bean, so doesn't appear for the rest app files.
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
