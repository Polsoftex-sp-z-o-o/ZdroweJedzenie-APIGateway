package com.gateway.security;

public interface AuthenticationProvider {
    AuthenticatedUser Authenticate(String username, String password) throws InvalidCredentialsException;
}
