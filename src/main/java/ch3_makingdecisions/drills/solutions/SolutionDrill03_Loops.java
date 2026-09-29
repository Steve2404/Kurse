package ch3_makingdecisions.drills.solutions;

import ch3_makingdecisions.drills.Week;
import ch3_makingdecisions.drills.Week.Day;

/**
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.drills.exercises.Drill03_Loops.
 */
public class SolutionDrill03_Loops {

    public static int sumTemps() {
        // for-each : pas besoin d'index quand on lit juste les valeurs.
        int sum = 0;
        for (int t : Week.TEMPS) {
            sum += t;
        }
        return sum;
    }

    public static int maxTemp() {
        // On part du premier element, pas de 0 (qui serait faux si tout etait negatif).
        int max = Week.TEMPS[0];
        for (int t : Week.TEMPS) {
            if (t > max) {
                max = t;
            }
        }
        return max;
    }

    public static int indexOfMin() {
        // Besoin de la position : for classique.
        int best = 0;
        for (int i = 1; i < Week.TEMPS.length; i++) {
            if (Week.TEMPS[i] < Week.TEMPS[best]) {
                best = i;
            }
        }
        return best;
    }

    public static String countdown(int n) {
        // while : on ne sait pas "combien de tours" a l'avance, on teste la condition.
        StringBuilder sb = new StringBuilder();
        while (n >= 0) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(n);
            n--;
        }
        return sb.toString();
    }

    public static int doWhileCount(int s, int limit) {
        // do/while : le corps s'execute avant le premier test, donc au moins 1 tour.
        int count = 0;
        do {
            s++;
            count++;
        } while (s < limit);
        return count;
    }

    public static int gridSum() {
        // Un tableau 2D est un tableau de lignes : on parcourt les lignes, puis les cases.
        int sum = 0;
        for (int[] row : Week.GRID) {
            for (int v : row) {
                sum += v;
            }
        }
        return sum;
    }

    public static int diagonal() {
        // Meme index pour la ligne et la colonne.
        int sum = 0;
        for (int i = 0; i < Week.GRID.length; i++) {
            sum += Week.GRID[i][i];
        }
        return sum;
    }

    public static String reverseTemps() {
        // Depart a length - 1 (pas length : ArrayIndexOutOfBoundsException) et i >= 0.
        StringBuilder sb = new StringBuilder();
        for (int i = Week.TEMPS.length - 1; i >= 0; i--) {
            sb.append(Week.TEMPS[i]);
            if (i > 0) {
                sb.append(',');
            }
        }
        return sb.toString();
    }

    public static String daysAbove(int t) {
        // Day.values() et TEMPS ont le meme ordre : un seul index sert aux deux.
        Day[] days = Day.values();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < days.length; i++) {
            if (Week.TEMPS[i] > t) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(days[i]);
            }
        }
        return sb.toString();
    }

    public static int evenIndexSum() {
        // L'increment peut etre n'importe quelle expression : ici i += 2.
        int sum = 0;
        for (int i = 0; i < Week.TEMPS.length; i += 2) {
            sum += Week.TEMPS[i];
        }
        return sum;
    }

    public static int pairsLeftBigger() {
        // Deux variables du MEME type dans l'initialisation, deux mises a jour separees par une virgule.
        int count = 0;
        for (int i = 0, j = Week.TEMPS.length - 1; i < j; i++, j--) {
            if (Week.TEMPS[i] > Week.TEMPS[j]) {
                count++;
            }
        }
        return count;
    }

    public static int powerOfTwoAtLeast(int n) {
        // while : on double tant que ce n'est pas assez ; 0 tour si n <= 1.
        int p = 1;
        while (p < n) {
            p *= 2;
        }
        return p;
    }
}
