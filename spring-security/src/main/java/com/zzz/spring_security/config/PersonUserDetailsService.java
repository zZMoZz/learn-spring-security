package com.zzz.spring_security.config;

import com.zzz.spring_security.entity.Person;
import com.zzz.spring_security.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PersonUserDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // fetch the user details and roles from the database
        Person user = personRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // build UserDetails object and return it the authentication provider
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole()) // it receives a string list of roles
                .build();
    }
}
