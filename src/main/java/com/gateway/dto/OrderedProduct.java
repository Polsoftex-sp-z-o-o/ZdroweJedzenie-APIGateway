package com.gateway.dto;

import java.util.UUID;

public class OrderedProduct {
    private UUID productId;
    private int quantity;

    public OrderedProduct(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }
}
