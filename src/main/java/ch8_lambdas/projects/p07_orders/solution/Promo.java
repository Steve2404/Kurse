package ch8_lambdas.projects.p07_orders.solution;

import java.util.function.LongPredicate;
import java.util.function.LongUnaryOperator;

/**
 * SOLUTION - un code promo : une condition sur le montant et un effet sur le montant (deux lambdas primitives).
 */
public record Promo(String code, LongPredicate eligible, LongUnaryOperator effect) {

    public static Promo parse(String line) {
        String[] p = line.split(" ");
        long value = Long.parseLong(p[2]);
        long minimum = Long.parseLong(p[3]);
        LongUnaryOperator effect = p[1].equals("fixed")
                ? amount -> Math.max(0, amount - value)
                : amount -> amount - Math.round(amount * value / 100.0);
        return new Promo(p[0], amount -> amount >= minimum, effect);
    }

    // Applique la promo seulement si elle est eligible AU MOMENT ou on l'applique.
    public long apply(long amount) {
        return eligible.test(amount) ? effect.applyAsLong(amount) : amount;
    }
}
