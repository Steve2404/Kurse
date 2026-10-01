package com.example.orders.processing;

import com.example.orders.common.OrderId;

public class OrderProcessor {
    public static String describe(OrderId id) {
        return "Commande " + id + " : en cours de traitement";
    }
}
