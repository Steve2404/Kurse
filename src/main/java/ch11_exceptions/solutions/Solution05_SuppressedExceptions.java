package ch11_exceptions.solutions;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans exceptions.exercises.Exercise05_SuppressedExceptions.
 */
public class Solution05_SuppressedExceptions {

    public static class FaultyResource implements AutoCloseable {
        @Override
        public void close() {
            throw new IllegalArgumentException("Erreur de fermeture");
        }
    }

    public static void runWithSuppressed() {
        try (FaultyResource r = new FaultyResource()) {
            throw new IllegalStateException("Erreur primaire");
        }
    }
}
