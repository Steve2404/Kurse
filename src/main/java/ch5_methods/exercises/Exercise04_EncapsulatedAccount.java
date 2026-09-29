package ch5_methods.exercises;

import ch5_methods.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 4 - L'encapsulation en vrai : un compte bancaire aux champs private, des methodes public qui protegent (niveau : avance)
 * ============================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_MethodDeclarationRules.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un compte bancaire garde son solde dans un coffre (private) : PERSONNE
 * ne peut ecrire "compte.balance = 1000000" de l'exterieur. Pour changer
 * le solde, on passe par le guichet (des methodes public) qui VERIFIE
 * chaque operation : pas de depot negatif, pas de retrait plus grand
 * que le solde. Et quand on donne l'historique, on donne une COPIE en
 * lecture seule, pas le vrai registre (sinon on pourrait le modifier).
 *
 * Detail important : private veut dire "prive a la CLASSE", pas "prive
 * a l'objet". Dans Account, on peut lire other.balance d'un AUTRE compte.
 *
 * Les montants sont en centimes (int) pour eviter les erreurs des double.
 *
 *
 * ==================================================================
 * TODO 1 : Account.open(owner, initialCents)    [methode static de fabrique]
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le constructeur est private : on ne peut ouvrir un compte que par le
 * guichet open(), qui refuse un nom vide et un depot initial negatif.
 *
 * -- Essayons a la main --
 *
 *   open("Ada", 1000) -> compte d'Ada, solde 1000, historique ["depot 1000"]
 *   open("Ada", 0)    -> solde 0, historique []
 *   open("  ", 10)    -> IllegalArgumentException("titulaire obligatoire")
 *   open("Ada", -1)   -> IllegalArgumentException("depot initial negatif")
 *
 * -- Le plan --
 *
 *   1. owner null ou blank -> exception ; initialCents < 0 -> exception.
 *   2. account = new Account(owner) ; si initialCents > 0, account.deposit(initialCents) ; rendre account.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : deposit (TODO 2).
 *
 * ==================================================================
 * TODO 2 : Account.deposit(cents)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   solde 0, deposit(500)  -> true, solde 500, historique ["depot 500"]
 *   deposit(0) ou deposit(-5) -> false, rien ne change
 *
 * -- Le plan --
 *
 *   1. Si cents <= 0 -> false.
 *   2. balance += cents ; record("depot " + cents) ; true.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : record(entry), private, deja ecrite.
 *
 *
 * ==================================================================
 * TODO 3 : Account.withdraw(cents)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   solde 500, withdraw(200) -> true, solde 300, "retrait 200"
 *   withdraw(1000) -> false (pas assez) ; withdraw(-1) -> false
 *
 * -- Le plan --
 *
 *   1. Si cents <= 0 ou cents > balance -> false.
 *   2. balance -= cents ; record("retrait " + cents) ; true.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : record.
 *
 *
 * ==================================================================
 * TODO 4 : Account.transferTo(other, cents)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un virement : on retire ici ET on depose la-bas, ou rien du tout. On
 * reutilise withdraw et deposit : leurs verifications suffisent.
 *
 * -- Le plan --
 *
 *   1. Si other == this -> false (virement vers soi-meme interdit).
 *   2. Si !withdraw(cents) -> false.
 *   3. other.deposit(cents) ; true.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : withdraw et deposit (TODO 2 et 3).
 *
 *
 * ==================================================================
 * TODO 5 : Account.getHistory()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On montre le registre, mais on donne une PHOTOCOPIE plastifiee :
 * List.copyOf rend une liste qu'on ne peut pas modifier
 * (UnsupportedOperationException si on essaie).
 *
 * -- Le plan --
 *
 *   1. Rendre List.copyOf(history).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : Account.hasMoreThan(other)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Rendre balance > other.balance (on lit le champ private d'un AUTRE objet : permis dans la meme classe).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Une methode static de la classe peut appeler le constructeur private.
 *   - throw new IllegalArgumentException("message");
 */
public class Exercise04_EncapsulatedAccount {

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
            throw new UnsupportedOperationException("TODO 2 : implementer deposit()");
        }

        public boolean withdraw(int cents) {
            throw new UnsupportedOperationException("TODO 3 : implementer withdraw()");
        }

        public boolean transferTo(Account other, int cents) {
            throw new UnsupportedOperationException("TODO 4 : implementer transferTo()");
        }

        public List<String> getHistory() {
            throw new UnsupportedOperationException("TODO 5 : implementer getHistory()");
        }

        public boolean hasMoreThan(Account other) {
            throw new UnsupportedOperationException("TODO 6 : implementer hasMoreThan()");
        }

        public static Account open(String owner, int initialCents) {
            throw new UnsupportedOperationException("TODO 1 : implementer open()");
        }
    }

    public static void main(String[] args) {
        Account ada = Account.open("Ada", 1000);
        Account linus = Account.open("Linus", 0);
        ExerciseChecker.check("open : Ada 1000 [depot 1000], Linus 0 []",
                ada.getOwner().equals("Ada") && ada.getBalance() == 1000 && ada.getHistory().equals(List.of("depot 1000"))
                        && linus.getBalance() == 0 && linus.getHistory().isEmpty());

        boolean ok = ada.deposit(500);
        boolean refused = !ada.deposit(0) && !ada.deposit(-5);
        ExerciseChecker.check("deposit : 500 accepte, 0 et -5 refuses", ok && refused && ada.getBalance() == 1500);

        ExerciseChecker.check("withdraw : 200 accepte, 5000 et -1 refuses",
                ada.withdraw(200) && !ada.withdraw(5000) && !ada.withdraw(-1) && ada.getBalance() == 1300);

        ExerciseChecker.check("transferTo : 300 vers Linus, puis 10 000 refuse, vers soi-meme refuse",
                ada.transferTo(linus, 300) && ada.getBalance() == 1000 && linus.getBalance() == 300
                        && !linus.transferTo(ada, 10_000) && !ada.transferTo(ada, 1) && linus.getBalance() == 300);

        ExerciseChecker.check("getHistory : l'ordre des operations",
                ada.getHistory().equals(List.of("depot 1000", "depot 500", "retrait 200", "retrait 300")));
        boolean readOnly;
        try {
            ada.getHistory().add("fraude");
            readOnly = false;
        } catch (UnsupportedOperationException e) {
            readOnly = true;
        }
        ExerciseChecker.check("getHistory rend une copie en lecture seule", readOnly && ada.getHistory().size() == 4);

        ExerciseChecker.check("hasMoreThan lit le champ private d'un AUTRE compte", ada.hasMoreThan(linus) && !linus.hasMoreThan(ada));

        ExerciseChecker.check("open refuse un titulaire vide et un depot negatif",
                "titulaire obligatoire".equals(errorOf("  ", 10)) && "depot initial negatif".equals(errorOf("Ada", -1)));

        ExerciseChecker.summary();
    }

    // Deja ecrit : le message de l'exception lancee par open(), ou null.
    private static String errorOf(String owner, int cents) {
        try {
            Account.open(owner, cents);
            return null;
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }
}
