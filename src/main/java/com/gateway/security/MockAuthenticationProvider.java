package com.gateway.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class MockAuthenticationProvider implements AuthenticationProvider{
    @Override
    public AuthenticatedUser Authenticate(String username, String password) throws InvalidCredentialsException{
        if(username.equals("admin") && password.equals("admin"))
        {
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                    .commaSeparatedStringToAuthorityList("ROLE_USER,ROLE_ADMIN");
            return new AuthenticatedUser(UUID.randomUUID(), username, grantedAuthorities);
        }
        else if(username.equals("user") && password.equals("user"))
        {
            List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                    .commaSeparatedStringToAuthorityList("ROLE_USER");
            return new AuthenticatedUser(UUID.randomUUID(), username, grantedAuthorities);
        }
        else
        {
            throw new InvalidCredentialsException();
        }
    }


}
