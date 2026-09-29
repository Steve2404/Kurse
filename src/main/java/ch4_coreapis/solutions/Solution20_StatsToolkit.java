package ch4_coreapis.solutions;

import java.util.Arrays;

/**
 * Corrige de l'exercice 20. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise20_StatsToolkit.
 */
public class Solution20_StatsToolkit {

    public static double mean(int... values) {
        // Cast en double AVANT la division, sinon 3 / 2 == 1.
        int sum = 0;
        for (int v : values) {
            sum += v;
        }
        return (double) sum / values.length;
    }

    public static double median(int... values) {
        // Trier une COPIE : Arrays.sort modifierait le tableau de l'appelant.
        int[] sorted = Arrays.copyOf(values, values.length);
        Arrays.sort(sorted);
        int n = sorted.length;
        if (n % 2 == 1) {
            return sorted[n / 2];
        }
        return (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0;
    }

    public static double stdDev(int... values) {
        // Math.pow et Math.sqrt travaillent en double.
        double m = mean(values);
        double sum = 0;
        for (int v : values) {
            sum += Math.pow(v - m, 2);
        }
        return Math.sqrt(sum / values.length);
    }

    public static int percent(int part, int total) {
        // 100.0 force le calcul en double ; Math.round(double) rend un long, d'ou le cast.
        return (int) Math.round(100.0 * part / total);
    }

    public static String bar(int value, int max, int width) {
        // Division par (double) max pour garder la partie decimale avant l'arrondi.
        int n = (int) Math.round(value * width / (double) max);
        return "#".repeat(n);
    }

    public static String histogram(int... values) {
        // %3d aligne les nombres a droite ; le "\n" seulement entre deux lignes.
        int max = values[0];
        for (int v : values) {
            max = Math.max(max, v);
        }
        StringBuilder sb = new StringBuilder();
        for (int v : values) {
            if (sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(String.format("%3d | ", v)).append(bar(v, max, 10));
        }
        return sb.toString();
    }
}
