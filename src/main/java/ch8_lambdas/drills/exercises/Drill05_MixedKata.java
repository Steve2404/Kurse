package ch8_lambdas.drills.exercises;

import ch8_lambdas.ExerciseChecker;
import ch8_lambdas.drills.Words;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * DRILL 05 - Kata melange : tout le chapitre 8 sans indice de forme
 * =================================================================
 *
 * Mode d'emploi : voir Drill01_LambdaSyntax. Ici, PAS de crochet : a toi
 * de choisir l'interface, la forme de lambda ou la reference. Fais ce
 * drill seulement quand les drills 01 a 04 passent.
 *
 *
 * -- Les TODO --
 *
 * TODO 1  : filter(words, rule)       garder les mots qui passent la regle.
 * TODO 2  : longWords()               les mots de Words.WORDS de plus de 4 lettres -> [lambda, stream, record, sealed].
 * TODO 3  : mapAll(words, f)          appliquer f a chaque mot.
 * TODO 4  : tagAll()                  "#" + mot en majuscules, pour chaque mot de Words.WORDS (compose deux fonctions).
 * TODO 5  : applyTwice(f)             une fonction qui applique f deux fois (UnaryOperator<String>).
 * TODO 6  : lazyLength(words)         un Supplier de la longueur totale, calculee seulement a l'appel.
 * TODO 7  : byLength(words)           une Map longueur -> liste des mots, en utilisant computeIfAbsent.
 * TODO 8  : combiner()                (mot, n) -> le mot repete n fois, separe par "-" : ("ab", 3) -> "ab-ab-ab".
 * TODO 9  : firstMatching(words, p)   le premier mot qui passe p, ou "aucun".
 * TODO 10 : rulesFor(minLength)       un Predicate : longueur >= minLength ET ne contient pas "v".
 */
public class Drill05_MixedKata {

    public static List<String> filter(List<String> words, Predicate<String> rule) {
        throw new UnsupportedOperationException("TODO 1 : implementer filter()");
    }

    public static List<String> longWords() {
        throw new UnsupportedOperationException("TODO 2 : implementer longWords()");
    }

    public static List<String> mapAll(List<String> words, Function<String, String> f) {
        throw new UnsupportedOperationException("TODO 3 : implementer mapAll()");
    }

    public static List<String> tagAll() {
        throw new UnsupportedOperationException("TODO 4 : implementer tagAll()");
    }

    public static UnaryOperator<String> applyTwice(UnaryOperator<String> f) {
        throw new UnsupportedOperationException("TODO 5 : implementer applyTwice()");
    }

    public static Supplier<Integer> lazyLength(List<String> words) {
        throw new UnsupportedOperationException("TODO 6 : implementer lazyLength()");
    }

    public static Map<Integer, List<String>> byLength(List<String> words) {
        throw new UnsupportedOperationException("TODO 7 : implementer byLength()");
    }

    public static BiFunction<String, Integer, String> combiner() {
        throw new UnsupportedOperationException("TODO 8 : implementer combiner()");
    }

    public static String firstMatching(List<String> words, Predicate<String> p) {
        throw new UnsupportedOperationException("TODO 9 : implementer firstMatching()");
    }

    public static Predicate<String> rulesFor(int minLength) {
        throw new UnsupportedOperationException("TODO 10 : implementer rulesFor()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  filter", filter(List.of("a", "bb", "ccc"), s -> s.length() > 1).equals(List.of("bb", "ccc")));
        ExerciseChecker.check("2  longWords", longWords().equals(List.of("lambda", "stream", "record", "sealed")));
        ExerciseChecker.check("3  mapAll", mapAll(List.of("a", "b"), s -> s + s).equals(List.of("aa", "bb")));
        ExerciseChecker.check("4  tagAll", tagAll().equals(List.of("#LAMBDA", "#STREAM", "#JAVA", "#RECORD", "#SEALED", "#VAR")));
        ExerciseChecker.check("5  applyTwice", applyTwice(s -> s + "!").apply("go").equals("go!!"));
        ExerciseChecker.check("6  lazyLength(WORDS) == 31", lazyLength(Words.WORDS).get() == 31);
        Map<Integer, List<String>> groups = byLength(Words.WORDS);
        ExerciseChecker.check("7  byLength : 6 -> [lambda, stream, record, sealed], 3 -> [var]",
                groups.get(6).equals(List.of("lambda", "stream", "record", "sealed")) && groups.get(3).equals(List.of("var")));
        ExerciseChecker.check("8  combiner", combiner().apply("ab", 3).equals("ab-ab-ab"));
        ExerciseChecker.check("9  firstMatching", firstMatching(Words.WORDS, s -> s.startsWith("s")).equals("stream")
                && firstMatching(Words.WORDS, s -> s.startsWith("z")).equals("aucun"));
        ExerciseChecker.check("10 rulesFor(5)", rulesFor(5).test("lambda") && !rulesFor(5).test("java") && !rulesFor(3).test("var"));

        ExerciseChecker.summary();
    }
}
