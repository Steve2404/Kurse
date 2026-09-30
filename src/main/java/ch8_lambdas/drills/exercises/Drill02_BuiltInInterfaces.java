package ch8_lambdas.drills.exercises;

import ch8_lambdas.ExerciseChecker;
import ch8_lambdas.drills.Words;

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
 * DRILL 02 - Les interfaces integrees et leurs methodes pratiques (and, or, negate, andThen, compose, identity)
 * ===========================================================================================================
 *
 * Mode d'emploi : voir Drill01_LambdaSyntax.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : counterSupplier()      [Supplier] rend une NOUVELLE liste vide a chaque get().
 * TODO 2  : adder(list)            [Consumer] ajoute le mot a list.
 * TODO 3  : logger(log)            [Consumer.andThen] ajoute le mot, PUIS le mot en majuscules.
 * TODO 4  : longAndNotVar()        [Predicate.and + negate] longueur > 4 ET pas "var" -> 4 mots.
 * TODO 5  : shortOrJava()          [Predicate.or] longueur <= 3 OU egal a "java".
 * TODO 6  : notBlank()             [Predicate.not (Java 11)] Predicate.not(String::isBlank).
 * TODO 7  : startsWithBi()         [BiPredicate] (mot, prefixe) -> mot.startsWith(prefixe).
 * TODO 8  : lengthThenDouble()     [Function.andThen] longueur puis * 2 -> "java" -> 8.
 * TODO 9  : doubleComposeLength()  [Function.compose] la meme chose ecrite avec compose.
 * TODO 10 : identity()             [Function.identity()] rend l'entree telle quelle.
 * TODO 11 : shout()                [UnaryOperator] toUpperCase + "!".
 * TODO 12 : longer()               [BinaryOperator] le plus long des deux mots (le premier si egalite).
 * TODO 13 : printer(sb)            [BiConsumer] (mot, n) -> ajoute mot + n + ";" a sb.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Supplier<T> get()   Consumer<T> accept(T)   BiConsumer<T,U> accept(T,U)
 *   Predicate<T> test(T)   BiPredicate<T,U> test(T,U)
 *   Function<T,R> apply(T)   BiFunction<T,U,R> apply(T,U)
 *   UnaryOperator<T> apply(T) (T -> T)   BinaryOperator<T> apply(T,T)
 *   Consumer.andThen   Predicate.and/or/negate, Predicate.not(p) (static)
 *   Function.andThen(g) : this puis g ; compose(g) : g puis this ; Function.identity()
 * ---------------------------------------------------------------------
 */
public class Drill02_BuiltInInterfaces {

    public static Supplier<List<String>> counterSupplier() {
        throw new UnsupportedOperationException("TODO 1 : implementer counterSupplier()");
    }

    public static Consumer<String> adder(List<String> list) {
        throw new UnsupportedOperationException("TODO 2 : implementer adder()");
    }

    public static Consumer<String> logger(List<String> log) {
        throw new UnsupportedOperationException("TODO 3 : implementer logger()");
    }

    public static Predicate<String> longAndNotVar() {
        throw new UnsupportedOperationException("TODO 4 : implementer longAndNotVar()");
    }

    public static Predicate<String> shortOrJava() {
        throw new UnsupportedOperationException("TODO 5 : implementer shortOrJava()");
    }

    public static Predicate<String> notBlank() {
        throw new UnsupportedOperationException("TODO 6 : implementer notBlank()");
    }

    public static BiPredicate<String, String> startsWithBi() {
        throw new UnsupportedOperationException("TODO 7 : implementer startsWithBi()");
    }

    public static Function<String, Integer> lengthThenDouble() {
        throw new UnsupportedOperationException("TODO 8 : implementer lengthThenDouble()");
    }

    public static Function<String, Integer> doubleComposeLength() {
        throw new UnsupportedOperationException("TODO 9 : implementer doubleComposeLength()");
    }

    public static Function<String, String> identity() {
        throw new UnsupportedOperationException("TODO 10 : implementer identity()");
    }

    public static UnaryOperator<String> shout() {
        throw new UnsupportedOperationException("TODO 11 : implementer shout()");
    }

    public static BinaryOperator<String> longer() {
        throw new UnsupportedOperationException("TODO 12 : implementer longer()");
    }

    public static BiConsumer<String, Integer> printer(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 13 : implementer printer()");
    }

    public static void main(String[] args) {
        List<String> a = counterSupplier().get();
        a.add("x");
        ExerciseChecker.check("1  counterSupplier : liste neuve a chaque get()", counterSupplier().get().isEmpty() && a.size() == 1);
        List<String> list = new ArrayList<>();
        adder(list).accept("java");
        ExerciseChecker.check("2  adder", list.equals(List.of("java")));
        List<String> log = new ArrayList<>();
        logger(log).accept("var");
        ExerciseChecker.check("3  logger : var puis VAR", log.equals(List.of("var", "VAR")));
        int count = 0;
        for (String w : Words.WORDS) {
            if (longAndNotVar().test(w)) {
                count++;
            }
        }
        ExerciseChecker.check("4  longAndNotVar : 4 mots", count == 4);
        ExerciseChecker.check("5  shortOrJava", shortOrJava().test("var") && shortOrJava().test("java") && !shortOrJava().test("stream"));
        ExerciseChecker.check("6  notBlank", notBlank().test("a") && !notBlank().test("  "));
        ExerciseChecker.check("7  startsWithBi", startsWithBi().test("stream", "str") && !startsWithBi().test("java", "va"));
        ExerciseChecker.check("8  lengthThenDouble(java) == 8", lengthThenDouble().apply("java") == 8);
        ExerciseChecker.check("9  doubleComposeLength(java) == 8", doubleComposeLength().apply("java") == 8);
        ExerciseChecker.check("10 identity", identity().apply("record").equals("record"));
        ExerciseChecker.check("11 shout", shout().apply("java").equals("JAVA!"));
        ExerciseChecker.check("12 longer", longer().apply("java", "stream").equals("stream") && longer().apply("java", "void").equals("java"));
        StringBuilder sb = new StringBuilder();
        printer(sb).accept("a", 1);
        ExerciseChecker.check("13 printer", sb.toString().equals("a1;"));

        ExerciseChecker.summary();
    }
}
