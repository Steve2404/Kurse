package ch5_methods.drills.r03_access.solution.club;

import ch5_methods.drills.r03_access.solution.shop.Item;

/**
 * SOLUTION - une sous-classe dans un AUTRE paquet : elle voit le protected, pas le package-private.
 */
public class Special extends Item {

    public int bonus(int n) {
        stock += n;                    // this.stock : herite, donc accessible
        return stock;
    }

    public static int peek(Special s) {
        return s.stock;                // via une reference de type Special : permis
    }

    public static String describe() {
        return label() + " special";   // protected static : accessible dans la sous-classe
    }
}
