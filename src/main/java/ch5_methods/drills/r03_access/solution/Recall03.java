package ch5_methods.drills.r03_access.solution;

import ch5_methods.drills.r03_access.solution.club.Special;
import ch5_methods.drills.r03_access.solution.shop.Clerk;
import ch5_methods.drills.r03_access.solution.shop.Item;

/**
 * SOLUTION du drill de rappel 3 - les modificateurs d'acces. Recall03 n'est ni dans shop, ni une sous-classe.
 */
public class Recall03 {

    public static void main(String[] args) {
        Item item = new Item();
        System.out.println("D01 : " + item.name + " " + item.secret());
        System.out.println("D02 : " + Clerk.report(item));
        Special special = new Special();
        System.out.println("D03 : " + special.bonus(3) + " " + Special.peek(special));
        System.out.println("D04 : " + Special.describe());
        item.name = "crayon";
        Item same = item;
        System.out.println("D05 : " + same.name + " " + Clerk.report(same));
        Item viaParent = special;
        System.out.println("D06 : " + viaParent.name + " " + viaParent.secret() + " " + Clerk.report(viaParent));
    }
}
