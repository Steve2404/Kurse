package ch8_lambdas.drills.solutions;

import ch8_lambdas.drills.Words;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.drills.exercises.Drill01_LambdaSyntax.
 */
public class SolutionDrill01_LambdaSyntax {

    public static Predicate<String> isShort() {
        // Un seul parametre sans type : les parentheses sont facultatives.
        return s -> s.length() <= 4;
    }

    public static Predicate<String> isShortTyped() {
        // Avec un type, les parentheses deviennent obligatoires.
        return (String s) -> s.length() <= 4;
    }

    public static Predicate<String> isShortVar() {
        // var aussi exige des parentheses.
        return (var s) -> s.length() <= 4;
    }

    public static BiFunction<String, String, String> join() {
        // Deux parametres : les parentheses sont obligatoires, les types sont deduits.
        return (a, b) -> a + "-" + b;
    }

    public static BiFunction<String, String, String> joinVar() {
        // Tous en var : jamais de melange avec un type ou un nom seul.
        return (var a, var b) -> a + "-" + b;
    }

    public static BiFunction<String, String, String> joinTyped() {
        // Si un parametre a un type, tous doivent en avoir un (pas de melange type / sans type).
        return (String a, String b) -> a + "-" + b;
    }

    public static Supplier<String> firstWord() {
        // Aucun parametre : () obligatoires.
        return () -> Words.WORDS.get(0);
    }

    public static Function<String, String> describe() {
        // Corps en bloc : instructions completes et return explicite.
        return s -> {
            int length = s.length();
            return "mot:" + s + "(" + length + ")";
        };
    }

    public static Function<String, String> withPrefix() {
        // Une constante static se lit librement dans une lambda.
        return s -> Words.PREFIX + s;
    }

    public static int shortCount() {
        // On reutilise la lambda du TODO 1 comme n'importe quel objet.
        Predicate<String> shortWord = isShort();
        int count = 0;
        for (String word : Words.WORDS) {
            if (shortWord.test(word)) {
                count++;
            }
        }
        return count;
    }
}
