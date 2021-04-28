package com.gateway.security;

import com.gateway.dto.ServiceUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AuthenticatedUser {
    private final UUID id;
    private final String email;
    private final String firstName;
    private final String lastName;
    private final List<GrantedAuthority> grantedAuthorities;

    public AuthenticatedUser(UUID id, String email, String firstName, String lastName, List<GrantedAuthority> grantedAuthorities) {
        this.id = id;
        this.email = email;
        this.grantedAuthorities = grantedAuthorities;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static AuthenticatedUser FromServiceUser(ServiceUser serviceUser){
        var authorities = serviceUser.getRoles().stream()
                .map(su -> (GrantedAuthority) new SimpleGrantedAuthority(su.getName()))
                .collect(Collectors.toList());

        return new AuthenticatedUser(serviceUser.getId(), serviceUser.getEmail(), serviceUser.getFirstName(), serviceUser.getLastName(), authorities);
    }

    public String getEmail() {
        return email;
    }

    public List<GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    public UUID getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }
}
