package ch8_lambdas.drills.exercises;

import ch8_lambdas.ExerciseChecker;
import ch8_lambdas.drills.Words;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * DRILL 01 - Ecrire des lambdas sous toutes les formes permises
 * ============================================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te la fait REPETER jusqu'a
 * ce qu'elle sorte toute seule. Chaque TODO tient en UNE ligne et vise
 * UNE forme precise (entre crochets).
 *
 *   1. Chronometre-toi, note ton temps et ton score dans drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Bloque plus d'une
 *      minute : regarde-la, cache-la, reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (voir REVISION.md).
 *
 * Donnees : ch8_lambdas.drills.Words.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : isShort()        [un parametre SANS parentheses] s -> longueur <= 4.
 * TODO 2  : isShortTyped()   [un parametre AVEC type] (String s) -> ...
 * TODO 3  : isShortVar()     [un parametre avec var] (var s) -> ...
 * TODO 4  : join()           [deux parametres sans type] (a, b) -> a + "-" + b.
 * TODO 5  : joinVar()        [deux parametres var] (var a, var b) -> ...
 * TODO 6  : joinTyped()      [deux parametres types] (String a, String b) -> ...
 * TODO 7  : firstWord()      [aucun parametre] () -> le premier mot de Words.WORDS.
 * TODO 8  : describe()       [corps en bloc avec return] s -> { ... return ...; } : "mot:" + s + "(" + longueur + ")".
 * TODO 9  : withPrefix()     [capture d'une constante] s -> Words.PREFIX + s.
 * TODO 10 : shortCount()     [lambda utilisee dans une boucle] le nombre de mots courts (TODO 1) -> 2.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   s -> ...                       un seul parametre, sans type : parentheses facultatives
 *   (String s) -> ... / (var s) -> ... / (final String s) -> ...
 *   (a, b) / (String a, String b) / (var a, var b)   jamais de melange ; jamais "String s ->" sans ()
 *   () -> ...                      aucun parametre : () obligatoires
 *   Corps expression : pas de return, pas de ;   Corps bloc : { instructions; return valeur; }
 *   Une lambda a besoin d'une CIBLE : une interface fonctionnelle (jamais Object, jamais var)
 * ---------------------------------------------------------------------
 */
public class Drill01_LambdaSyntax {

    public static Predicate<String> isShort() {
        throw new UnsupportedOperationException("TODO 1 : implementer isShort()");
    }

    public static Predicate<String> isShortTyped() {
        throw new UnsupportedOperationException("TODO 2 : implementer isShortTyped()");
    }

    public static Predicate<String> isShortVar() {
        throw new UnsupportedOperationException("TODO 3 : implementer isShortVar()");
    }

    public static BiFunction<String, String, String> join() {
        throw new UnsupportedOperationException("TODO 4 : implementer join()");
    }

    public static BiFunction<String, String, String> joinVar() {
        throw new UnsupportedOperationException("TODO 5 : implementer joinVar()");
    }

    public static BiFunction<String, String, String> joinTyped() {
        throw new UnsupportedOperationException("TODO 6 : implementer joinTyped()");
    }

    public static Supplier<String> firstWord() {
        throw new UnsupportedOperationException("TODO 7 : implementer firstWord()");
    }

    public static Function<String, String> describe() {
        throw new UnsupportedOperationException("TODO 8 : implementer describe()");
    }

    public static Function<String, String> withPrefix() {
        throw new UnsupportedOperationException("TODO 9 : implementer withPrefix()");
    }

    public static int shortCount() {
        throw new UnsupportedOperationException("TODO 10 : implementer shortCount()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  isShort : java oui, lambda non", isShort().test("java") && !isShort().test("lambda"));
        ExerciseChecker.check("2  isShortTyped", isShortTyped().test("var") && !isShortTyped().test("stream"));
        ExerciseChecker.check("3  isShortVar", isShortVar().test("var") && !isShortVar().test("record"));
        ExerciseChecker.check("4  join", join().apply("a", "b").equals("a-b"));
        ExerciseChecker.check("5  joinVar", joinVar().apply("x", "y").equals("x-y"));
        ExerciseChecker.check("6  joinTyped", joinTyped().apply("1", "2").equals("1-2"));
        ExerciseChecker.check("7  firstWord == lambda", firstWord().get().equals("lambda"));
        ExerciseChecker.check("8  describe(\"java\") == mot:java(4)", describe().apply("java").equals("mot:java(4)"));
        ExerciseChecker.check("9  withPrefix(\"java\") == ocp-java", withPrefix().apply("java").equals("ocp-java"));
        ExerciseChecker.check("10 shortCount() == 2", shortCount() == 2);

        ExerciseChecker.summary();
    }
}
