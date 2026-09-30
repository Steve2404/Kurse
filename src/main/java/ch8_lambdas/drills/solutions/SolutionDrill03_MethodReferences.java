package ch8_lambdas.drills.solutions;

import ch8_lambdas.drills.Words;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.drills.exercises.Drill03_MethodReferences.
 */
public class SolutionDrill03_MethodReferences {

    public static Function<String, Integer> toInt() {
        // Static : x -> Integer.parseInt(x).
        return Integer::parseInt;
    }

    public static Function<Integer, Integer> absolute() {
        // Reference static : Math.abs(int) est choisie, l'Integer est deballe puis le resultat remballe.
        return Math::abs;
    }

    public static Function<String, String> prefixed() {
        // Liee : l'objet Words.PREFIX est fixe.
        return Words.PREFIX::concat;
    }

    public static Predicate<String> inWords() {
        // Reference liee a un objet static : Words.WORDS est evalue une fois, contains a chaque test.
        return Words.WORDS::contains;
    }

    public static Function<String, String> upper() {
        // Non liee : x -> x.toUpperCase().
        return String::toUpperCase;
    }

    public static Predicate<String> isEmpty() {
        // Non liee : String::isEmpty equivaut a s -> s.isEmpty().
        return String::isEmpty;
    }

    public static BiFunction<String, String, Integer> indexOf() {
        // Non liee a 2 parametres : (a, b) -> a.indexOf(b).
        return String::indexOf;
    }

    public static Function<String, StringBuilder> newBuilder() {
        // Constructeur : x -> new StringBuilder(x).
        return StringBuilder::new;
    }

    public static Supplier<List<String>> newList() {
        // Reference de constructeur : Supplier appelle le constructeur sans argument.
        return ArrayList::new;
    }

    public static IntFunction<String[]> newArray() {
        // Constructeur de tableau : n -> new String[n].
        return String[]::new;
    }

    public static Consumer<String> collect(List<String> list) {
        // Reference liee : chaque mot recu est ajoute a la liste fixee a la creation.
        return list::add;
    }
}
