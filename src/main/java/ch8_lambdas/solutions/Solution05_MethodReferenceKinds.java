package ch8_lambdas.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise05_MethodReferenceKinds.
 */
public class Solution05_MethodReferenceKinds {

    public static Function<String, Integer> parser() {
        // Static : le parametre de la lambda devient l'argument de la methode.
        return Integer::parseInt;
    }

    public static Function<String, String> prefixer(String prefix) {
        // Liee : l'objet (prefix) est fixe a la creation ; le parametre devient l'argument.
        return prefix::concat;
    }

    public static Function<String, String> shouter() {
        // Non liee : le 1er parametre EST l'objet sur lequel on appelle la methode.
        return String::toUpperCase;
    }

    public static Function<String, StringBuilder> builderMaker() {
        // Constructeur : le parametre devient l'argument de new StringBuilder(...).
        return StringBuilder::new;
    }

    public static BiPredicate<String, String> startsWith() {
        // Non liee a 2 parametres : (a, b) -> a.startsWith(b).
        return String::startsWith;
    }

    public static IntFunction<String[]> arrayMaker() {
        // Constructeur de tableau : n -> new String[n].
        return String[]::new;
    }

    public static Supplier<List<String>> listMaker() {
        // Un nouvel objet a chaque get().
        return ArrayList::new;
    }

    public static Predicate<String> isBlank() {
        // Non liee : le String teste devient l'objet sur lequel isBlank() est appele (s -> s.isBlank()).
        return String::isBlank;
    }

    public static String kindOf(String reference, boolean methodIsStatic) {
        // A gauche de :: : une classe (static ou non liee), un objet (liee), ou ::new (constructeur).
        if (reference.endsWith("::new")) {
            return "constructor";
        }
        String left = reference.substring(0, reference.indexOf("::"));
        if (Character.isLowerCase(left.charAt(0)) || left.contains(".")) {
            return "bound";
        }
        return methodIsStatic ? "static" : "unbound";
    }
}
