package com.example.orders.shipping;

import com.example.orders.common.OrderId;

public class ShippingService {
    public static String describe(OrderId id) {
        return "Commande " + id + " : etiquette prete";
    }
}
