package ch8_lambdas.drills.r04_compose.solution;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * SOLUTION du drill de rappel 4 - composer des fonctions.
 */
public class Recall04 {

    public static void main(String[] args) {
        Function<Integer, Integer> plus2 = x -> x + 2;
        Function<Integer, Integer> times3 = x -> x * 3;
        System.out.println("D01 : " + plus2.andThen(times3).apply(1) + " " + plus2.compose(times3).apply(1) + " " + Function.<Integer>identity().apply(5) + " "
                + UnaryOperator.<String>identity().apply("id"));
        Predicate<String> notEmpty = s -> !s.isEmpty();
        Predicate<String> shortWord = s -> s.length() < 5;
        System.out.println("D02 : " + notEmpty.and(shortWord).test("java") + " " + notEmpty.and(shortWord).test("lambda") + " " + notEmpty.negate().or(shortWord).test("")
                + " " + Predicate.not(notEmpty).test("x") + " " + Predicate.isEqual("a").test("a"));
        StringBuilder sb = new StringBuilder();
        Consumer<String> first = s -> sb.append("1:").append(s);
        Consumer<String> second = s -> sb.append(" 2:").append(s.length());
        first.andThen(second).accept("abc");
        System.out.println("D03 : " + sb);
        BiFunction<Integer, Integer, Integer> mult = (a, b) -> a * b;
        System.out.println("D04 : " + mult.andThen(x -> "=" + x).apply(6, 7));
        BinaryOperator<String> shorter = BinaryOperator.minBy((a, b) -> a.length() - b.length());
        BinaryOperator<String> longer = BinaryOperator.maxBy((a, b) -> a.length() - b.length());
        System.out.println("D05 : " + shorter.apply("pomme", "kiwi") + " " + longer.apply("pomme", "kiwi") + " " + shorter.apply("ab", "cd"));
        Function<String, String> trim = String::strip;
        Function<String, Integer> pipeline = trim.andThen(String::length).andThen(n -> n * n);
        System.out.println("D06 : " + pipeline.apply("  abc  "));
    }
}
