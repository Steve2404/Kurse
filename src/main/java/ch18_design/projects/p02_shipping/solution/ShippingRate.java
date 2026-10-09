package ch18_design.projects.p02_shipping.solution;

/**
 * La STRATEGIE d'un transporteur. Le calculateur ne connait que cette interface : ajouter un transporteur,
 * c'est ecrire une nouvelle classe, sans modifier une ligne de l'existant (le O de SOLID).
 */
public interface ShippingRate {

    String code();

    boolean accepts(Parcel parcel);

    /** Le prix de base en centimes, avant les surcharges. Appele seulement si accepts(parcel). */
    long basePrice(Parcel parcel);
}
