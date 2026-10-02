package ch11_exceptions.projects.p01_bank.solution;

/**
 * SOLUTION - une exception qui TRANSPORTE une donnee (le montant manquant), lue ensuite par le catch.
 */
public class InsufficientFundsException extends BankException {

    private final long missing;

    public InsufficientFundsException(String accountId, long missing) {
        super("solde insuffisant sur " + accountId);
        this.missing = missing;
    }

    public long missing() {
        return missing;
    }
}
