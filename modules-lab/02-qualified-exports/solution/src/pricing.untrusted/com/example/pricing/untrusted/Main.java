package com.example.pricing.untrusted;

import com.example.pricing.engine.Discount;
import com.example.pricing.engine.TenPercentDiscount;

public class Main {
    public static void main(String[] args) {
        Discount discount = new TenPercentDiscount();
        System.out.println("Prix untrusted apres remise : " + discount.apply(100.0));
    }
}
