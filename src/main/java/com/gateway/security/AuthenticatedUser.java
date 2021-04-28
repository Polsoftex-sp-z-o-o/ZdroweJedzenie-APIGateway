package com.gateway.security;

import com.gateway.dto.ServiceUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        var authorities = serviceUser.getRoles().stream()
                .map(su -> (GrantedAuthority) new SimpleGrantedAuthority(su.getName()))
                .collect(Collectors.toList());

        return new AuthenticatedUser(serviceUser.getId(), serviceUser.getEmail(), authorities);
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
