package ch11_exceptions.projects.p01_bank.solution;

/**
 * SOLUTION - une erreur de PROGRAMMATION (mauvais identifiant) : exception NON verifiee (extends RuntimeException).
 * Aucun throws n'est exige : l'appelant peut la laisser remonter.
 */
public class UnknownAccountException extends RuntimeException {

    public UnknownAccountException(String accountId) {
        super("compte inconnu : " + accountId);
    }
}
