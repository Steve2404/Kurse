package ch8_lambdas.solutions;

import java.util.function.Function;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans lambdas.exercises.Exercise04_FunctionComposition.
 */
public class Solution04_FunctionComposition {

    public static Function<Double, Double> addShippingFee(double fee) {
        return price -> price + fee;
    }

    public static Function<Double, Double> applyTaxRate(double rate) {
        return price -> price * (1 + rate);
    }

    public static double priceWithShippingThenTax(double price, double fee, double rate) {
        Function<Double, Double> shipping = addShippingFee(fee);
        Function<Double, Double> tax = applyTaxRate(rate);
        return shipping.andThen(tax).apply(price);
    }

    public static double priceWithTaxThenShipping(double price, double fee, double rate) {
        Function<Double, Double> shipping = addShippingFee(fee);
        Function<Double, Double> tax = applyTaxRate(rate);
        return shipping.compose(tax).apply(price);
    }
}
