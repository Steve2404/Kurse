package ch13_concurrency.solutions;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise08_AtomicClasses.
 */
public class Solution08_AtomicClasses {

    public static void incrementAtomic(AtomicInteger counter) {
        // incrementAndGet est atomique (compare-and-set) : pas besoin de verrou.
        counter.incrementAndGet();
    }

    public static void updateMax(AtomicInteger currentMax, int candidate) {
        // accumulateAndGet applique Math::max de facon atomique ; un get() puis set() laisserait passer une race condition.
        currentMax.accumulateAndGet(candidate, Math::max);
    }
}
