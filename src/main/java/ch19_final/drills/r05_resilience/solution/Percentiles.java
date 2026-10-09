package ch19_final.drills.r05_resilience.solution;

import java.util.Arrays;

/** Le percentile p par la methode du rang le plus proche : la valeur de rang ceil(p/100 x n) dans les valeurs triees. */
public final class Percentiles {

    private Percentiles() {
    }

    public static long of(long[] values, double p) {
        if (values.length == 0) {
            throw new IllegalArgumentException("aucune valeur");
        }
        if (p <= 0 || p > 100) {
            throw new IllegalArgumentException("percentile hors de ]0, 100] : " + p);
        }
        long[] sorted = values.clone();
        Arrays.sort(sorted);
        return sorted[(int) Math.ceil(p / 100 * sorted.length) - 1];
    }
}
