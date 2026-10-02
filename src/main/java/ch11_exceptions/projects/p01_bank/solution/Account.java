package ch11_exceptions.projects.p01_bank.solution;

/**
 * SOLUTION - le contrat d'un compte. withdraw declare la classe MERE des refus :
 * une implementation peut declarer moins (une sous-classe, ou rien), jamais plus.
 */
public interface Account {

    String id();

    long balance();

    // Les erreurs d'argument sont NON verifiees : pas besoin de les declarer.
    void deposit(long amount);

    void withdraw(long amount) throws BankException;
}
