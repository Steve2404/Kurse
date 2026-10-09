package ch19_final.projects.p05_resilience.solution;

import java.time.Duration;
import java.util.Arrays;

/**
 * Les temps de reponse des size derniers appels (une fenetre glissante, dans un tableau circulaire).
 * La MOYENNE ment : 99 appels a 10 ms et 1 a 5 s font une moyenne de 60 ms, que personne n'a vecue.
 * On regarde les PERCENTILES : p95 = 95 % des appels ont ete au moins aussi rapides.
 */
public final class LatencyRecorder {

    private final long[] window;
    private int next;
    private int count;

    public LatencyRecorder(int size) {
        this.window = new long[size];
    }

    public synchronized void record(Duration duration) {
        window[next] = duration.toMillis();
        next = (next + 1) % window.length;
        count = Math.min(count + 1, window.length);
    }

    /** Le percentile p (0 < p <= 100), methode du rang le plus proche : la valeur de rang ceil(p/100 x n). */
    public synchronized long percentile(double p) {
        if (p <= 0 || p > 100) {
            throw new IllegalArgumentException("percentile hors de ]0, 100] : " + p);
        }
        if (count == 0) {
            throw new IllegalStateException("aucune mesure");
        }
        long[] sorted = Arrays.copyOf(window, count);
        Arrays.sort(sorted);
        int rank = (int) Math.ceil(p / 100 * count);
        return sorted[rank - 1];
    }

    public synchronized int count() {
        return count;
    }

    public synchronized String summary() {
        if (count == 0) {
            return "aucune mesure";
        }
        return "n=" + count + " p50=" + percentile(50) + " ms p95=" + percentile(95) + " ms p99=" + percentile(99)
                + " ms max=" + percentile(100) + " ms";
    }
}
