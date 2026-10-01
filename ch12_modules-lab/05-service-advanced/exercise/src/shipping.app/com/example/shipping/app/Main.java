package com.example.shipping.app;

import com.example.shipping.api.ShippingRate;

import java.util.List;
import java.util.ServiceLoader;

public class Main {

    static List<String> providerTypes() {
        // TODO 4 : les noms SIMPLES des classes fournisseurs, tries, SANS creer aucun objet fournisseur
        //          (ServiceLoader.stream() et Provider.type()).
        throw new UnsupportedOperationException("TODO 4 : implementer providerTypes()");
    }

    static String cheapest(double kg) {
        // TODO 5 : le fournisseur le moins cher pour ce poids, sous la forme "Poste (7.0)".
        throw new UnsupportedOperationException("TODO 5 : implementer cheapest()");
    }

    public static void main(String[] args) {
        System.out.println("Fournisseurs trouves : " + ServiceLoader.load(ShippingRate.class).stream().count());
        System.out.println("Fournisseurs : " + providerTypes());
        System.out.println("2 kg : " + cheapest(2));
        System.out.println("20 kg : " + cheapest(20));
    }
}
