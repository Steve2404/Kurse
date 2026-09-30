package ch8_lambdas.solutions;

import java.util.function.Supplier;

/**
 * Corrige de l'exercice 17. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise17_RetryUntilSuccess.
 */
public class Solution17_RetryUntilSuccess {

    public static <T> T retryUntilSuccess(Supplier<T> action, int maxAttempts) {
        // Supplier = une action a relancer ; on rejette la derniere exception si toutes les tentatives echouent.
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
