package ch8_lambdas.solutions;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Corrige de l'exercice 18. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise18_CurryBiFunction.
 */
public class Solution18_CurryBiFunction {

    public static <A, B, R> Function<A, Function<B, R>> curry(BiFunction<A, B, R> biFunction) {
        // Une fonction qui rend une fonction : a est capture, b arrive plus tard.
        return a -> b -> biFunction.apply(a, b);
    }

    public static <A, B, R> BiFunction<A, B, R> uncurry(Function<A, Function<B, R>> curried) {
        // L'inverse : on applique les deux etapes l'une apres l'autre.
        return (a, b) -> curried.apply(a).apply(b);
    }
}
