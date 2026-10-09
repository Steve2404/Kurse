package ch19_final.projects.p06_performance.solution;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Un petit banc de mesure, avec les trois precautions qui comptent :
 *   - CHAUFFER : les premiers tours sont interpretes, puis le compilateur JIT optimise le code ; on jette ces tours ;
 *   - REPETER et prendre la MEDIANE : un tour peut etre ralenti par le ramasse-miettes ou un autre programme ;
 *   - UTILISER le resultat : un calcul dont personne ne lit le resultat peut etre supprime par le JIT,
 *     et l'on mesurerait... rien. On le range dans un champ volatile (un "trou noir").
 * Pour des mesures serieuses, on utilise JMH (l'outil du JDK) ; ce banc suffit pour comparer deux versions.
 */
public final class Bench {

    /** Le temps de chaque tour mesure, en nanosecondes, trie. */
    public record Result(long[] nanos) {

        public long median() {
            return nanos[nanos.length / 2];
        }

        public long min() {
            return nanos[0];
        }

        public long max() {
            return nanos[nanos.length - 1];
        }

        @Override
        public String toString() {
            return "mediane " + median() / 1_000_000 + " ms (min " + min() / 1_000_000 + ", max " + max() / 1_000_000 + ")";
        }
    }

    private static volatile int sink;

    private Bench() {
    }

    public static Result measure(Supplier<?> task, int warmups, int runs) {
        if (warmups < 0 || runs < 1) {
            throw new IllegalArgumentException("chauffe >= 0 et au moins 1 tour");
        }
        for (int i = 0; i < warmups; i++) {
            sink += task.get().hashCode();
        }
        long[] nanos = new long[runs];
        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            sink += task.get().hashCode();
            nanos[i] = System.nanoTime() - start;
        }
        Arrays.sort(nanos);
        return new Result(nanos);
    }
}
