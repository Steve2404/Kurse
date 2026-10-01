package com.example.app;

import com.example.core.Money;
import com.example.text.Formatter;

/** Depend de text-utils ET de core. */
public class Main {

    public static void main(String[] args) {
        System.out.println("Total : " + Formatter.euros(new Money(1250)));
    }
}
