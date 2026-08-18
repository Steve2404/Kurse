package lambdas.solutions;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans lambdas.exercises.Exercise13_CurryBiFunction.
 */
public class Solution13_CurryBiFunction {

    public static <A, B, R> Function<A, Function<B, R>> curry(BiFunction<A, B, R> biFunction) {
        return a -> b -> biFunction.apply(a, b);
    }

    public static <A, B, R> BiFunction<A, B, R> uncurry(Function<A, Function<B, R>> curried) {
        return (a, b) -> curried.apply(a).apply(b);
    }
}
