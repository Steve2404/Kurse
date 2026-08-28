package com.example.geo.calculator;

import com.example.geo.units.Distance;

public class Calculator {
    public static Distance sum(Distance a, Distance b) {
        return new Distance(a.getMeters() + b.getMeters());
    }
}
