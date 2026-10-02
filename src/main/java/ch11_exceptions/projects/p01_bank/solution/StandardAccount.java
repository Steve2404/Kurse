package ch11_exceptions.projects.p01_bank.solution;

/**
 * SOLUTION - un compte ordinaire.
 */
public class StandardAccount implements Account {

    private final String id;
    private long balance;

    public StandardAccount(String id, long balance) {
        this.id = id;
        this.balance = balance;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public long balance() {
        return balance;
    }

    @Override
    public void deposit(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("montant invalide : " + amount);
        }
        balance += amount;
    }

    // Redefinition qui declare une exception PLUS PRECISE que l'interface : permis.
    @Override
    public void withdraw(long amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("montant invalide : " + amount);
        }
        if (amount > balance) {
            throw new InsufficientFundsException(id, amount - balance);
        }
        balance -= amount;
    }
}
