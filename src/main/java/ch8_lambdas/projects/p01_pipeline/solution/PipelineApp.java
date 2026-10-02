package ch8_lambdas.projects.p01_pipeline.solution;

import ch8_lambdas.projects.p01_pipeline.Data;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * SOLUTION du projet 1 - le pipeline de texte.
 */
public class PipelineApp {

    public static void main(String[] args) {
        for (String pipeline : Data.PIPELINES) {
            Step step = Step.parse(pipeline);
            StringBuilder out = new StringBuilder(pipeline + " :");
            for (String text : Data.TEXTS) {
                out.append(" [").append(step.apply(text)).append(']');
            }
            System.out.println(out);
        }

        // andThen ou compose : l'ordre d'application change le resultat.
        Function<String, String> exclaim = s -> s + "!";
        Function<String, String> doubled = s -> s + s;
        System.out.println("andThen " + exclaim.andThen(doubled).apply("ok") + ", compose " + exclaim.compose(doubled).apply("ok") + ", identite "
                + Function.<String>identity().apply("meme"));

        // Function<String, Integer> puis Function<Integer, String> : andThen change le type du resultat.
        Function<String, Integer> length = String::length;
        Function<Integer, String> stars = n -> "*".repeat(n);
        Function<String, String> bar = length.andThen(stars);
        BiFunction<String, Integer, String> repeat = (s, n) -> s.repeat(n);
        BinaryOperator<String> longer = (a, b) -> a.length() >= b.length() ? a : b;
        UnaryOperator<String> brackets = s -> "<" + s + ">";
        System.out.println("types : " + bar.apply("lambda") + " " + repeat.andThen(brackets).apply("ab", 3) + " " + longer.apply("java", "kotlin") + " "
                + BinaryOperator.minBy((String a, String b) -> a.length() - b.length()).apply("pomme", "kiwi"));

        // Une lambda ne peut modifier une variable locale : on compte dans un tableau (la REFERENCE reste la meme).
        int[] applied = {0};
        Step counted = s -> {
            applied[0]++;
            return s.strip();
        };
        Step twice = counted.then(counted).then(Step.of("upper"));
        System.out.println("compteur : " + twice.apply("  x  ") + " apres " + applied[0] + " appels");

        Memo memo = new Memo(4);
        Step slow = memo.wrap(Step.parse("trim | squeeze | title | reverse"));
        String[] inputs = {" a  b ", "c d", " a  b ", "c d", " a  b ", "e"};
        StringBuilder results = new StringBuilder("memo :");
        for (String in : inputs) {
            results.append(" [").append(slow.apply(in)).append(']');
        }
        System.out.println(results + " -> " + memo.stats());
    }
}
