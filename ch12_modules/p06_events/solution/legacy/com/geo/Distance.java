package com.geo;

import java.util.Map;

/**
 * SOLUTION - une bibliotheque heritee SANS module-info (geo-tools-1.0.jar -> module automatique "geo.tools").
 */
public final class Distance {

    private static final Map<String, double[]> CITIES = Map.of(
            "Paris", new double[] {48.8566, 2.3522}, "Lyon", new double[] {45.7640, 4.8357},
            "Lille", new double[] {50.6292, 3.0573}, "Marseille", new double[] {43.2965, 5.3698},
            "Bordeaux", new double[] {44.8378, -0.5792}, "Nice", new double[] {43.7102, 7.2620});

    private Distance() {
    }

    // Formule de haversine, rayon terrestre 6371 km, arrondie au km.
    public static long km(String from, String to) {
        double[] a = CITIES.get(from);
        double[] b = CITIES.get(to);
        double dLat = Math.toRadians(b[0] - a[0]);
        double dLon = Math.toRadians(b[1] - a[1]);
        double h = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(Math.toRadians(a[0])) * Math.cos(Math.toRadians(b[0])) * Math.pow(Math.sin(dLon / 2), 2);
        return Math.round(2 * 6371 * Math.asin(Math.sqrt(h)));
    }
}
