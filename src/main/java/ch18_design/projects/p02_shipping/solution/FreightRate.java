package ch18_design.projects.p02_shipping.solution;

/**
 * Etape 6 : le transporteur de fret, pour les gros colis en France. Ajoute SANS modifier le calculateur :
 * une nouvelle classe, et une ligne dans Shop.standard().
 */
public final class FreightRate implements ShippingRate {

    @Override
    public String code() {
        return "FREIGHT";
    }

    // Seulement AU-DESSUS de 30 kg (30 kg pile, c'est encore la poste) et seulement en France.
    @Override
    public boolean accepts(Parcel parcel) {
        return parcel.domestic() && parcel.grams() > 30_000;
    }

    @Override
    public long basePrice(Parcel parcel) {
        return 4900 + (parcel.grams() - 30_000 + 999) / 1000 * 90L;
    }
}
