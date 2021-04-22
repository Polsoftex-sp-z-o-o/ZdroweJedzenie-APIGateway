package com.gateway.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

import com.gateway.SecurityConfiguration;
import com.gateway.dto.LoginSucceedMessage;
import com.gateway.security.AuthenticatedUser;
import com.gateway.security.AuthenticationProvider;
import com.gateway.security.InvalidCredentialsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {

    @Autowired
    private AuthenticationProvider _authenticationProvider;

    @PostMapping("/login")
    public LoginSucceedMessage login(
            @RequestParam("username") String username
            , @RequestParam("password") String password)
            throws InvalidCredentialsException, IOException {

        AuthenticatedUser user = _authenticationProvider.Authenticate(username, password);
        String token = getJWTToken(user);
        return new LoginSucceedMessage(user, token);
    }

    private String getJWTToken(AuthenticatedUser user) {
        String secretKey = SecurityConfiguration.JwtEncryptionKey;

        String token = Jwts
                .builder()
                .setId("zdrowe-jedzenie-jwt")
                .setSubject(user.getId().toString())
                .claim("authorities",
                        user.getGrantedAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}