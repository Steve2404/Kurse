package ch11_exceptions.solutions;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.exercises.Exercise08_SuppressedExceptions.
 */
public class Solution08_SuppressedExceptions {

    public static class FaultyResource implements AutoCloseable {
        @Override
        public void close() {
            // Une fermeture qui echoue : son exception sera ajoutee en suppressed a l'exception primaire.
            throw new IllegalArgumentException("Erreur de fermeture");
        }
    }

    @SuppressWarnings("try") // r n'est pas lu dans le bloc : seule sa fermeture (qui echoue) nous interesse
    public static void runWithSuppressed() {
        // L'exception du corps est la primaire ; celle de close() ne l'ecrase pas, elle est accrochee via addSuppressed.
        try (FaultyResource r = new FaultyResource()) {
            throw new IllegalStateException("Erreur primaire");
        }
    }
}
