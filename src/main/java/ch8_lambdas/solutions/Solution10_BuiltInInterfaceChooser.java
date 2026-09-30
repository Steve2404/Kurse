package ch8_lambdas.solutions;

import java.time.LocalDate;
import java.util.List;
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
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise10_BuiltInInterfaceChooser.
 */
public class Solution10_BuiltInInterfaceChooser {

    public static String choose(int inputs, String returns) {
        // Deux questions : combien d'entrees (prefixe Bi) et que rend-on.
        if (inputs == 0) {
            return "Supplier";
        }
        String prefix = inputs == 2 ? "Bi" : "";
        return switch (returns) {
            case "nothing" -> prefix + "Consumer";
            case "boolean" -> prefix + "Predicate";
            case "same" -> inputs == 1 ? "UnaryOperator" : "BinaryOperator";
            default -> prefix + "Function";
        };
    }

    public static String methodOf(String interfaceName) {
        // Les operateurs heritent de apply (ce sont des Function/BiFunction).
        return switch (interfaceName) {
            case "Supplier" -> "get";
            case "Consumer", "BiConsumer" -> "accept";
            case "Predicate", "BiPredicate" -> "test";
            default -> "apply";
        };
    }

    public static Supplier<LocalDate> today() {
        // Aucune entree : une reference static suffit.
        return LocalDate::now;
    }

    public static Consumer<String> collector(List<String> list) {
        // Reference liee : list est l'objet, le parametre l'argument.
        return list::add;
    }

    public static BiConsumer<String, Integer> putter(StringBuilder sb) {
        // Deux entrees, rien a rendre ; sb est capture (effectivement final).
        return (key, value) -> sb.append(key).append('=').append(value).append(';');
    }

    public static Predicate<String> longWord() {
        // Une entree, un boolean : Predicate, et sa methode est test.
        return s -> s.length() > 5;
    }

    public static BiPredicate<String, Integer> longerThan() {
        // Deux entrees, un boolean : BiPredicate ; les types des parametres sont deduits de la cible.
        return (s, n) -> s.length() > n;
    }

    public static Function<String, Integer> length() {
        // Function<String, Integer> : le int rendu par length() est emballe en Integer (boxing).
        return String::length;
    }

    public static BiFunction<String, Integer, String> repeat() {
        // Reference non liee a deux parametres : (s, n) -> s.repeat(n).
        return String::repeat;
    }

    public static UnaryOperator<String> trimmer() {
        // Entree et sortie du meme type : UnaryOperator, qui herite de Function (methode apply).
        return String::strip;
    }

    public static BinaryOperator<String> longest() {
        // >= garde le premier en cas d'egalite.
        return (a, b) -> a.length() >= b.length() ? a : b;
    }
}
