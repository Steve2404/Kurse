package ch3_makingdecisions.drills.exercises;

import ch3_makingdecisions.ExerciseChecker;
import ch3_makingdecisions.drills.Week;
import ch3_makingdecisions.drills.Week.Day;

/**
 * DRILL 03 - Les boucles : for, for-each, while, do/while, boucles imbriquees
 * ===========================================================================
 *
 * Mode d'emploi : voir Drill01_IfAndPatterns.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : sumTemps()              [for-each] somme de Week.TEMPS -> 71.
 * TODO 2  : maxTemp()               [for-each + if] -> 21.
 * TODO 3  : indexOfMin()            [for classique avec index] -> 2.
 * TODO 4  : countdown(n)            [while] n=3 -> "3,2,1,0".
 * TODO 5  : doWhileCount(s, limit)  [do/while] compte les tours de do { s++ } while (s < limit).
 *                                    (0, 3) -> 3 ; (10, 5) -> 1 (le corps tourne toujours une fois).
 * TODO 6  : gridSum()               [for-each imbrique] somme de Week.GRID -> 35.
 * TODO 7  : diagonal()              [for avec GRID[i][i]] -> 5.
 * TODO 8  : reverseTemps()          [for a l'envers] -> "17,0,21,8,-2,15,12".
 * TODO 9  : daysAbove(t)            [Day.values() et le meme index] jours dont TEMPS > t.
 *                                    t=15 -> "FRI,SUN".
 * TODO 10 : evenIndexSum()          [for avec i += 2] TEMPS[0] + TEMPS[2] + ... -> 48.
 * TODO 11 : pairsLeftBigger()       [for a deux variables : int i = 0, j = ... ; i < j ; i++, j--]
 *                                    compte les paires ou TEMPS[i] > TEMPS[j] -> 1.
 * TODO 12 : powerOfTwoAtLeast(n)    [while sans compteur] plus petite puissance de 2 >= n. 71 -> 128, 1 -> 1.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   for (int i = 0; i < a.length; i++)          for (int x : a)   (copie, pas d'index)
 *   for (int i = 0, j = n - 1; i < j; i++, j--)  : un seul type pour toutes les variables
 *   while (cond) { }  : 0 tour possible        do { } while (cond);  : au moins 1 tour ; ; final !
 *   for (;;) et while (true) : boucles infinies valides ; while (false) { } -> unreachable statement
 *   int[][] g : g.length lignes, g[i].length colonnes ; for (int[] row : g) for (int v : row)
 *   Day.values() : tableau des constantes dans l'ordre de declaration
 * ---------------------------------------------------------------------
 */
public class Drill03_Loops {

    public static int sumTemps() {
        throw new UnsupportedOperationException("TODO 1 : implementer sumTemps()");
    }

    public static int maxTemp() {
        throw new UnsupportedOperationException("TODO 2 : implementer maxTemp()");
    }

    public static int indexOfMin() {
        throw new UnsupportedOperationException("TODO 3 : implementer indexOfMin()");
    }

    public static String countdown(int n) {
        throw new UnsupportedOperationException("TODO 4 : implementer countdown()");
    }

    public static int doWhileCount(int s, int limit) {
        throw new UnsupportedOperationException("TODO 5 : implementer doWhileCount()");
    }

    public static int gridSum() {
        throw new UnsupportedOperationException("TODO 6 : implementer gridSum()");
    }

    public static int diagonal() {
        throw new UnsupportedOperationException("TODO 7 : implementer diagonal()");
    }

    public static String reverseTemps() {
        throw new UnsupportedOperationException("TODO 8 : implementer reverseTemps()");
    }

    public static String daysAbove(int t) {
        throw new UnsupportedOperationException("TODO 9 : implementer daysAbove()");
    }

    public static int evenIndexSum() {
        throw new UnsupportedOperationException("TODO 10 : implementer evenIndexSum()");
    }

    public static int pairsLeftBigger() {
        throw new UnsupportedOperationException("TODO 11 : implementer pairsLeftBigger()");
    }

    public static int powerOfTwoAtLeast(int n) {
        throw new UnsupportedOperationException("TODO 12 : implementer powerOfTwoAtLeast()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  sumTemps() == 71", sumTemps() == 71);
        ExerciseChecker.check("2  maxTemp() == 21", maxTemp() == 21);
        ExerciseChecker.check("3  indexOfMin() == 2", indexOfMin() == 2);
        ExerciseChecker.check("4  countdown(3) == \"3,2,1,0\"", countdown(3).equals("3,2,1,0"));
        ExerciseChecker.check("5  doWhileCount : (0, 3) -> 3, (10, 5) -> 1", doWhileCount(0, 3) == 3 && doWhileCount(10, 5) == 1);
        ExerciseChecker.check("6  gridSum() == 35", gridSum() == 35);
        ExerciseChecker.check("7  diagonal() == 5", diagonal() == 5);
        ExerciseChecker.check("8  reverseTemps() == \"17,0,21,8,-2,15,12\"", reverseTemps().equals("17,0,21,8,-2,15,12"));
        ExerciseChecker.check("9  daysAbove(15) == \"FRI,SUN\"", daysAbove(15).equals("FRI,SUN"));
        ExerciseChecker.check("10 evenIndexSum() == 48", evenIndexSum() == 48);
        ExerciseChecker.check("11 pairsLeftBigger() == 1", pairsLeftBigger() == 1);
        ExerciseChecker.check("12 powerOfTwoAtLeast : 71 -> 128, 1 -> 1", powerOfTwoAtLeast(71) == 128 && powerOfTwoAtLeast(1) == 1);

        ExerciseChecker.summary();
    }
}
