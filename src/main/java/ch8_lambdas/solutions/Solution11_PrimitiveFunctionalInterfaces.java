package ch8_lambdas.solutions;

import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise11_PrimitiveFunctionalInterfaces.
 */
public class Solution11_PrimitiveFunctionalInterfaces {

    public static int countMatching(int[] values, IntPredicate predicate) {
        // IntPredicate.test(int) : aucun boxing en Integer.
        int count = 0;
        for (int value : values) {
            if (predicate.test(value)) {
                count++;
            }
        }
        return count;
    }

    public static int[] transformAll(int[] values, IntUnaryOperator operator) {
        // IntUnaryOperator.applyAsInt(int) rend un int.
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = operator.applyAsInt(values[i]);
        }
        return result;
    }

    public static int reduce(int[] values, int identity, IntBinaryOperator operator) {
        // IntBinaryOperator combine deux int ; identity est le point de depart.
        int accumulator = identity;
        for (int value : values) {
            accumulator = operator.applyAsInt(accumulator, value);
        }
        return accumulator;
    }
}
