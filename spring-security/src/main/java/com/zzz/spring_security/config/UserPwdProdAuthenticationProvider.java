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
@Profile("prod")
@RequiredArgsConstructor
public class UserPwdProdAuthenticationProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // fetch coming username and password from authentication object
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString(); // getCredentials can return multiple types. so, we need to convert it to String

        // fetch user details from database using coming username
        UserDetails userDetails = userDetailsService.loadUserByUsername(username); // we already handled the error internally.

        // check if password is valid or not (compare with stored password)
        if (passwordEncoder.matches(pwd, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, pwd, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid password");
        }

        /* Why don't retrieve these information from repository directly?
        * I think because we don't to retrieve the full information about the user
        * we only want to fetch information that help us in the security. So, we fetch
        * this information using UserDetails object and also this object provide useful methods
        * Also, the roles stored using special way not string, So in any location we want to retrieve
        * the roles from the database, we should convert it to the roles data type. So, we use UserDetails
        * that already we do this process only once.
        * */

        /* Why we don't return the retrieved authentication object?
        because the original authentication object is not yet authenticated and we
        should return fully authenticated `Authentication` object. This returned object
        indicates that the authentication was successful and makes us adding the authorities.
         */

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
