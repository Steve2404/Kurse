package ch8_lambdas.solutions;

import java.util.function.LongSupplier;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise16_ThrottledAction.
 */
public class Solution16_ThrottledAction {

    public static Runnable buildThrottledAction(Runnable action, long cooldownMillis, LongSupplier clock) {
        // L'horloge injectee (LongSupplier) rend le test deterministe ; un tableau garde la derniere execution.
        boolean[] hasRun = {false};
        long[] lastRun = {0L};
        return () -> {
            long now = clock.getAsLong();
            if (!hasRun[0] || now - lastRun[0] >= cooldownMillis) {
                action.run();
                lastRun[0] = now;
                hasRun[0] = true;
            }
        };
    }
}
