package com.example.shipping.app;

import com.example.shipping.api.ShippingRate;

import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class Main {

    static List<String> providerTypes() {
        // stream() rend des Provider : type() donne la classe SANS instancier le fournisseur (get() le creerait).
        return ServiceLoader.load(ShippingRate.class).stream()
                .map(p -> p.type().getSimpleName())
                .sorted()
                .collect(Collectors.toList());
    }

    static String cheapest(double kg) {
        // Ici on a besoin des prix : get() instancie chaque fournisseur (via provider() pour la Poste).
        ShippingRate best = ServiceLoader.load(ShippingRate.class).stream()
                .map(ServiceLoader.Provider::get)
                .min(Comparator.comparingDouble(r -> r.price(kg)))
                .orElseThrow();
        return best.carrier() + " (" + best.price(kg) + ")";
    }

    public static void main(String[] args) {
        System.out.println("Fournisseurs trouves : " + ServiceLoader.load(ShippingRate.class).stream().count());
        System.out.println("Fournisseurs : " + providerTypes());
        System.out.println("2 kg : " + cheapest(2));
        System.out.println("20 kg : " + cheapest(20));
    }
}
