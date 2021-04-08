package com.gateway.controllers;

import java.nio.file.AccessDeniedException;
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

//import es.softtek.jwtDemo.dto.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {

    @Autowired
    private AuthenticationProvider _authenticationProvider;

    @PostMapping("user")
    public LoginSucceedMessage login(@RequestParam("user") String username, @RequestParam("password") String pwd) throws InvalidCredentialsException {

        AuthenticatedUser user = _authenticationProvider.Authenticate(username, pwd);
        String token = getJWTToken(user);
        return new LoginSucceedMessage(user, token);
    }

    private String getJWTToken(AuthenticatedUser user) {
        String secretKey = SecurityConfiguration.JwtEncryptionKey;
        //List<GrantedAuthority> grantedAuthorities = AuthorityUtils
        //        .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("zdrowe-jedzenie-jwt")
                .setSubject(user.getName())
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