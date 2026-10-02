package ch8_lambdas.drills.r03_builtin.solution;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * SOLUTION du drill de rappel 3 - les interfaces fonctionnelles du JDK.
 */
public class Recall03 {

    public static void main(String[] args) {
        Supplier<String> greeting = () -> "salut";
        Supplier<StringBuilder> builder = StringBuilder::new;
        System.out.println("D01 : " + greeting.get() + " " + builder.get().append("x").length() + " " + (builder.get() != builder.get()));
        StringBuilder log = new StringBuilder();
        Consumer<String> add = s -> log.append(s).append(';');
        BiConsumer<String, Integer> repeat = (s, n) -> log.append(s.repeat(n)).append(';');
        add.accept("a");
        repeat.accept("b", 3);
        System.out.println("D02 : " + log);
        Predicate<String> isLong = s -> s.length() > 4;
        BiPredicate<String, Integer> hasLength = (s, n) -> s.length() == n;
        System.out.println("D03 : " + isLong.test("lambda") + " " + isLong.test("java") + " " + hasLength.test("java", 4));
        Function<String, Integer> length = String::length;
        BiFunction<String, String, String> join = (x, y) -> x + "-" + y;
        System.out.println("D04 : " + length.apply("fonction") + " " + join.apply("a", "b"));
        UnaryOperator<String> upper = String::toUpperCase;
        BinaryOperator<Integer> sum = Integer::sum;
        System.out.println("D05 : " + upper.apply("ok") + " " + sum.apply(20, 22));
        // UnaryOperator<T> EST une Function<T, T> ; BinaryOperator<T> EST une BiFunction<T, T, T>.
        Function<String, String> asFunction = upper;
        BiFunction<Integer, Integer, Integer> asBiFunction = sum;
        System.out.println("D06 : " + asFunction.apply("a") + " " + asBiFunction.apply(1, 2));
    }
}
