package com.gateway.security;

import com.gateway.exceptions.InvalidCredentialsException;

import java.io.IOException;

public interface AuthenticationProvider {
    AuthenticatedUser Authenticate(String emailAddress, String password) throws InvalidCredentialsException, IOException;
}
