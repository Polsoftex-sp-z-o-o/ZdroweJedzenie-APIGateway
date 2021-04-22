package com.gateway.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
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

    public static AuthenticatedUser FromServiceUser(ServiceUser serviceUser){
        //todo user ID not yet implemented!
        UUID userId = UUID.randomUUID();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if(serviceUser.getElevation())
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return new AuthenticatedUser(userId, serviceUser.getUsername(), authorities);
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
