package ch8_lambdas.exercises;

import ch8_lambdas.ExerciseChecker;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.ArrayList;
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
 * EXERCICE 10 - Choisir la bonne interface fonctionnelle integree, et connaitre sa methode (niveau : difficile)
 * ===========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * java.util.function offre 9 interfaces de base. On choisit avec deux
 * questions : combien d'entrees ? que rend-on ?
 *
 *   Supplier<T>          0 entree, rend T                     get()
 *   Consumer<T>          1 entree, rend rien                  accept(T)
 *   BiConsumer<T, U>     2 entrees, rend rien                 accept(T, U)
 *   Predicate<T>         1 entree, rend boolean               test(T)
 *   BiPredicate<T, U>    2 entrees, rend boolean              test(T, U)
 *   Function<T, R>       1 entree, rend un autre type R       apply(T)
 *   BiFunction<T, U, R>  2 entrees, rend R                    apply(T, U)
 *   UnaryOperator<T>     1 entree, rend le MEME type          apply(T)
 *   BinaryOperator<T>    2 entrees du meme type, rend ce type apply(T, T)
 *
 * main() verifie les noms de methodes par REFLEXION sur les vraies
 * interfaces du JDK.
 *
 *
 * ==================================================================
 * TODO 1 : choose(inputs, returns)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * returns vaut "nothing", "boolean", "same" (le meme type que les
 * entrees, toutes du meme type) ou "other". On rend le NOM simple.
 *
 * -- Essayons a la main --
 *
 *   (0, "other") -> "Supplier" ; (1, "nothing") -> "Consumer" ; (2, "boolean") -> "BiPredicate"
 *   (1, "same") -> "UnaryOperator" ; (2, "same") -> "BinaryOperator" ; (2, "other") -> "BiFunction"
 *
 * -- Le plan --
 *
 *   1. inputs == 0 -> "Supplier".
 *   2. prefix = inputs == 2 ? "Bi" : "".
 *   3. switch sur returns : nothing -> prefix + "Consumer" ; boolean -> prefix + "Predicate" ;
 *      other -> prefix + "Function" ; same -> inputs == 1 ? "UnaryOperator" : "BinaryOperator".
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : methodOf(interfaceName)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   "Supplier" -> "get" ; "Consumer" -> "accept" ; "Predicate" -> "test" ; "UnaryOperator" -> "apply"
 *
 * -- Le plan --
 *
 *   1. Supplier -> get ; Consumer et BiConsumer -> accept ; Predicate et BiPredicate -> test ; le reste -> apply.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 a TODO 11 : une lambda (ou reference) par interface
 * ==================================================================
 *
 *   TODO 3  : today()            Supplier<LocalDate>                  -> la date du jour (LocalDate::now)
 *   TODO 4  : collector(list)    Consumer<String>                     -> ajoute a list
 *   TODO 5  : putter(sb)         BiConsumer<String, Integer>          -> ajoute "cle=valeur;" a sb
 *   TODO 6  : longWord()         Predicate<String>                    -> longueur > 5
 *   TODO 7  : longerThan()       BiPredicate<String, Integer>         -> s.length() > n
 *   TODO 8  : length()           Function<String, Integer>            -> la longueur
 *   TODO 9  : repeat()           BiFunction<String, Integer, String>  -> s.repeat(n)
 *   TODO 10 : trimmer()          UnaryOperator<String>                -> strip
 *   TODO 11 : longest()          BinaryOperator<String>               -> le plus long des deux (le premier si egalite)
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - UnaryOperator<T> EST une Function<T, T> ; BinaryOperator<T> EST une BiFunction<T, T, T>.
 */
public class Exercise10_BuiltInInterfaceChooser {

    public static String choose(int inputs, String returns) {
        throw new UnsupportedOperationException("TODO 1 : implementer choose()");
    }

    public static String methodOf(String interfaceName) {
        throw new UnsupportedOperationException("TODO 2 : implementer methodOf()");
    }

    public static Supplier<LocalDate> today() {
        throw new UnsupportedOperationException("TODO 3 : implementer today()");
    }

    public static Consumer<String> collector(List<String> list) {
        throw new UnsupportedOperationException("TODO 4 : implementer collector()");
    }

    public static BiConsumer<String, Integer> putter(StringBuilder sb) {
        throw new UnsupportedOperationException("TODO 5 : implementer putter()");
    }

    public static Predicate<String> longWord() {
        throw new UnsupportedOperationException("TODO 6 : implementer longWord()");
    }

    public static BiPredicate<String, Integer> longerThan() {
        throw new UnsupportedOperationException("TODO 7 : implementer longerThan()");
    }

    public static Function<String, Integer> length() {
        throw new UnsupportedOperationException("TODO 8 : implementer length()");
    }

    public static BiFunction<String, Integer, String> repeat() {
        throw new UnsupportedOperationException("TODO 9 : implementer repeat()");
    }

    public static UnaryOperator<String> trimmer() {
        throw new UnsupportedOperationException("TODO 10 : implementer trimmer()");
    }

    public static BinaryOperator<String> longest() {
        throw new UnsupportedOperationException("TODO 11 : implementer longest()");
    }

    public static void main(String[] args) throws Exception {
        String[][] table = {
                {"0", "other", "Supplier"}, {"1", "nothing", "Consumer"}, {"2", "nothing", "BiConsumer"},
                {"1", "boolean", "Predicate"}, {"2", "boolean", "BiPredicate"}, {"1", "other", "Function"},
                {"2", "other", "BiFunction"}, {"1", "same", "UnaryOperator"}, {"2", "same", "BinaryOperator"}};
        int agree = 0;
        for (String[] row : table) {
            if (choose(Integer.parseInt(row[0]), row[1]).equals(row[2])) {
                agree++;
            }
        }
        ExerciseChecker.check("choose() sur les 9 interfaces (" + agree + " d'accord)", agree == 9);

        int methodsOk = 0;
        for (String[] row : table) {
            if (methodOf(row[2]).equals(realAbstractMethod(row[2]))) {
                methodsOk++;
            }
        }
        ExerciseChecker.check("methodOf() == la VRAIE methode (reflexion sur le JDK) sur 9 interfaces (" + methodsOk + ")", methodsOk == 9);

        ExerciseChecker.check("3  today() == LocalDate.now()", today().get().equals(LocalDate.now()));
        List<String> list = new ArrayList<>();
        collector(list).accept("a");
        ExerciseChecker.check("4  collector ajoute a la liste", list.equals(List.of("a")));
        StringBuilder sb = new StringBuilder();
        putter(sb).accept("x", 1);
        putter(sb).accept("y", 2);
        ExerciseChecker.check("5  putter : x=1;y=2;", sb.toString().equals("x=1;y=2;"));
        ExerciseChecker.check("6  longWord : lambdas oui, java non", longWord().test("lambdas") && !longWord().test("java"));
        ExerciseChecker.check("7  longerThan(\"java\", 3) oui", longerThan().test("java", 3) && !longerThan().test("java", 4));
        ExerciseChecker.check("8  length(\"java\") == 4", length().apply("java") == 4);
        ExerciseChecker.check("9  repeat(\"ab\", 3)", repeat().apply("ab", 3).equals("ababab"));
        ExerciseChecker.check("10 trimmer", trimmer().apply("  x  ").equals("x"));
        ExerciseChecker.check("11 longest : kotlin, et java en cas d'egalite",
                longest().apply("java", "kotlin").equals("kotlin") && longest().apply("java", "rust").equals("java"));

        ExerciseChecker.summary();
    }

    // Deja ecrit : le nom de l'unique methode abstraite, lu par reflexion dans java.util.function.
    private static String realAbstractMethod(String simpleName) throws ClassNotFoundException {
        Class<?> type = Class.forName("java.util.function." + simpleName);
        for (Method m : type.getMethods()) {
            if (Modifier.isAbstract(m.getModifiers())) {
                return m.getName();
            }
        }
        return "?";
    }
}
