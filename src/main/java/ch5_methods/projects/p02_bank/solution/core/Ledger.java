package ch5_methods.projects.p02_bank.solution.core;

/**
 * SOLUTION - le grand livre : la seule porte publique pour faire bouger l'argent.
 */
public class Ledger {

    public static final int MAX_ACCOUNTS = 8;
    // final sur une reference : on ne peut pas REASSIGNER le tableau, mais on peut remplir ses cases.
    private final Account[] accounts = new Account[MAX_ACCOUNTS];
    private int count;
    private static int operations;

    // Un varargs pour ajouter un ou plusieurs comptes d'un coup.
    public void add(Account... newAccounts) {
        for (Account a : newAccounts) {
            accounts[count++] = a;
        }
    }

    public static int operations() {
        return operations;
    }

    private Account find(int id) {
        for (int i = 0; i < count; i++) {
            if (accounts[i].getId() == id) {
                return accounts[i];
            }
        }
        return null;
    }

    public String execute(String command) {
        operations++;
        String[] p = command.split(" ");
        switch (p[0]) {
            case "DEPOT": {
                Account a = find(Integer.parseInt(p[1]));
                long cents = Long.parseLong(p[2]);
                if (a == null || cents <= 0) {
                    return "REFUS " + command + " (compte inconnu ou montant <= 0)";
                }
                a.deposit(cents);
                return "OK depot " + Money.format(cents) + " sur " + a.getId() + " -> " + Money.format(a.getBalance());
            }
            case "RETRAIT": {
                Account a = find(Integer.parseInt(p[1]));
                long cents = Long.parseLong(p[2]);
                if (a == null) {
                    return "REFUS " + command + " (compte inconnu)";
                }
                if (!a.withdraw(cents)) {
                    return "REFUS " + command + " (decouvert autorise " + Money.format(a.overdraft) + ")";
                }
                String alert = a.withdrawStreak() >= 3 ? " ALERTE " + a.withdrawStreak() + " retraits de suite" : "";
                return "OK retrait " + Money.format(cents) + " sur " + a.getId() + " -> " + Money.format(a.getBalance()) + alert;
            }
            case "VIREMENT": {
                Account from = find(Integer.parseInt(p[1]));
                Account to = find(Integer.parseInt(p[2]));
                long cents = Long.parseLong(p[3]);
                // On verifie TOUT avant de toucher a l'argent : un virement est tout ou rien.
                if (from == null || to == null) {
                    return "REFUS " + command + " (compte inconnu)";
                }
                if (!from.withdraw(cents)) {
                    return "REFUS " + command + " (solde insuffisant)";
                }
                to.deposit(cents);
                return "OK virement " + Money.format(cents) + " de " + from.getId() + " vers " + to.getId();
            }
            case "INTERETS": {
                StringBuilder sb = new StringBuilder("OK interets :");
                for (int i = 0; i < count; i++) {
                    Account a = accounts[i];
                    long b = a.getBalance();
                    // Taux en dix-milliemes : courant +0,10 %, premium +0,25 %, decouvert -1,50 %.
                    int rate = b < 0 ? 150 : a.getKind().equals("premium") ? 25 : 10;
                    long interest = Math.round(b * rate / 10_000.0);
                    a.applyInterest(interest);
                    sb.append(' ').append(a.getId()).append(':').append(Money.format(interest));
                }
                return sb.toString();
            }
            case "RELEVE": {
                Account a = find(Integer.parseInt(p[1]));
                return a == null ? "REFUS " + command : a.statement();
            }
            default:
                return "REFUS " + command + " (commande inconnue)";
        }
    }

    // Classement par solde decroissant : tri par selection sur une COPIE (l'ordre d'ouverture reste intact).
    public Account[] ranking() {
        Account[] r = new Account[count];
        System.arraycopy(accounts, 0, r, 0, count);
        for (int i = 0; i < r.length - 1; i++) {
            int best = i;
            for (int j = i + 1; j < r.length; j++) {
                if (r[j].getBalance() > r[best].getBalance()) {
                    best = j;
                }
            }
            Account t = r[i];
            r[i] = r[best];
            r[best] = t;
        }
        return r;
    }

    public long total() {
        long sum = 0;
        for (int i = 0; i < count; i++) {
            sum += accounts[i].getBalance();
        }
        return sum;
    }
}
