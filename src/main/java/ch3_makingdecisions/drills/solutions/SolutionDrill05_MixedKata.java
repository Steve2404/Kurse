package ch3_makingdecisions.drills.solutions;

import ch3_makingdecisions.drills.Week;
import ch3_makingdecisions.drills.Week.Day;

/**
 * Corrige du drill 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch3_makingdecisions.drills.exercises.Drill05_MixedKata.
 */
public class SolutionDrill05_MixedKata {

    public static String weekReport() {
        // Une chaine de if pour classer (des intervalles, pas des constantes : switch impossible).
        int gel = 0;
        int froid = 0;
        int doux = 0;
        int chaud = 0;
        for (int t : Week.TEMPS) {
            if (t < 0) {
                gel++;
            } else if (t < 10) {
                froid++;
            } else if (t < 20) {
                doux++;
            } else {
                chaud++;
            }
        }
        return "gel=" + gel + " froid=" + froid + " doux=" + doux + " chaud=" + chaud;
    }

    public static String describeItems() {
        // null d'abord : ensuite chaque instanceof declare sa propre variable typee.
        StringBuilder sb = new StringBuilder();
        for (Object item : Week.ITEMS) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            if (item == null) {
                sb.append("null");
            } else if (item instanceof Integer i) {
                sb.append("int:").append(i);
            } else if (item instanceof String s) {
                sb.append("str:").append(s.length());
            } else {
                sb.append("autre");
            }
        }
        return sb.toString();
    }

    public static String finalState() {
        // Machine a etats : un switch expression calcule le nouvel etat a chaque commande.
        String state = "OFF";
        for (String command : Week.COMMANDS) {
            String current = state;
            state = switch (command) {
                case "start" -> "ON";
                case "pause" -> current.equals("ON") ? "PAUSED" : current;
                case "stop" -> "OFF";
                default -> current;
            };
        }
        return state;
    }

    public static int weekendAverage() {
        // switch sur l'enum pour choisir les jours ; division entiere a la fin.
        int sum = 0;
        int count = 0;
        Day[] days = Day.values();
        for (int i = 0; i < days.length; i++) {
            switch (days[i]) {
                case SAT, SUN -> {
                    sum += Week.TEMPS[i];
                    count++;
                }
                default -> {
                }
            }
        }
        return sum / count;
    }

    public static int longestWarmStreak() {
        // Compteur courant remis a 0 a chaque coupure ; on retient le meilleur.
        int best = 0;
        int current = 0;
        for (int t : Week.TEMPS) {
            if (t > 10) {
                current++;
                if (current > best) {
                    best = current;
                }
            } else {
                current = 0;
            }
        }
        return best;
    }

    public static String rowMaxes() {
        // Boucle externe par ligne, le max repart de la premiere case de CHAQUE ligne.
        StringBuilder sb = new StringBuilder();
        for (int[] row : Week.GRID) {
            int max = row[0];
            for (int v : row) {
                if (v > max) {
                    max = v;
                }
            }
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(max);
        }
        return sb.toString();
    }

    public static String columnSums() {
        // Colonnes : on inverse l'ordre des boucles (colonne dehors, ligne dedans) ; besoin d'index.
        StringBuilder sb = new StringBuilder();
        for (int c = 0; c < Week.GRID[0].length; c++) {
            int sum = 0;
            for (int r = 0; r < Week.GRID.length; r++) {
                sum += Week.GRID[r][c];
            }
            if (c > 0) {
                sb.append(',');
            }
            sb.append(sum);
        }
        return sb.toString();
    }

    public static int firstRepeatIndex() {
        // La boucle interne regarde en arriere ; return sort des deux boucles d'un coup.
        for (int i = 1; i < Week.COMMANDS.length; i++) {
            for (int j = 0; j < i; j++) {
                if (Week.COMMANDS[i].equals(Week.COMMANDS[j])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public static int daysWithinBudget(int max) {
        // On ajoute d'abord, on teste ensuite : le jour qui depasse n'est pas compte.
        int sum = 0;
        int days = 0;
        for (int t : Week.TEMPS) {
            sum += t;
            if (sum > max) {
                break;
            }
            days++;
        }
        return days;
    }

    public static Day hottestDay() {
        // On garde l'index du max, puis on le convertit en Day.
        int best = 0;
        for (int i = 1; i < Week.TEMPS.length; i++) {
            if (Week.TEMPS[i] > Week.TEMPS[best]) {
                best = i;
            }
        }
        return Day.values()[best];
    }

    public static String dayCodes() {
        // switch expression qui rend un char : exhaustif grace au default.
        StringBuilder sb = new StringBuilder();
        for (Day day : Day.values()) {
            char code = switch (day) {
                case SAT, SUN -> 'W';
                default -> 'S';
            };
            sb.append(code);
        }
        return sb.toString();
    }

    public static String weekSummary() {
        // Un seul parcours pour le max, le min et la somme.
        int hot = 0;
        int cold = 0;
        int sum = 0;
        for (int i = 0; i < Week.TEMPS.length; i++) {
            if (Week.TEMPS[i] > Week.TEMPS[hot]) {
                hot = i;
            }
            if (Week.TEMPS[i] < Week.TEMPS[cold]) {
                cold = i;
            }
            sum += Week.TEMPS[i];
        }
        Day[] days = Day.values();
        return days[hot] + " " + Week.TEMPS[hot] + " / " + days[cold] + " " + Week.TEMPS[cold]
                + " / moyenne " + sum / Week.TEMPS.length;
    }
}
