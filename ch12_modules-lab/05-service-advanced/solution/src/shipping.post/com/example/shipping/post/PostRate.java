package com.example.shipping.post;

import com.example.shipping.api.ShippingRate;

/**
 * La Poste : PAS de constructeur public. ServiceLoader passera par la methode
 * public static provider() (autorisee depuis Java 9). Piege verifie : Provider.type()
 * rend le type de RETOUR declare de provider() ; s'il rendait ShippingRate, type() dirait ShippingRate.
 */
public class PostRate implements ShippingRate {

    private PostRate() {
    }

    public static PostRate provider() {
        return new PostRate();
    }

    @Override
    public String carrier() {
        return "Poste";
    }

    @Override
    public double price(double kg) {
        return 4 + 1.5 * kg;
    }
}
