package com.zzz.spring_security.config;

import com.zzz.spring_security.entity.Authority;
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
        // fetch the user details
        Person user = personRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // fetch the roles
        String[] roles =  user.getAuthorities().stream().map(Authority::getAuthority).toArray(String[]::new);

        // build UserDetails object and return it the authentication provider
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(roles) // if you use "roles()" method will return an error,
                .build();                  // because roles() method adds "ROLE_" prefix, which means
                                               // roles() method want roles written in this format "USER" not "ROLE_USER"
    }
}
