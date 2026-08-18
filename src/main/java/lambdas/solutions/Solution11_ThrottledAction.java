package lambdas.solutions;

import java.util.function.LongSupplier;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans lambdas.exercises.Exercise11_ThrottledAction.
 */
public class Solution11_ThrottledAction {

    public static Runnable buildThrottledAction(Runnable action, long cooldownMillis, LongSupplier clock) {
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
