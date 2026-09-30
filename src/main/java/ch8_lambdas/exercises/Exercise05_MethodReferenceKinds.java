package ch8_lambdas.exercises;

import ch8_lambdas.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * EXERCICE 5 - Les 4 sortes de references de methode, a ecrire a la place de lambdas (niveau : difficile)
 * ======================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une reference de methode (Type::methode) est une lambda raccourcie,
 * quand la lambda ne fait QU'appeler une methode existante. 4 sortes :
 *
 *   static                 : Integer::parseInt        = s -> Integer.parseInt(s)
 *   instance LIEE          : prefix::concat           = s -> prefix.concat(s)       (l'objet est deja choisi)
 *   instance NON LIEE      : String::toUpperCase      = s -> s.toUpperCase()        (l'objet est le 1er parametre)
 *   constructeur           : StringBuilder::new       = s -> new StringBuilder(s)
 *
 * -- Verdicts reels de javac 17 --
 *
 *   Supplier<Integer> f = String::length;     -> error: incompatible types: invalid method reference (il manque le String)
 *   Function<String,Integer> f = String::length(); -> error: ';' expected (jamais de parentheses)
 *   Function<String,Integer> f = "x"::parseInt;    -> error: invalid method reference (static via un objet)
 *   BiFunction<String,String,Boolean> f = String::startsWith -> compile (1er parametre = l'objet, 2e = l'argument)
 *   IntFunction<int[]> f = int[]::new         -> compile (constructeur de tableau)
 *   Consumer<String> c = System.out::println  -> compile (instance liee : System.out est choisi)
 *
 *
 * ==================================================================
 * TODO 1 a TODO 6 : ecrire une REFERENCE DE METHODE (jamais de ->)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   TODO 1 : parser()          Function<String, Integer>          -> Integer::parseInt            (static)
 *   TODO 2 : prefixer(prefix)  Function<String, String>           -> prefix::concat               (liee)
 *   TODO 3 : shouter()         Function<String, String>           -> String::toUpperCase          (non liee)
 *   TODO 4 : builderMaker()    Function<String, StringBuilder>    -> StringBuilder::new           (constructeur)
 *   TODO 5 : startsWith()      BiPredicate<String, String>        -> String::startsWith           (non liee, 2 parametres)
 *   TODO 6 : arrayMaker()      IntFunction<String[]>              -> String[]::new                (constructeur de tableau)
 *   TODO 7 : listMaker()       Supplier<List<String>>             -> ArrayList::new
 *   TODO 8 : isBlank()         Predicate<String>                  -> String::isBlank
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une ligne chacune.
 *
 *
 * ==================================================================
 * TODO 9 : kindOf(reference, methodIsStatic)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Classer une reference ecrite en texte. On te dit si la methode est
 * static (le texte seul ne suffit pas : Integer::parseInt et
 * String::length se ressemblent).
 *
 * -- Essayons a la main --
 *
 *   ("StringBuilder::new", false) -> "constructor" ; ("int[]::new", false) -> "constructor"
 *   ("prefix::concat", false)     -> "bound"  (a gauche une VARIABLE : commence par une minuscule)
 *   ("this::up", false)           -> "bound" ; ("System.out::println", false) -> "bound" (un champ, donc un objet)
 *   ("Integer::parseInt", true)   -> "static" ; ("String::length", false) -> "unbound"
 *
 * -- Le plan --
 *
 *   1. Finit par "::new" -> "constructor".
 *   2. left = la partie avant "::" ; si left commence par une minuscule, ou contient un "." -> "bound".
 *   3. Sinon methodIsStatic ? "static" : "unbound".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Character.isLowerCase(left.charAt(0)).
 */
public class Exercise05_MethodReferenceKinds {

    public static Function<String, Integer> parser() {
        throw new UnsupportedOperationException("TODO 1 : implementer parser()");
    }

    public static Function<String, String> prefixer(String prefix) {
        throw new UnsupportedOperationException("TODO 2 : implementer prefixer()");
    }

    public static Function<String, String> shouter() {
        throw new UnsupportedOperationException("TODO 3 : implementer shouter()");
    }

    public static Function<String, StringBuilder> builderMaker() {
        throw new UnsupportedOperationException("TODO 4 : implementer builderMaker()");
    }

    public static BiPredicate<String, String> startsWith() {
        throw new UnsupportedOperationException("TODO 5 : implementer startsWith()");
    }

    public static IntFunction<String[]> arrayMaker() {
        throw new UnsupportedOperationException("TODO 6 : implementer arrayMaker()");
    }

    public static Supplier<List<String>> listMaker() {
        throw new UnsupportedOperationException("TODO 7 : implementer listMaker()");
    }

    public static Predicate<String> isBlank() {
        throw new UnsupportedOperationException("TODO 8 : implementer isBlank()");
    }

    public static String kindOf(String reference, boolean methodIsStatic) {
        throw new UnsupportedOperationException("TODO 9 : implementer kindOf()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  parser : \"42\" -> 42", parser().apply("42") == 42);
        ExerciseChecker.check("2  prefixer(\"Dr \") : Ada -> Dr Ada", prefixer("Dr ").apply("Ada").equals("Dr Ada"));
        ExerciseChecker.check("3  shouter : java -> JAVA", shouter().apply("java").equals("JAVA"));
        ExerciseChecker.check("4  builderMaker : un StringBuilder qui contient le texte", builderMaker().apply("ab").reverse().toString().equals("ba"));
        ExerciseChecker.check("5  startsWith : (\"javadoc\", \"java\") oui", startsWith().test("javadoc", "java") && !startsWith().test("java", "javadoc"));
        ExerciseChecker.check("6  arrayMaker(3) : un String[3]", arrayMaker().apply(3).length == 3);
        List<String> list = listMaker().get();
        list.add("x");
        ExerciseChecker.check("7  listMaker : une liste NEUVE et modifiable a chaque appel", list.size() == 1 && listMaker().get().isEmpty());
        ExerciseChecker.check("8  isBlank : \"  \" oui, \"a\" non", isBlank().test("  ") && !isBlank().test("a"));
        ExerciseChecker.check("9  kindOf : 7 cas",
                kindOf("StringBuilder::new", false).equals("constructor") && kindOf("int[]::new", false).equals("constructor")
                        && kindOf("prefix::concat", false).equals("bound") && kindOf("this::up", false).equals("bound")
                        && kindOf("System.out::println", false).equals("bound")
                        && kindOf("Integer::parseInt", true).equals("static") && kindOf("String::length", false).equals("unbound"));

        ExerciseChecker.summary();
    }
}
