package com.example.text;

import com.example.core.Money;

/** Depend de core. */
public class Formatter {

    public static String euros(Money money) {
        return money.amount() + " EUR";
    }
}
