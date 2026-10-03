package ch13_concurrency.projects.p02_primes.solution;

import java.util.concurrent.Callable;

/**
 * SOLUTION - une tache qui RENVOIE un resultat : Callable (Runnable ne peut rien renvoyer ni lever d'exception verifiee).
 * Crible segmente : on raye, dans [from, to), les multiples des premiers de base (tous les premiers <= racine(to)).
 */
public class SieveTask implements Callable<Segment> {

    private final int from;
    private final int to;
    private final int[] basePrimes;

    public SieveTask(int from, int to, int[] basePrimes) {
        this.from = from;
        this.to = to;
        this.basePrimes = basePrimes;
    }

    // Les premiers jusqu'a n, par le crible d'Eratosthene classique.
    public static int[] primesUpTo(int n) {
        boolean[] composite = new boolean[n + 1];
        int count = 0;
        for (int i = 2; i <= n; i++) {
            if (!composite[i]) {
                count++;
                for (long j = (long) i * i; j <= n; j += i) {
                    composite[(int) j] = true;
                }
            }
        }
        int[] primes = new int[count];
        for (int i = 2, k = 0; i <= n; i++) {
            if (!composite[i]) {
                primes[k++] = i;
            }
        }
        return primes;
    }

    @Override
    public Segment call() {
        boolean[] composite = new boolean[to - from];
        for (int p : basePrimes) {
            if ((long) p * p >= to) {
                break;
            }
            long start = Math.max((long) p * p, (from + p - 1) / p * (long) p);
            for (long j = start; j < to; j += p) {
                composite[(int) (j - from)] = true;
            }
        }
        int count = 0;
        int first = -1;
        int last = -1;
        int maxGap = 0;
        for (int i = 0; i < composite.length; i++) {
            if (!composite[i] && from + i >= 2) {
                int n = from + i;
                count++;
                if (first < 0) {
                    first = n;
                } else {
                    maxGap = Math.max(maxGap, n - last);
                }
                last = n;
            }
        }
        return new Segment(from, to, count, first, last, maxGap);
    }
}
