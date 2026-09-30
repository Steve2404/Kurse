package ch8_lambdas.exercises;

import ch8_lambdas.ExerciseChecker;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.ToIntFunction;

/**
 * EXERCICE 12 - Un bulletin de notes avec les interfaces fonctionnelles PRIMITIVES (sans boxing) (niveau : avance)
 * =============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Function<Integer, Integer> range chaque int dans une boite Integer :
 * c'est lent et un null peut exploser. Les interfaces primitives
 * travaillent directement sur int, long, double, boolean. Leurs noms
 * suivent une grammaire :
 *
 *   IntPredicate            int -> boolean          test(int)
 *   IntUnaryOperator        int -> int              applyAsInt(int)
 *   IntBinaryOperator       int, int -> int         applyAsInt(int, int)
 *   IntFunction<R>          int -> R                apply(int)
 *   ToIntFunction<T>        T -> int                applyAsInt(T)
 *   IntSupplier             () -> int               getAsInt()
 *   BooleanSupplier         () -> boolean           getAsBoolean()
 *   ObjIntConsumer<T>       T, int -> rien          accept(T, int)
 *   DoubleUnaryOperator     double -> double        applyAsDouble(double)
 *
 * Regle : "ToXxx" = on RENDS un xxx ; "Xxx" au debut = on RECOIT un xxx ;
 * la methode s'appelle applyAsXxx / getAsXxx quand elle rend un primitif.
 *
 *
 * ==================================================================
 * TODO 1 : methodOf(interfaceName)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. finit par "Predicate" -> "test" ; finit par "Consumer" -> "accept" ;
 *      "IntSupplier" -> "getAsInt" ; "BooleanSupplier" -> "getAsBoolean" ;
 *      "IntFunction" -> "apply" ; "DoubleUnaryOperator" -> "applyAsDouble" ;
 *      sinon (IntUnaryOperator, IntBinaryOperator, ToIntFunction) -> "applyAsInt".
 *   main() compare avec la VRAIE methode, lue par reflexion.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 a TODO 9 : le bulletin
 * ==================================================================
 *
 *   TODO 2 : passing()        IntPredicate            note >= 10
 *   TODO 3 : curve()          IntUnaryOperator        +2 points, sans depasser 20 (Math.min)
 *   TODO 4 : sum()            IntBinaryOperator       a + b
 *   TODO 5 : mention()        IntFunction<String>     >= 16 "TB", >= 14 "B", >= 12 "AB", >= 10 "P", sinon "AJ"
 *   TODO 6 : nameLength()     ToIntFunction<String>   la longueur d'un nom
 *   TODO 7 : counter()        IntSupplier             1, 2, 3... a chaque appel (un compteur dans un tableau d'une case)
 *   TODO 8 : appendScore()    ObjIntConsumer<StringBuilder>   ajoute la note puis ";"
 *   TODO 9 : toPercent()      DoubleUnaryOperator     note sur 20 -> pourcentage (x * 5)
 *
 *
 * ==================================================================
 * TODO 10 : report(scores)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   {8, 15, 19, 11} -> apres curve : {10, 17, 20, 13}
 *   -> "admis=4 total=60 mentions=P,TB,TB,AB"
 *
 * -- Le plan --
 *
 *   1. Pour chaque note : c = curve().applyAsInt(note) ; compter si passing().test(c) ;
 *      total = sum().applyAsInt(total, c) ; ajouter mention().apply(c).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : les TODO 2 a 5.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Un IntSupplier qui compte : int[] box = {0}; return () -> ++box[0];
 */
public class Exercise12_PrimitiveGradebook {

    public static String methodOf(String interfaceName) {
        throw new UnsupportedOperationException("TODO 1 : implementer methodOf()");
    }

    public static IntPredicate passing() {
        throw new UnsupportedOperationException("TODO 2 : implementer passing()");
    }

    public static IntUnaryOperator curve() {
        throw new UnsupportedOperationException("TODO 3 : implementer curve()");
    }

    public static IntBinaryOperator sum() {
        throw new UnsupportedOperationException("TODO 4 : implementer sum()");
    }

    public static IntFunction<String> mention() {
        throw new UnsupportedOperationException("TODO 5 : implementer mention()");
    }

    public static ToIntFunction<String> nameLength() {
        throw new UnsupportedOperationException("TODO 6 : implementer nameLength()");
    }

    public static IntSupplier counter() {
        throw new UnsupportedOperationException("TODO 7 : implementer counter()");
    }

    public static ObjIntConsumer<StringBuilder> appendScore() {
        throw new UnsupportedOperationException("TODO 8 : implementer appendScore()");
    }

    public static DoubleUnaryOperator toPercent() {
        throw new UnsupportedOperationException("TODO 9 : implementer toPercent()");
    }

    public static String report(int... scores) {
        throw new UnsupportedOperationException("TODO 10 : implementer report()");
    }

    public static void main(String[] args) throws Exception {
        String[] names = {"IntPredicate", "IntUnaryOperator", "IntBinaryOperator", "IntFunction", "ToIntFunction",
                "IntSupplier", "BooleanSupplier", "ObjIntConsumer", "DoubleUnaryOperator"};
        int agree = 0;
        for (String name : names) {
            if (methodOf(name).equals(realAbstractMethod(name))) {
                agree++;
            }
        }
        ExerciseChecker.check("methodOf() == la VRAIE methode (reflexion) sur 9 interfaces (" + agree + ")", agree == 9);
        ExerciseChecker.check("passing : 10 oui, 9 non", passing().test(10) && !passing().test(9));
        ExerciseChecker.check("curve : 8 -> 10, 19 -> 20", curve().applyAsInt(8) == 10 && curve().applyAsInt(19) == 20);
        ExerciseChecker.check("sum(3, 4) == 7", sum().applyAsInt(3, 4) == 7);
        ExerciseChecker.check("mention : 17 TB, 14 B, 12 AB, 10 P, 9 AJ",
                mention().apply(17).equals("TB") && mention().apply(14).equals("B") && mention().apply(12).equals("AB")
                        && mention().apply(10).equals("P") && mention().apply(9).equals("AJ"));
        ExerciseChecker.check("nameLength(\"Ada\") == 3", nameLength().applyAsInt("Ada") == 3);
        IntSupplier c = counter();
        ExerciseChecker.check("counter : 1, 2, 3", c.getAsInt() == 1 && c.getAsInt() == 2 && c.getAsInt() == 3);
        StringBuilder sb = new StringBuilder();
        appendScore().accept(sb, 12);
        appendScore().accept(sb, 7);
        ExerciseChecker.check("appendScore : 12;7;", sb.toString().equals("12;7;"));
        ExerciseChecker.check("toPercent(15) == 75.0", toPercent().applyAsDouble(15) == 75.0);
        BooleanSupplier ready = () -> true;
        ExerciseChecker.check("(rappel) BooleanSupplier.getAsBoolean()", ready.getAsBoolean());
        ExerciseChecker.check("report(8, 15, 19, 11)", report(8, 15, 19, 11).equals("admis=4 total=60 mentions=P,TB,TB,AB"));

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
