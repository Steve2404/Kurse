package com.example.core;

/** Le coeur du metier : aucune dependance. */
public record Money(long cents) {

    public String amount() {
        return cents / 100 + "," + String.format("%02d", cents % 100);
    }
}
