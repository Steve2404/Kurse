package ch8_lambdas.solutions;

import java.util.function.Function;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise07_FunctionComposition.
 */
public class Solution07_FunctionComposition {

    public static Function<Double, Double> addShippingFee(double fee) {
        // Fabrique de Function : fee est capture.
        return price -> price + fee;
    }

    public static Function<Double, Double> applyTaxRate(double rate) {
        // Meme principe, avec un taux capture.
        return price -> price * (1 + rate);
    }

    public static double priceWithShippingThenTax(double price, double fee, double rate) {
        // f.andThen(g) : f d'abord, puis g sur le resultat.
        Function<Double, Double> shipping = addShippingFee(fee);
        Function<Double, Double> tax = applyTaxRate(rate);
        return shipping.andThen(tax).apply(price);
    }

    public static double priceWithTaxThenShipping(double price, double fee, double rate) {
        // f.compose(g) : g d'abord, puis f (l'ordre inverse d'andThen).
        Function<Double, Double> shipping = addShippingFee(fee);
        Function<Double, Double> tax = applyTaxRate(rate);
        return shipping.compose(tax).apply(price);
    }
}
