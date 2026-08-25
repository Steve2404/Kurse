package concurrency.solutions;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans concurrency.exercises.Exercise06_AtomicClasses.
 */
public class Solution06_AtomicClasses {

    public static void incrementAtomic(AtomicInteger counter) {
        counter.incrementAndGet();
    }

    public static void updateMax(AtomicInteger currentMax, int candidate) {
        currentMax.accumulateAndGet(candidate, Math::max);
    }
}
