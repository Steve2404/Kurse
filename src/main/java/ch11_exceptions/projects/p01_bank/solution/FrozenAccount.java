package ch11_exceptions.projects.p01_bank.solution;

/**
 * SOLUTION - un compte gele : tout mouvement est refuse.
 */
public class FrozenAccount implements Account {

    private final String id;
    private final long balance;

    public FrozenAccount(String id, long balance) {
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

    // deposit ne peut PAS declarer d'exception verifiee (l'interface n'en declare pas) : on lance une non verifiee.
    @Override
    public void deposit(long amount) {
        throw new IllegalStateException("compte gele : " + id);
    }

    @Override
    public void withdraw(long amount) throws FrozenAccountException {
        throw new FrozenAccountException(id);
    }
}
