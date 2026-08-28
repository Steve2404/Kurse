package ch8_lambdas.solutions;

import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans lambdas.exercises.Exercise06_PrimitiveFunctionalInterfaces.
 */
public class Solution06_PrimitiveFunctionalInterfaces {

    public static int countMatching(int[] values, IntPredicate predicate) {
        int count = 0;
        for (int value : values) {
            if (predicate.test(value)) {
                count++;
            }
        }
        return count;
    }

    public static int[] transformAll(int[] values, IntUnaryOperator operator) {
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = operator.applyAsInt(values[i]);
        }
        return result;
    }

    public static int reduce(int[] values, int identity, IntBinaryOperator operator) {
        int accumulator = identity;
        for (int value : values) {
            accumulator = operator.applyAsInt(accumulator, value);
        }
        return accumulator;
    }
}
