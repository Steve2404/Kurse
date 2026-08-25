package com.example.geo.app;

import com.example.geo.calculator.Calculator;
import com.example.geo.units.Distance;

public class Main {
    public static void main(String[] args) {
        Distance total = Calculator.sum(new Distance(3), new Distance(4));
        System.out.println("Distance totale : " + total);
    }
}
