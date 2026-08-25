package com.example.geo.units;

public class Distance {
    private final double meters;

    public Distance(double meters) {
        this.meters = meters;
    }

    public double getMeters() {
        return meters;
    }

    @Override
    public String toString() {
        return meters + " m";
    }
}
