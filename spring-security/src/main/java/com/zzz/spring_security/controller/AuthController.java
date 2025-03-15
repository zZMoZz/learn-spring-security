package com.zzz.spring_security.controller;

import com.zzz.spring_security.entity.Person;
import com.zzz.spring_security.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    // This is not a robust endpoint for registrations, there is no any validation on it.
    // It is only for create a quick registration process.

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Person person) {
        try {
            // hash the password
            String hashPwd = passwordEncoder.encode(person.getPassword());
            person.setPassword(hashPwd);

            // save this user to database
            Person savedUser = personRepository.save(person);

            // return a response
            if (savedUser.getId() > 0)
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body("User registered successfully");
            else
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("User registration failed");

        } catch (Exception error) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(error.getMessage());
        }
    }
}
