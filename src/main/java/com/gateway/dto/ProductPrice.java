package com.gateway.dto;

import java.util.UUID;

public class ProductPrice {
    private UUID id;
    private double price;

    public ProductPrice(UUID productId, double price) {
        this.id = productId;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
