package ch3_makingdecisions.drills.solutions;

import ch3_makingdecisions.drills.Week;
import ch3_makingdecisions.drills.Week.Day;

/**
 * Corrige du drill 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.drills.exercises.Drill01_IfAndPatterns.
 */
public class SolutionDrill01_IfAndPatterns {

    public static String sign(int n) {
        // Une seule branche s'execute ; le else final couvre tout le reste.
        if (n < 0) {
            return "negatif";
        } else if (n == 0) {
            return "zero";
        } else {
            return "positif";
        }
    }

    public static int max3(int a, int b, int c) {
        // On garde le meilleur au fur et a mesure.
        int max = a;
        if (b > max) {
            max = b;
        }
        if (c > max) {
            max = c;
        }
        return max;
    }

    public static boolean isWeekend(Day day) {
        // Chaque constante d'enum est unique : == suffit.
        return day == Day.SAT || day == Day.SUN;
    }

    public static int lengthIfString(Object o) {
        // s n'existe que dans la branche ou le test a reussi.
        if (o instanceof String s) {
            return s.length();
        }
        return -1;
    }

    public static int positiveIntOrZero(Object o) {
        // La condition supplementaire se met apres && : i y est deja disponible.
        if (o instanceof Integer i && i > 0) {
            return i;
        }
        return 0;
    }

    public static int countStrings() {
        // null instanceof String est false : pas besoin de le filtrer a part.
        int count = 0;
        for (Object item : Week.ITEMS) {
            if (item instanceof String) {
                count++;
            }
        }
        return count;
    }

    public static int sumIntegers() {
        // 42 + (-7) : seuls les Integer comptent (3.5 est un Double).
        int sum = 0;
        for (Object item : Week.ITEMS) {
            if (item instanceof Integer i) {
                sum += i;
            }
        }
        return sum;
    }

    public static String nullOrType(Object o) {
        // o.getClass() sur null lancerait NullPointerException : on teste null d'abord.
        return o == null ? "null" : o.getClass().getSimpleName();
    }

    public static String upperIfString(Object o) {
        // Sortie anticipee : apres le return, s est utilisable.
        if (!(o instanceof String s)) {
            return "";
        }
        return s.toUpperCase();
    }

    public static String classifyTemp(int t) {
        // Du plus petit seuil au plus grand : le premier test vrai gagne.
        if (t < 0) {
            return "gel";
        } else if (t < 10) {
            return "froid";
        } else if (t < 20) {
            return "doux";
        }
        return "chaud";
    }
}
