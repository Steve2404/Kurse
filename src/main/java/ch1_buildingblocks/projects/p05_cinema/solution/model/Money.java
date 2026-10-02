package ch1_buildingblocks.projects.p05_cinema.solution.model;

/**
 * Affichage des montants en centimes, avec les seuls operateurs arithmetiques.
 */
public class Money {

    public static String euros(int cents) {
        return cents / 100 + "." + cents / 10 % 10 + cents % 10;
    }
}
