package com.gateway.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import com.gateway.SecurityConfiguration;
import com.gateway.dto.LoginRequest;
import com.gateway.security.AuthenticatedUser;
import com.gateway.security.AuthenticationProvider;
import com.gateway.exceptions.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class SessionController {

    @Autowired
    private AuthenticationProvider _authenticationProvider;

    @PostMapping(value = "/login", consumes = "application/json")
    public String login(@RequestBody LoginRequest loginRequest)
            throws InvalidCredentialsException, IOException {

        AuthenticatedUser user = _authenticationProvider.Authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        return getJWTToken(user);
    }

    private String getJWTToken(AuthenticatedUser user) {
        String secretKey = SecurityConfiguration.JwtEncryptionKey;

        String token = Jwts
                .builder()
                .setId("zdrowe-jedzenie-jwt")
                .setSubject(user.getEmail())
                .claim("authorities",
                        user.getGrantedAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .claim("first-name", user.getFirstName())
                .claim("last-name", user.getLastName())
                .claim("user-id", user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}