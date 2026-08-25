package com.example.orders.common;

public class OrderId {
    private final String value;

    public OrderId(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
