package ch11_exceptions.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.exercises.Exercise01_CheckedVsUnchecked.
 */
public class Solution01_CheckedVsUnchecked {

    public static class InsufficientFundsException extends Exception {
        private static final long serialVersionUID = 1L; // une Exception est Serializable : -Xlint:serial l'exige

        private final double shortfall;

        public InsufficientFundsException(String message, double shortfall) {
            // Checked (extends Exception) : l'appelant sera OBLIGE de la traiter ; super(message) garde le texte pour getMessage().
            super(message);
            this.shortfall = shortfall;
        }

        public double getShortfall() {
            return shortfall;
        }
    }

    public static class InvalidAccountException extends RuntimeException {
        private static final long serialVersionUID = 1L; // une Exception est Serializable : -Xlint:serial l'exige

        public InvalidAccountException(String message) {
            super(message);
        }
    }

    public static double withdraw(double balance, double amount) throws InsufficientFundsException {
        // Un probleme METIER previsible (solde trop bas) : checked, declaree dans throws.
        if (amount > balance) {
            throw new InsufficientFundsException("Solde insuffisant", amount - balance);
        }
        return balance - amount;
    }

    public static void validateAccountId(String id) {
        // Une erreur de PROGRAMMATION (mauvais argument) : unchecked, aucun throws necessaire.
        if (id == null || id.isBlank()) {
            throw new InvalidAccountException("Identifiant de compte invalide : " + id);
        }
    }
}
