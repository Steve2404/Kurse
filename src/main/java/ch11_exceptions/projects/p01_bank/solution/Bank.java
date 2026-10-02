package ch11_exceptions.projects.p01_bank.solution;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SOLUTION - la banque : recherche des comptes, virement atomique (avec compensation), partage.
 */
public class Bank {

    private final Map<String, Account> accounts = new LinkedHashMap<>();

    static String money(long cents) {
        return cents / 100 + "." + (cents % 100 < 10 ? "0" : "") + cents % 100;
    }

    public void open(String line) {
        String[] p = line.split(" ");
        long balance = Long.parseLong(p[2]);
        accounts.put(p[0], p.length > 3 && p[3].equals("gele") ? new FrozenAccount(p[0], balance) : new StandardAccount(p[0], balance));
    }

    public Account find(String id) {
        Account a = accounts.get(id);
        if (a == null) {
            throw new UnknownAccountException(id);     // non verifiee : find n'a pas de throws
        }
        return a;
    }

    public Collection<Account> accounts() {
        return accounts.values();
    }

    // Virement ATOMIQUE : si le credit echoue apres le debit, on rembourse (compensation) avant de signaler l'echec.
    public void transfer(String from, String to, long amount) throws TransferException {
        Account source = find(from);
        Account target = find(to);
        try {
            source.withdraw(amount);
        } catch (BankException e) {
            throw new TransferException("virement " + from + "->" + to + " refuse", e);   // on enveloppe la cause
        }
        try {
            target.deposit(amount);
        } catch (IllegalStateException e) {
            source.deposit(amount);
            throw new TransferException("virement " + from + "->" + to + " annule", e);
        }
    }

    // Division entiere par parts == 0 : ArithmeticException (/ by zero), non verifiee.
    public String share(String id, int parts) {
        long balance = find(id).balance();
        return parts + " parts de " + money(balance / parts) + " (reste " + money(balance % parts) + ")";
    }
}
