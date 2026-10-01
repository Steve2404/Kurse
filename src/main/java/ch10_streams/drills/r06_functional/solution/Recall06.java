package ch10_streams.drills.r06_functional.solution;

import ch10_streams.drills.Data;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToIntFunction;
import java.util.function.ObjIntConsumer;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * SOLUTION du drill de rappel 6 - interfaces fonctionnelles primitives.
 * Regle de nommage : IntXxx = recoit un int ; ToIntXxx = RENVOIE un int ; XxxToYyy = recoit Xxx, renvoie Yyy.
 */
public class Recall06 {

    public static void main(String[] args) {
        IntPredicate even = n -> n % 2 == 0;
        LongPredicate big = l -> l > 1_000_000_000L;
        DoublePredicate cheap = d -> d < 10.0;
        System.out.println("D01 : " + even.test(4) + " " + even.negate().test(4) + " " + big.test(3_000_000_000L) + " " + cheap.and(d -> d > 5).test(7.5)
                + " " + IntStream.of(Data.NUMBERS).filter(even).count());

        // andThen : this PUIS l'autre ; compose : l'autre PUIS this.
        IntUnaryOperator square = n -> n * n;
        IntUnaryOperator plusOne = n -> n + 1;
        System.out.println("D02 : " + square.andThen(plusOne).applyAsInt(3) + " " + square.compose(plusOne).applyAsInt(3)
                + " " + IntUnaryOperator.identity().applyAsInt(42));

        IntBinaryOperator gcd = (a, b) -> {
            while (b != 0) {
                int t = b;
                b = a % b;
                a = t;
            }
            return a;
        };
        System.out.println("D03 : " + IntStream.of(12, 18, 24).reduce(gcd).getAsInt() + " " + gcd.applyAsInt(35, 21));

        // IntFunction<R> : int -> R (methode apply, pas applyAsXxx car le retour est un objet).
        IntFunction<String> stars = "*"::repeat;
        System.out.println("D04 : " + stars.apply(3) + " " + IntStream.rangeClosed(1, 3).mapToObj(stars).toList());

        ToIntFunction<String> length = String::length;
        ToIntBiFunction<String, String> both = (a, b) -> a.length() + b.length();
        System.out.println("D05 : " + length.applyAsInt("lambda") + " " + both.applyAsInt("java", "stream")
                + " " + Data.WORDS.stream().mapToInt(length).max().getAsInt());

        IntToLongFunction cube = n -> (long) n * n * n;
        IntToDoubleFunction half = n -> n / 2.0;
        System.out.println("D06 : " + cube.applyAsLong(2000) + " " + half.applyAsDouble(7));

        DoubleToIntFunction floor = d -> (int) Math.floor(d);
        LongToIntFunction digits = l -> String.valueOf(l).length();
        System.out.println("D07 : " + floor.applyAsInt(-2.5) + " " + digits.applyAsInt(cube.applyAsLong(2000)));

        // Les Supplier primitifs : getAsInt, getAsLong, getAsDouble, getAsBoolean.
        IntSupplier answer = () -> 42;
        LongSupplier now = () -> 1_790_000_000L;
        DoubleSupplier pi = () -> Math.PI;
        BooleanSupplier ready = () -> answer.getAsInt() > 40;
        System.out.println("D08 : " + answer.getAsInt() + " " + now.getAsLong() + " " + (int) pi.getAsDouble() + " " + ready.getAsBoolean());

        // IntConsumer pour IntStream.forEach ; ObjIntConsumer pour l'accumulateur de IntStream.collect.
        StringBuilder sb = new StringBuilder();
        IntConsumer digit = sb::append;
        IntStream.of(Data.NUMBERS).limit(3).forEach(digit);
        ObjIntConsumer<StringBuilder> withComma = (b, n) -> b.append(b.length() == 0 ? "" : ",").append(n);
        String csv = IntStream.of(Data.NUMBERS).collect(StringBuilder::new, withComma, (x, y) -> x.append(",").append(y)).toString();
        System.out.println("D09 : " + sb + " " + csv);

        ToDoubleBiFunction<Integer, Integer> ratio = (a, b) -> (double) a / b;
        DoubleBinaryOperator avg2 = (a, b) -> (a + b) / 2;
        DoubleUnaryOperator twice = d -> d * 2;
        System.out.println("D10 : " + ratio.applyAsDouble(1, 4) + " " + avg2.applyAsDouble(1, 2) + " " + twice.andThen(twice).applyAsDouble(1.5));

        LongBinaryOperator times = (a, b) -> a * b;
        System.out.println("D11 : " + LongStream.rangeClosed(1, 15).reduce(1, times));
    }
}
