package ch11_exceptions.projects.p01_bank.solution;

/**
 * SOLUTION - un autre refus metier, frere de InsufficientFundsException.
 */
public class FrozenAccountException extends BankException {

    public FrozenAccountException(String accountId) {
        super("compte gele : " + accountId);
    }
}
