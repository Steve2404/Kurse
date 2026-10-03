package ch13_concurrency.projects.p08_async.solution;

import ch13_concurrency.projects.p08_async.Data;

import java.util.concurrent.RecursiveTask;

/**
 * SOLUTION - le sous-tableau de somme maximale par "diviser pour regner", sur le framework Fork/Join.
 * Chaque moitie rend 4 nombres ; la fusion de deux moities voisines se fait en temps constant.
 */
public class MaxSubarrayTask extends RecursiveTask<MaxSubarrayTask.Summary> {

    // total, meilleur prefixe, meilleur suffixe, meilleur sous-tableau
    public record Summary(long total, long prefix, long suffix, long best) {

        static Summary of(int value) {
            return new Summary(value, value, value, value);
        }

        Summary merge(Summary right) {
            return new Summary(total + right.total, Math.max(prefix, total + right.prefix), Math.max(right.suffix, right.total + suffix),
                    Math.max(Math.max(best, right.best), suffix + right.prefix));
        }
    }

    static final int THRESHOLD = 10_000;

    private final int from;
    private final int to;

    public MaxSubarrayTask(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    protected Summary compute() {
        if (to - from <= THRESHOLD) {                         // assez petit : on calcule directement
            Summary s = Summary.of(Data.delta(from));
            for (int i = from + 1; i < to; i++) {
                s = s.merge(Summary.of(Data.delta(i)));
            }
            return s;
        }
        int mid = (from + to) >>> 1;
        MaxSubarrayTask left = new MaxSubarrayTask(from, mid);
        MaxSubarrayTask right = new MaxSubarrayTask(mid, to);
        left.fork();                                          // la moitie gauche part dans la file du pool...
        Summary r = right.compute();                          // ... ce thread calcule la droite lui-meme ...
        return left.join().merge(r);                          // ... puis attend la gauche (join : pas d'exception verifiee)
    }

    // Kadane, en sequentiel : la reference.
    public static long kadane(int size) {
        long best = Long.MIN_VALUE;
        long current = 0;
        for (int i = 0; i < size; i++) {
            current = Math.max(Data.delta(i), current + Data.delta(i));
            best = Math.max(best, current);
        }
        return best;
    }
}
