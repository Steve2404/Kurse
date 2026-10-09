package ch18_design.projects.p02_shipping.solution;

import java.util.List;

/**
 * Le seul endroit qui NOMME les strategies de la boutique : la racine de composition.
 * Ajouter un transporteur, c'est ajouter une ligne ici (et sa classe), rien d'autre.
 */
public final class Shop {

    private Shop() {
    }

    public static ShippingCalculator standard() {
        return new ShippingCalculator(
                List.of(new PostRate(), new ExpressRate(), new PickupRate(), new FreightRate()),
                List.of(Surcharges.customs(), Surcharges.fragile()));
    }
}
