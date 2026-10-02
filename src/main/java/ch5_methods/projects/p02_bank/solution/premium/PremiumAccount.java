package ch5_methods.projects.p02_bank.solution.premium;

import ch5_methods.projects.p02_bank.solution.core.Account;

/**
 * SOLUTION - un compte premium, dans un AUTRE paquet. extends ne sert ici qu'a montrer protected.
 */
public class PremiumAccount extends Account {

    public static PremiumAccount openPremium(String owner, long cents, long overdraft) {
        PremiumAccount p = new PremiumAccount();
        // protected depuis un autre paquet : permis car l'appel passe par une reference de type PremiumAccount.
        // Avec "Account a = p; a.init(...)", javac refuserait : init() has protected access.
        p.init(owner, cents);
        p.overdraft = overdraft;
        p.kind = "premium";
        return p;
    }
}
