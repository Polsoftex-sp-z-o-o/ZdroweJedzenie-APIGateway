package com.gateway.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.UUID;

public class AuthenticatedUser {
    private final UUID id;
    private final String name;
    private final List<GrantedAuthority> grantedAuthorities;

    public AuthenticatedUser(UUID id, String name, List<GrantedAuthority> grantedAuthorities) {
        this.id = id;
        this.name = name;
        this.grantedAuthorities = grantedAuthorities;
    }

    public String getName() {
        return name;
    }

    public List<GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    public UUID getId() {
        return id;
    }
}
