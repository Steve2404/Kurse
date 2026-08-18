package exceptions.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans exceptions.exercises.Exercise01_CheckedVsUnchecked.
 */
public class Solution01_CheckedVsUnchecked {

    public static class InsufficientFundsException extends Exception {
        private final double shortfall;

        public InsufficientFundsException(String message, double shortfall) {
            super(message);
            this.shortfall = shortfall;
        }

        public double getShortfall() {
            return shortfall;
        }
    }

    public static class InvalidAccountException extends RuntimeException {
        public InvalidAccountException(String message) {
            super(message);
        }
    }

    public static double withdraw(double balance, double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException("Solde insuffisant", amount - balance);
        }
        return balance - amount;
    }

    public static void validateAccountId(String id) {
        if (id == null || id.isBlank()) {
            throw new InvalidAccountException("Identifiant de compte invalide : " + id);
        }
    }
}
