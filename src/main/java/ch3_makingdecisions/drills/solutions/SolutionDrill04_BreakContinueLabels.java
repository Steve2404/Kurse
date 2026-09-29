package ch3_makingdecisions.drills.solutions;

import ch3_makingdecisions.drills.Week;
import ch3_makingdecisions.drills.Week.Day;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.drills.exercises.Drill04_BreakContinueLabels.
 */
public class SolutionDrill04_BreakContinueLabels {

    public static int firstNegativeTemp() {
        // break : inutile de lire la suite une fois trouve.
        int found = 0;
        for (int t : Week.TEMPS) {
            if (t < 0) {
                found = t;
                break;
            }
        }
        return found;
    }

    public static int sumPositiveTemps() {
        // continue saute le reste du tour : le += n'est jamais atteint pour t <= 0.
        int sum = 0;
        for (int t : Week.TEMPS) {
            if (t <= 0) {
                continue;
            }
            sum += t;
        }
        return sum;
    }

    public static int daysBeforeFreeze() {
        // On compte AVANT de tester le suivant : le break laisse count au bon nombre.
        int count = 0;
        for (int t : Week.TEMPS) {
            if (t <= 0) {
                break;
            }
            count++;
        }
        return count;
    }

    public static int rowsWithoutNegatives() {
        // continue rows abandonne toute la ligne : le count++ n'est pas atteint.
        int count = 0;
        rows:
        for (int[] row : Week.GRID) {
            for (int v : row) {
                if (v < 0) {
                    continue rows;
                }
            }
            count++;
        }
        return count;
    }

    public static String findInGrid(int target) {
        // break search sort des DEUX boucles d'un coup.
        String result = "absent";
        search:
        for (int r = 0; r < Week.GRID.length; r++) {
            for (int c = 0; c < Week.GRID[r].length; c++) {
                if (Week.GRID[r][c] == target) {
                    result = r + "," + c;
                    break search;
                }
            }
        }
        return result;
    }

    public static int commandsBeforeStop() {
        // Un break simple sortirait seulement du switch : l'etiquette vise la boucle.
        int count = 0;
        loop:
        for (String command : Week.COMMANDS) {
            switch (command) {
                case "stop":
                    break loop;
                default:
                    count++;
            }
        }
        return count;
    }

    public static int firstIndexOver(int limit) {
        // while avec index : on sort par break si trouve, sinon la condition finit la boucle.
        int i = 0;
        int found = -1;
        while (i < Week.TEMPS.length) {
            if (Week.TEMPS[i] > limit) {
                found = i;
                break;
            }
            i++;
        }
        return found;
    }

    public static int cellsBeforeNegativeInner() {
        // break sans etiquette : sort de la boucle interne seulement, la ligne suivante continue.
        int count = 0;
        for (int[] row : Week.GRID) {
            for (int v : row) {
                if (v < 0) {
                    break;
                }
                count++;
            }
        }
        return count;
    }

    public static int cellsBeforeNegativeOuter() {
        // Avec l'etiquette, le premier negatif arrete TOUT.
        int count = 0;
        outer:
        for (int[] row : Week.GRID) {
            for (int v : row) {
                if (v < 0) {
                    break outer;
                }
                count++;
            }
        }
        return count;
    }

    public static Day firstDayAtLeast(int t) {
        // return sort de la methode entiere : ni break ni variable temporaire.
        Day[] days = Day.values();
        for (int i = 0; i < days.length; i++) {
            if (Week.TEMPS[i] >= t) {
                return days[i];
            }
        }
        return null;
    }
}
