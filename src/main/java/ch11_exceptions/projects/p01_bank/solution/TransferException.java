package ch11_exceptions.projects.p01_bank.solution;

/**
 * SOLUTION - l'echec d'un virement ENVELOPPE la cause precise (chainage d'exceptions).
 */
public class TransferException extends BankException {

    public TransferException(String message, Throwable cause) {
        super(message, cause);
    }
}
