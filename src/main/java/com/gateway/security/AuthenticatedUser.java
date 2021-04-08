package com.gateway.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;

public class AuthenticatedUser {
    private final String name;
    private final List<GrantedAuthority> grantedAuthorities;

    public AuthenticatedUser(String name, List<GrantedAuthority> grantedAuthorities) {
        this.name = name;
        this.grantedAuthorities = grantedAuthorities;
    }

    public String getName() {
        return name;
    }

    public List<GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }
}
