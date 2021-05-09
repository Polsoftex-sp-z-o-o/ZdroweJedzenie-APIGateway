package com.gateway.dto;

public class Payment {
    private Card card;
    private double value;

    public Payment(Card card, double value) {
        this.card = card;
        this.value = value;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
