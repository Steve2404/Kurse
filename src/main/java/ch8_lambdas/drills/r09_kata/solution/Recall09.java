package ch8_lambdas.drills.r09_kata.solution;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * SOLUTION du drill de rappel 9 - kata mixte du chapitre 8.
 */
public class Recall09 {

    static int countIf(int[] values, IntPredicate keep) {
        int n = 0;
        for (int v : values) {
            if (keep.test(v)) {
                n++;
            }
        }
        return n;
    }

    static int applyTimes(IntUnaryOperator f, int times, int start) {
        int x = start;
        for (int i = 0; i < times; i++) {
            x = f.applyAsInt(x);
        }
        return x;
    }

    public static void main(String[] args) {
        int[] values = {3, 8, 12, 5, 20};
        int limit = 6;
        System.out.println("D01 : " + countIf(values, v -> v > limit) + " " + countIf(values, ((IntPredicate) v -> v % 2 == 0).negate()));
        System.out.println("D02 : " + applyTimes(x -> x * 2, 10, 1) + " " + applyTimes(IntUnaryOperator.identity(), 5, 7));
        Function<Integer, Function<Integer, Integer>> adder = a -> b -> a + b;   // currying
        Function<Integer, Integer> add10 = adder.apply(10);
        System.out.println("D03 : " + add10.apply(5) + " " + adder.apply(1).apply(2));
        Predicate<String> valid = Predicate.not(String::isBlank);
        UnaryOperator<String> clean = String::strip;
        Function<String, String> normalize = clean.andThen(String::toLowerCase);
        System.out.println("D04 : " + valid.test("  ") + " [" + normalize.apply("  JaVa ") + "]");
        BiFunction<String, Integer, String> cut = String::substring;
        Supplier<String> lazy = () -> cut.apply("bonjour", 3);
        System.out.println("D05 : " + lazy.get() + " " + cut.andThen(String::length).apply("abcdef", 2));
    }
}
