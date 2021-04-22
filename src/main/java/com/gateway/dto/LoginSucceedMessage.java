package com.gateway.dto;

import com.gateway.security.AuthenticatedUser;
import com.gateway.security.AuthenticationProvider;

public class LoginSucceedMessage {
    private AuthenticatedUser user;
    private String token;

    public LoginSucceedMessage(AuthenticatedUser user, String token) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public AuthenticatedUser getUser() {
        return user;
    }
}
