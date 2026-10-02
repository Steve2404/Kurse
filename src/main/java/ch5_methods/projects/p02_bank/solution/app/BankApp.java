package ch5_methods.projects.p02_bank.solution.app;

import ch5_methods.projects.p02_bank.Data;
import ch5_methods.projects.p02_bank.solution.core.Account;
import ch5_methods.projects.p02_bank.solution.core.Ledger;
import ch5_methods.projects.p02_bank.solution.premium.PremiumAccount;

import static ch5_methods.projects.p02_bank.solution.core.Money.format;

/**
 * SOLUTION - l'application : elle ne voit que le PUBLIC des autres paquets.
 */
public class BankApp {

    public static void main(String[] args) {
        Ledger ledger = new Ledger();
        for (String line : Data.ACCOUNTS) {
            String[] p = line.split(" ");
            long cents = Long.parseLong(p[2]);
            Account a = p[0].equals("P") ? PremiumAccount.openPremium(p[1], cents, Long.parseLong(p[3])) : Account.open(p[1], cents);
            ledger.add(a);
            System.out.println("ouvert " + a.getId() + " " + a.getOwner() + " " + a.getKind() + " " + format(a.getBalance()));
        }
        for (String op : Data.OPERATIONS) {
            System.out.println(ledger.execute(op));
        }
        StringBuilder rank = new StringBuilder("classement :");
        int place = 1;
        for (Account a : ledger.ranking()) {
            rank.append(' ').append(place++).append('.').append(a.getOwner()).append('=').append(format(a.getBalance()));
        }
        System.out.println(rank);
        // Membres static appeles par le NOM DE LA CLASSE : ils appartiennent a la classe, pas a un objet.
        System.out.println("total " + format(ledger.total()) + ", comptes ouverts " + Account.opened() + ", operations " + Ledger.operations()
                + ", max " + Ledger.MAX_ACCOUNTS);
    }
}
