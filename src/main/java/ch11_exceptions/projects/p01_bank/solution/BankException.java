package ch11_exceptions.projects.p01_bank.solution;

/**
 * SOLUTION - la racine des erreurs METIER de la banque : une exception VERIFIEE (extends Exception).
 * Qui appelle une methode qui la declare doit la traiter (catch) ou la declarer (throws).
 */
public class BankException extends Exception {

    public BankException(String message) {
        super(message);
    }

    // Le constructeur (message, cause) permet le CHAINAGE : l'exception d'origine reste accessible par getCause().
    public BankException(String message, Throwable cause) {
        super(message, cause);
    }
}
