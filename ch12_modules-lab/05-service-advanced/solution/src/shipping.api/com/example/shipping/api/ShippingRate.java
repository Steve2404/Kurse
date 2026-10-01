package com.example.shipping.api;

/** L'interface du service : chaque transporteur donne son nom et son prix pour un poids. */
public interface ShippingRate {

    String carrier();

    double price(double kg);
}
