package ch8_lambdas.drills.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.drills.exercises.Drill02_BuiltInInterfaces.
 */
public class SolutionDrill02_BuiltInInterfaces {

    public static Supplier<List<String>> counterSupplier() {
        // Un Supplier peut fabriquer un nouvel objet a chaque appel.
        return ArrayList::new;
    }

    public static Consumer<String> adder(List<String> list) {
        // Reference liee : list est fixee maintenant, add sera appele plus tard. La valeur rendue par add est ignoree.
        return list::add;
    }

    public static Consumer<String> logger(List<String> log) {
        // andThen : le premier Consumer, puis le second, avec la meme entree.
        Consumer<String> first = log::add;
        return first.andThen(s -> log.add(s.toUpperCase()));
    }

    public static Predicate<String> longAndNotVar() {
        // negate() inverse une regle, and() les combine en court-circuit.
        Predicate<String> isLong = s -> s.length() > 4;
        Predicate<String> isVar = s -> s.equals("var");
        return isLong.and(isVar.negate());
    }

    public static Predicate<String> shortOrJava() {
        // or() n'evalue la 2e regle que si la 1re est fausse.
        Predicate<String> isShort = s -> s.length() <= 3;
        return isShort.or(s -> s.equals("java"));
    }

    public static Predicate<String> notBlank() {
        // Predicate.not (static, Java 11) rend une reference de methode negatable.
        return Predicate.not(String::isBlank);
    }

    public static BiPredicate<String, String> startsWithBi() {
        // Non liee a deux parametres : le 1er devient l'objet, le 2e l'argument (a.startsWith(b)).
        return String::startsWith;
    }

    public static Function<String, Integer> lengthThenDouble() {
        // andThen : d'abord length, puis * 2.
        Function<String, Integer> length = String::length;
        return length.andThen(n -> n * 2);
    }

    public static Function<String, Integer> doubleComposeLength() {
        // compose : l'argument de compose passe EN PREMIER.
        Function<Integer, Integer> twice = n -> n * 2;
        return twice.compose(String::length);
    }

    public static Function<String, String> identity() {
        // Function.identity() rend son entree telle quelle : utile comme point de depart d'un andThen.
        return Function.identity();
    }

    public static UnaryOperator<String> shout() {
        // Meme type en entree et en sortie.
        return s -> s.toUpperCase() + "!";
    }

    public static BinaryOperator<String> longer() {
        // BinaryOperator : deux entrees et une sortie du meme type ; >= garde le premier en cas d'egalite.
        return (a, b) -> a.length() >= b.length() ? a : b;
    }

    public static BiConsumer<String, Integer> printer(StringBuilder sb) {
        // BiConsumer ne rend rien : son seul effet est d'ecrire dans sb, qui est capture.
        return (word, n) -> sb.append(word).append(n).append(';');
    }
}
