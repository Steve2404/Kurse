package ch11_exceptions.projects.p02_calculator.solution;

/**
 * SOLUTION - une erreur de SAISIE, que l'appelant doit traiter : exception verifiee, avec la position fautive.
 */
public class SyntaxException extends Exception {

    private final int position;

    public SyntaxException(String message, int position) {
        super(message);
        this.position = position;
    }

    // Avec la cause : l'exception technique d'origine (ex. NumberFormatException) reste accessible.
    public SyntaxException(String message, int position, Throwable cause) {
        super(message, cause);
        this.position = position;
    }

    public int position() {
        return position;
    }
}
