package com.example.shipping.express;

import com.example.shipping.api.ShippingRate;

/** Express : un fournisseur classique, avec un constructeur public sans argument. */
public class ExpressRate implements ShippingRate {

    @Override
    public String carrier() {
        return "Express";
    }

    @Override
    public double price(double kg) {
        return 9 + 0.5 * kg;
    }
}
