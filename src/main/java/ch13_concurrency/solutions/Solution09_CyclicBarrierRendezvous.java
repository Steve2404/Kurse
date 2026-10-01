package ch13_concurrency.solutions;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise09_CyclicBarrierRendezvous.
 */
public class Solution09_CyclicBarrierRendezvous {

    public static Runnable buildBarrierWorker(CyclicBarrier barrier, int index, List<String> trace) {
        // await() bloque jusqu'a ce que TOUS les participants arrivent : aucune phase2 ne peut preceder une phase1.
        return () -> {
            trace.add("phase1-" + index);
            try {
                barrier.await();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            trace.add("phase2-" + index);
        };
    }
}
