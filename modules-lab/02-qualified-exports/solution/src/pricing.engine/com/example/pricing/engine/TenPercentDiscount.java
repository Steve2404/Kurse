package com.example.pricing.engine;

public class TenPercentDiscount implements Discount {
    @Override
    public double apply(double price) {
        return price * 0.9;
    }
}
