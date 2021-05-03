package com.gateway.security;

import java.util.UUID;

public class GatewayPrincipal {
    private final UUID userId;

    public GatewayPrincipal(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
