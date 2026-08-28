package ch11_exceptions.solutions;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans exceptions.exercises.Exercise12_ExceptionChaining.
 */
public class Solution12_ExceptionChaining {

    public static class DataImportException extends Exception {
        public DataImportException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static int importRecord(String raw) throws DataImportException {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new DataImportException("Enregistrement invalide : " + raw, e);
        }
    }
}
