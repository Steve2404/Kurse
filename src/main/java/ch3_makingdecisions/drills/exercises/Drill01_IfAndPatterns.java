package ch3_makingdecisions.drills.exercises;

import ch3_makingdecisions.ExerciseChecker;
import ch3_makingdecisions.drills.Week;
import ch3_makingdecisions.drills.Week.Day;

/**
 * DRILL 01 - if/else et pattern matching instanceof (projet meteo)
 * ================================================================
 *
 * -- Comment utiliser un DRILL (different d'un exercice) --
 *
 * Un exercice t'APPREND une notion. Un drill te la fait REPETER jusqu'a
 * ce qu'elle sorte toute seule. Chaque TODO tient en quelques lignes et
 * vise UNE forme precise (entre crochets).
 *
 *   1. Chronometre-toi, note ton temps et ton score dans drills/REVISION.md.
 *   2. Ecris SANS regarder la "carte memoire" en bas. Bloque plus d'une
 *      minute : regarde-la, cache-la, reecris.
 *   3. Refais le MEME drill plus tard, a partir de zero (voir REVISION.md).
 *
 * Donnees : ch3_makingdecisions.drills.Week.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : sign(n)                 [if / else if / else] "negatif", "zero", "positif".
 * TODO 2  : max3(a, b, c)           [if imbriques ou successifs]
 * TODO 3  : isWeekend(day)          [== sur un enum] SAT ou SUN.
 * TODO 4  : lengthIfString(o)       [instanceof String s] longueur, sinon -1.
 * TODO 5  : positiveIntOrZero(o)    [instanceof Integer i && i > 0] i, sinon 0.
 * TODO 6  : countStrings()          [instanceof dans une boucle] sur Week.ITEMS -> 2.
 * TODO 7  : sumIntegers()           [instanceof Integer i] sur Week.ITEMS -> 35.
 * TODO 8  : nullOrType(o)           [null n'est jamais instanceof] "null" ou le nom simple de la classe.
 * TODO 9  : upperIfString(o)        [sortie anticipee sur un test NIE] "" si pas une String.
 * TODO 10 : classifyTemp(t)         [chaine de if] < 0 "gel", < 10 "froid", < 20 "doux", sinon "chaud".
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   if (o instanceof String s) { s existe ici }
 *   if (o instanceof String s && s.length() > 3)  : s utilisable a droite du &&
 *   if (!(o instanceof String s)) return ...;  puis s existe apres
 *   if (!(o instanceof String s)) { } else { s existe ici }
 *   null instanceof X -> false (jamais d'exception)
 *   Un enum se compare avec == (une seule instance par constante)
 * ---------------------------------------------------------------------
 */
public class Drill01_IfAndPatterns {

    public static String sign(int n) {
        throw new UnsupportedOperationException("TODO 1 : implementer sign()");
    }

    public static int max3(int a, int b, int c) {
        throw new UnsupportedOperationException("TODO 2 : implementer max3()");
    }

    public static boolean isWeekend(Day day) {
        throw new UnsupportedOperationException("TODO 3 : implementer isWeekend()");
    }

    public static int lengthIfString(Object o) {
        throw new UnsupportedOperationException("TODO 4 : implementer lengthIfString()");
    }

    public static int positiveIntOrZero(Object o) {
        throw new UnsupportedOperationException("TODO 5 : implementer positiveIntOrZero()");
    }

    public static int countStrings() {
        throw new UnsupportedOperationException("TODO 6 : implementer countStrings()");
    }

    public static int sumIntegers() {
        throw new UnsupportedOperationException("TODO 7 : implementer sumIntegers()");
    }

    public static String nullOrType(Object o) {
        throw new UnsupportedOperationException("TODO 8 : implementer nullOrType()");
    }

    public static String upperIfString(Object o) {
        throw new UnsupportedOperationException("TODO 9 : implementer upperIfString()");
    }

    public static String classifyTemp(int t) {
        throw new UnsupportedOperationException("TODO 10 : implementer classifyTemp()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  sign : -4 negatif, 0 zero, 9 positif",
                sign(-4).equals("negatif") && sign(0).equals("zero") && sign(9).equals("positif"));
        ExerciseChecker.check("2  max3(3, 9, 5) == 9, max3(7, 2, 7) == 7", max3(3, 9, 5) == 9 && max3(7, 2, 7) == 7);
        ExerciseChecker.check("3  isWeekend : SAT oui, WED non", isWeekend(Day.SAT) && !isWeekend(Day.WED));
        ExerciseChecker.check("4  lengthIfString : lundi 5, 42 -1", lengthIfString("lundi") == 5 && lengthIfString(42) == -1);
        ExerciseChecker.check("5  positiveIntOrZero : 42 -> 42, -7 -> 0, x -> 0",
                positiveIntOrZero(42) == 42 && positiveIntOrZero(-7) == 0 && positiveIntOrZero("x") == 0);
        ExerciseChecker.check("6  countStrings() == 2", countStrings() == 2);
        ExerciseChecker.check("7  sumIntegers() == 35", sumIntegers() == 35);
        ExerciseChecker.check("8  nullOrType : null, 3.5 -> Double", nullOrType(null).equals("null") && nullOrType(3.5).equals("Double"));
        ExerciseChecker.check("9  upperIfString : mardi -> MARDI, 42 -> \"\"",
                upperIfString("mardi").equals("MARDI") && upperIfString(42).isEmpty());
        ExerciseChecker.check("10 classifyTemp : -2 gel, 8 froid, 15 doux, 21 chaud",
                classifyTemp(-2).equals("gel") && classifyTemp(8).equals("froid") && classifyTemp(15).equals("doux")
                        && classifyTemp(21).equals("chaud"));

        ExerciseChecker.summary();
    }
}
