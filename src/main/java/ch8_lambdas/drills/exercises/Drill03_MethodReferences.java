package ch8_lambdas.drills.exercises;

import ch8_lambdas.ExerciseChecker;
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
 * DRILL 03 - References de methode : static, liee, non liee, constructeur
 * ======================================================================
 *
 * Mode d'emploi : voir Drill01_LambdaSyntax. INTERDIT d'ecrire "->" ici :
 * chaque TODO est une reference de methode (Type::methode).
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : toInt()          [static] Function<String, Integer> : Integer::parseInt.
 * TODO 2  : absolute()       [static] Function<Integer, Integer> : Math::abs.
 * TODO 3  : prefixed()       [liee] Function<String, String> : Words.PREFIX::concat.
 * TODO 4  : inWords()        [liee] Predicate<String> : Words.WORDS::contains.
 * TODO 5  : upper()          [non liee] Function<String, String>.
 * TODO 6  : isEmpty()        [non liee] Predicate<String>.
 * TODO 7  : indexOf()        [non liee a 2 parametres] BiFunction<String, String, Integer> : String::indexOf.
 * TODO 8  : newBuilder()     [constructeur] Function<String, StringBuilder>.
 * TODO 9  : newList()        [constructeur sans argument] Supplier<List<String>>.
 * TODO 10 : newArray()       [constructeur de tableau] IntFunction<String[]>.
 * TODO 11 : collect(list)    [liee sur un parametre] Consumer<String> : list::add.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Classe::static       Integer::parseInt   x -> Integer.parseInt(x)
 *   objet::methode       prefix::concat      x -> prefix.concat(x)        (liee)
 *   Classe::methode      String::length      x -> x.length()              (non liee : 1er param = objet)
 *   Classe::new          StringBuilder::new  x -> new StringBuilder(x)    ; int[]::new : n -> new int[n]
 *   Jamais de parentheses apres le nom : String::length() ne compile pas (';' expected)
 * ---------------------------------------------------------------------
 */
public class Drill03_MethodReferences {

    public static Function<String, Integer> toInt() {
        throw new UnsupportedOperationException("TODO 1 : implementer toInt()");
    }

    public static Function<Integer, Integer> absolute() {
        throw new UnsupportedOperationException("TODO 2 : implementer absolute()");
    }

    public static Function<String, String> prefixed() {
        throw new UnsupportedOperationException("TODO 3 : implementer prefixed()");
    }

    public static Predicate<String> inWords() {
        throw new UnsupportedOperationException("TODO 4 : implementer inWords()");
    }

    public static Function<String, String> upper() {
        throw new UnsupportedOperationException("TODO 5 : implementer upper()");
    }

    public static Predicate<String> isEmpty() {
        throw new UnsupportedOperationException("TODO 6 : implementer isEmpty()");
    }

    public static BiFunction<String, String, Integer> indexOf() {
        throw new UnsupportedOperationException("TODO 7 : implementer indexOf()");
    }

    public static Function<String, StringBuilder> newBuilder() {
        throw new UnsupportedOperationException("TODO 8 : implementer newBuilder()");
    }

    public static Supplier<List<String>> newList() {
        throw new UnsupportedOperationException("TODO 9 : implementer newList()");
    }

    public static IntFunction<String[]> newArray() {
        throw new UnsupportedOperationException("TODO 10 : implementer newArray()");
    }

    public static Consumer<String> collect(List<String> list) {
        throw new UnsupportedOperationException("TODO 11 : implementer collect()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  toInt(\"12\") == 12", toInt().apply("12") == 12);
        ExerciseChecker.check("2  absolute(-5) == 5", absolute().apply(-5) == 5);
        ExerciseChecker.check("3  prefixed(\"java\") == ocp-java", prefixed().apply("java").equals("ocp-java"));
        ExerciseChecker.check("4  inWords : var oui, kotlin non", inWords().test("var") && !inWords().test("kotlin"));
        ExerciseChecker.check("5  upper", upper().apply("java").equals("JAVA"));
        ExerciseChecker.check("6  isEmpty", isEmpty().test("") && !isEmpty().test("x"));
        ExerciseChecker.check("7  indexOf(\"lambda\", \"mb\") == 2", indexOf().apply("lambda", "mb") == 2);
        ExerciseChecker.check("8  newBuilder", newBuilder().apply("ab").append("c").toString().equals("abc"));
        ExerciseChecker.check("9  newList : liste neuve", newList().get().isEmpty());
        ExerciseChecker.check("10 newArray(4).length == 4", newArray().apply(4).length == 4);
        List<String> list = new ArrayList<>();
        collect(list).accept("sealed");
        ExerciseChecker.check("11 collect", list.equals(List.of("sealed")));

        ExerciseChecker.summary();
    }
}
