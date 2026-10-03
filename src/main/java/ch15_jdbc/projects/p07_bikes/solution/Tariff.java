package ch15_jdbc.projects.p07_bikes.solution;

import ch15_jdbc.projects.p07_bikes.Data;

/**
 * Le tarif, enregistre dans H2 comme fonction TARIF (CREATE ALIAS) : la classe et la methode doivent etre public.
 */
public final class Tariff {

    private Tariff() {
    }

    // Tranche ENTAMEE : une division entiere arrondie vers le haut, (a + b - 1) / b.
    public static int cost(int minutes) {
        int paid = Math.max(0, minutes - Data.FREE);
        return (paid + Data.STEP - 1) / Data.STEP * Data.STEP_PRICE;
    }
}
