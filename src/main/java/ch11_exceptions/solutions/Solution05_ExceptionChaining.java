package ch11_exceptions.solutions;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.exercises.Exercise05_ExceptionChaining.
 */
public class Solution05_ExceptionChaining {

    public static class DataImportException extends Exception {
        private static final long serialVersionUID = 1L; // une Exception est Serializable : -Xlint:serial l'exige

        public DataImportException(String message, Throwable cause) {
            // super(message, cause) garde l'exception d'origine : getCause() la rendra intacte.
            super(message, cause);
        }
    }

    public static int importRecord(String raw) throws DataImportException {
        // On traduit l'erreur technique en erreur metier SANS perdre l'originale (passee comme cause).
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException e) {
            throw new DataImportException("Enregistrement invalide : " + raw, e);
        }
    }
}
