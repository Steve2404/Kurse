package ch8_lambdas.solutions;

import java.util.function.Supplier;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans lambdas.exercises.Exercise12_RetryUntilSuccess.
 */
public class Solution12_RetryUntilSuccess {

    public static <T> T retryUntilSuccess(Supplier<T> action, int maxAttempts) {
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                return action.get();
            } catch (RuntimeException e) {
                if (attempt == maxAttempts) {
                    throw e;
                }
            }
        }
        throw new IllegalStateException("inatteignable");
    }
}
