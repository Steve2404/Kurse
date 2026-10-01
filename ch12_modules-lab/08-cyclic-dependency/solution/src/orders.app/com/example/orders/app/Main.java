package com.example.orders.app;

import com.example.orders.common.OrderId;
import com.example.orders.processing.OrderProcessor;
import com.example.orders.shipping.ShippingService;

public class Main {
    public static void main(String[] args) {
        OrderId id = new OrderId("42");
        System.out.println(OrderProcessor.describe(id));
        System.out.println(ShippingService.describe(id));
    }
}
