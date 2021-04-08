package com.gateway.security;

import java.nio.file.AccessDeniedException;

public interface AuthenticationProvider {
    AuthenticatedUser Authenticate(String username, String password) throws InvalidCredentialsException;
}
