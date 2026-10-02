package ch8_lambdas.drills.r05_primitives.solution;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleConsumer;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToLongFunction;
import java.util.function.IntConsumer;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToLongBiFunction;
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
        // Les consommateurs primitifs, chaines par andThen, ecrivent dans le meme StringBuilder.
        StringBuilder log = new StringBuilder();
        IntConsumer i = n -> log.append('i').append(n);
        LongConsumer l = n -> log.append(" l").append(n);
        DoubleConsumer d = x -> log.append(" d").append(x);
        ObjLongConsumer<StringBuilder> ol = (b, n) -> b.append(" ol").append(n);
        ObjDoubleConsumer<StringBuilder> od = (b, x) -> b.append(" od").append(x);
        i.andThen(n -> log.append('+')).accept(1);
        l.accept(2L);
        d.accept(3.5);
        ol.accept(log, 4L);
        od.accept(log, 5.5);
        System.out.println("D06 : " + log);
        DoublePredicate positive = x -> x > 0;
        LongBinaryOperator gcd = (a, b) -> {
            while (b != 0) {
                long t = a % b;
                a = b;
                b = t;
            }
            return a;
        };
        LongFunction<String> hex = Long::toHexString;
        LongToIntFunction digits = n -> String.valueOf(n).length();
        LongToDoubleFunction kilo = n -> n / 1000.0;
        DoubleToLongFunction round = Math::round;
        ToLongBiFunction<String, String> totalLength = (a, b) -> (long) a.length() + b.length();
        ToDoubleBiFunction<Integer, Integer> ratio = (a, b) -> (double) a / b;
        System.out.println("D07 : " + positive.negate().test(-1.5) + " " + gcd.applyAsLong(84, 36) + " " + hex.apply(255L) + " " + digits.applyAsInt(123456L) + " "
                + kilo.applyAsDouble(1500L) + " " + round.applyAsLong(2.5) + " " + totalLength.applyAsLong("ab", "cde") + " " + ratio.applyAsDouble(1, 4));
    }
}
