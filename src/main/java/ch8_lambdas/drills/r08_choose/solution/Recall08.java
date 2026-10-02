package ch8_lambdas.drills.r08_choose.solution;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntBiFunction;
import java.util.function.UnaryOperator;

/**
 * SOLUTION du drill de rappel 8 - choisir la bonne interface fonctionnelle d'apres la forme de la lambda.
 */
public class Recall08 {

    public static void main(String[] args) {
        // () -> T
        Supplier<String> s1 = () -> "t";
        // () -> double / boolean
        DoubleSupplier s2 = () -> 2.5;
        BooleanSupplier s3 = () -> false;
        System.out.println("D01 : " + s1.get() + " " + s2.getAsDouble() + " " + s3.getAsBoolean());
        StringBuilder out = new StringBuilder();
        // T -> void ; (T, U) -> void
        Consumer<String> c1 = out::append;
        BiConsumer<String, Integer> c2 = (a, n) -> out.append(n).append(a);
        c1.accept("x");
        c2.accept("y", 2);
        System.out.println("D02 : " + out);
        // T -> boolean ; (T, U) -> boolean ; int -> boolean
        Predicate<String> p1 = String::isBlank;
        BiPredicate<String, Character> p2 = (str, ch) -> str.indexOf(ch) >= 0;
        IntPredicate p3 = n -> n > 0;
        System.out.println("D03 : " + p1.test(" ") + " " + p2.test("java", 'v') + " " + p3.test(-1));
        // T -> R ; (T, U) -> R ; T -> T ; (T, T) -> T
        Function<String, Character> f1 = str -> str.charAt(0);
        BiFunction<String, Integer, String> f2 = String::substring;
        UnaryOperator<String> f3 = str -> str + "?";
        BinaryOperator<String> f4 = String::concat;
        System.out.println("D04 : " + f1.apply("zeta") + " " + f2.apply("lambda", 3) + " " + f3.apply("quoi") + " " + f4.apply("con", "cat"));
        // T -> double ; (T, U) -> int ; (int, int) -> int
        ToDoubleFunction<String> d1 = str -> str.length() / 2.0;
        ToIntBiFunction<String, String> i1 = String::compareTo;
        IntBinaryOperator i2 = (a, b) -> a % b;
        System.out.println("D05 : " + d1.applyAsDouble("abc") + " " + i1.applyAsInt("b", "a") + " " + i2.applyAsInt(17, 5));
    }
}
