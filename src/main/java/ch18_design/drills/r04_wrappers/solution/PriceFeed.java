package ch18_design.drills.r04_wrappers.solution;

/** Notre interface : le prix d'un symbole (en majuscules), en centimes. */
@FunctionalInterface
public interface PriceFeed {

    long price(String symbol);
}
