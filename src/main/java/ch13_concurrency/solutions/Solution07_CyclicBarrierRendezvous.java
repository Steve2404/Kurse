package ch13_concurrency.solutions;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans concurrency.exercises.Exercise07_CyclicBarrierRendezvous.
 */
public class Solution07_CyclicBarrierRendezvous {

    public static Runnable buildBarrierWorker(CyclicBarrier barrier, int index, List<String> trace) {
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
