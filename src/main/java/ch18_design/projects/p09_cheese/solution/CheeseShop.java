package ch18_design.projects.p09_cheese.solution;

import java.util.List;

/** La cave : chaque article vieillit selon SA regle. Plus aucun nom de produit ici. */
public final class CheeseShop {

    private CheeseShop() {
    }

    public static List<Cheese> nextDay(List<Cheese> stock) {
        return stock.stream().map(cheese -> RuleBook.ruleFor(cheese.name()).age(cheese)).toList();
    }

    public static List<Cheese> afterDays(List<Cheese> stock, int days) {
        List<Cheese> current = stock;
        for (int day = 0; day < days; day++) {
            current = nextDay(current);
        }
        return current;
    }
}
