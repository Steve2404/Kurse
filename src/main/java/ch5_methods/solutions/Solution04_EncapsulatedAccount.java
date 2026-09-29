package ch5_methods.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch5_methods.exercises.Exercise04_EncapsulatedAccount.
 */
public class Solution04_EncapsulatedAccount {

    public static class Account {
        private final String owner;
        private int balance;
        private final List<String> history = new ArrayList<>();

        private Account(String owner) {
            this.owner = owner;
        }

        public String getOwner() {
            return owner;
        }

        public int getBalance() {
            return balance;
        }

        private void record(String entry) {
            history.add(entry);
        }

        public boolean deposit(int cents) {
            // Le guichet verifie avant de toucher au champ private.
            if (cents <= 0) {
                return false;
            }
            balance += cents;
            record("depot " + cents);
            return true;
        }

        public boolean withdraw(int cents) {
            // Deux refus : montant invalide ou solde insuffisant.
            if (cents <= 0 || cents > balance) {
                return false;
            }
            balance -= cents;
            record("retrait " + cents);
            return true;
        }

        public boolean transferTo(Account other, int cents) {
            // Reutiliser withdraw/deposit : leurs regles protegent le virement ; rien ne bouge si le retrait echoue.
            if (other == this || !withdraw(cents)) {
                return false;
            }
            other.deposit(cents);
            return true;
        }

        public List<String> getHistory() {
            // Copie non modifiable : l'appelant ne peut pas ecrire dans le vrai registre.
            return List.copyOf(history);
        }

        public boolean hasMoreThan(Account other) {
            // private = prive a la CLASSE : on lit le champ d'un autre objet Account.
            return balance > other.balance;
        }

        public static Account open(String owner, int initialCents) {
            // Fabrique static : seule porte vers le constructeur private, avec validation.
            if (owner == null || owner.isBlank()) {
                throw new IllegalArgumentException("titulaire obligatoire");
            }
            if (initialCents < 0) {
                throw new IllegalArgumentException("depot initial negatif");
            }
            Account account = new Account(owner);
            if (initialCents > 0) {
                account.deposit(initialCents);
            }
            return account;
        }
    }
}
