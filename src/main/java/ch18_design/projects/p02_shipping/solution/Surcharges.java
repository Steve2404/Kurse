package ch18_design.projects.p02_shipping.solution;

import java.util.Set;

/** Les surcharges de la boutique, ecrites en lambdas : une strategie d'une ligne n'a pas besoin d'une classe. */
public final class Surcharges {

    static final Set<String> EU = Set.of("FR", "BE", "DE", "ES", "IT", "LU", "NL");

    private Surcharges() {
    }

    // +15 % du prix de base, arrondi au centime le plus proche.
    public static Surcharge fragile() {
        return (parcel, base) -> parcel.fragile() ? (base * 15 + 50) / 100 : 0;
    }

    // 8,00 de frais de douane hors de l'Union europeenne.
    public static Surcharge customs() {
        return (parcel, base) -> EU.contains(parcel.country()) ? 0 : 800;
    }
}
