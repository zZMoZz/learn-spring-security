package com.zzz.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
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

import java.util.HashMap;
import java.util.Map;

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

    /* Notes
         1. we will continue with "DelegatingPasswordEncoder" due to its flexibility
         2. don't implement multiple beans of the same object like this. I do that for explaining only.
    */

    /* Case 1 :
    * this is the configuration that used by the authentication provider by default.
    * to use it in your project files, you must create a bean of this password encoder.
    * because authentication provider uses it locally not a bean.
    */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /* Case 2 :
    * You want to use "DelegatingPasswordEncoder" with your own configuration
    */
    @Bean
    public PasswordEncoder passwordEncoder2() {
        // define a map to hold encoders and associated prefix.
        Map<String, PasswordEncoder> encoderMap = new HashMap<>();

        // Add encoders to map
        encoderMap.put("bcrypt", new BCryptPasswordEncoder());
        encoderMap.put("scrypt", SCryptPasswordEncoder.defaultsForSpringSecurity_v5_8());
        encoderMap.put("noop", NoOpPasswordEncoder.getInstance());

        // Return, with make "bcrypt" as the default during encoding process
        return new DelegatingPasswordEncoder("bcrypt", encoderMap);
    }

    /* Case 3 :
     * You want to use the default "DelegatingPasswordEncoder", but add some configuration to the bcrypt algorithm.
     * Note: you can't add or remove password encoders from the default "DelegatingPasswordEncoder".
     * So,  we will return to the second case and add our bCryptPasswordEncoder to the map.
     * BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2A, 20);
     * You can add both encoders of the same type, only change the prefix.
     */


}
