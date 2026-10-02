package ch8_lambdas.drills.r05_primitives.solution;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleToIntFunction;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongSupplier;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;

/**
 * SOLUTION du drill de rappel 5 - les interfaces fonctionnelles primitives.
 */
public class Recall05 {

    public static void main(String[] args) {
        IntSupplier answer = () -> 42;
        LongSupplier big = () -> 1L << 40;
        BooleanSupplier yes = () -> true;
        System.out.println("D01 : " + answer.getAsInt() + " " + big.getAsLong() + " " + yes.getAsBoolean());
        IntPredicate even = n -> n % 2 == 0;
        IntUnaryOperator square = n -> n * n;
        IntBinaryOperator max = Math::max;
        System.out.println("D02 : " + even.test(7) + " " + even.negate().test(7) + " " + square.applyAsInt(9) + " " + square.andThen(n -> n + 1).applyAsInt(3) + " "
                + max.applyAsInt(4, 9));
        ToIntFunction<String> length = String::length;
        ToIntBiFunction<String, String> both = (a, b) -> a.length() + b.length();
        IntFunction<String> stars = n -> "*".repeat(n);
        System.out.println("D03 : " + length.applyAsInt("lambda") + " " + both.applyAsInt("ab", "cde") + " " + stars.apply(4));
        IntToDoubleFunction half = n -> n / 2.0;
        DoubleToIntFunction floor = d -> (int) Math.floor(d);
        DoubleBinaryOperator avg = (a, b) -> (a + b) / 2;
        System.out.println("D04 : " + half.applyAsDouble(7) + " " + floor.applyAsInt(-2.5) + " " + avg.applyAsDouble(3, 4));
        StringBuilder sb = new StringBuilder();
        ObjIntConsumer<StringBuilder> append = (s, n) -> s.append(n).append(',');
        for (int i = 1; i <= 3; i++) {
            append.accept(sb, i * i);
        }
        System.out.println("D05 : " + sb);
    }
}
