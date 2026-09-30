package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * EXERCICE 17 - Un objet valeur immuable : Money (et un Wallet qui ne se laisse pas modifier) (niveau : avance)
 * ===========================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un billet de 10 euros ne se transforme pas en 20 euros : on en donne
 * un AUTRE. Money est pareil : la classe est final (personne ne peut en
 * faire une sous-classe modifiable), les champs sont private final, il
 * n'y a aucun setter, et chaque operation (plus, times) rend un NOUVEL
 * objet. Deux Money de meme montant et meme devise sont egaux
 * (equals/hashCode redefinis).
 *
 * Les montants sont stockes en centimes (long) : jamais de double pour
 * de l'argent.
 *
 *
 * ==================================================================
 * TODO 1 : Money.of(amount, currency)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   of("12.50", "EUR") -> 1250 centimes ; of("3", "EUR") -> 300 ; of("0.5", "EUR") -> 50 ; of("-1.25", "EUR") -> -125
 *   of("1", "euro") -> IllegalArgumentException("devise invalide")  (3 lettres majuscules exigees)
 *
 * -- Le plan --
 *
 *   1. currency doit correspondre a "[A-Z]{3}" (currency.matches(...)) sinon exception.
 *   2. negative = amount commence par "-" ; enlever le signe.
 *   3. Couper sur "." : partie entiere, partie decimale (absente -> "00" ; 1 chiffre -> ajouter un "0").
 *   4. cents = entiere * 100 + decimale ; appliquer le signe ; rendre new Money(cents, currency).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : plus(other)    et    TODO 3 : times(factor)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. plus : devises differentes -> IllegalArgumentException("devises differentes") ;
 *      sinon rendre new Money(cents + other.cents, currency).
 *   2. times : rendre new Money(cents * factor, currency).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. Ne JAMAIS modifier this : l'ancien objet doit rester identique.
 *
 *
 * ==================================================================
 * TODO 4 : allocate(parts)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Partager 10,00 EUR en 3 : 3,33 x 3 = 9,99 ; il reste 1 centime. On le
 * donne au premier. La somme des parts doit TOUJOURS redonner le total.
 *
 * -- Essayons a la main --
 *
 *   10.00 EUR en 3 -> [3.34, 3.33, 3.33]      1.00 EUR en 4 -> [0.25, 0.25, 0.25, 0.25]
 *
 * -- Le plan --
 *
 *   1. base = cents / parts ; reste = cents % parts.
 *   2. La part i vaut base + (i < reste ? 1 : 0).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : equals(other) et hashCode()    TODO 6 : toString()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   of("12.5", "EUR").equals(of("12.50", "EUR")) -> true
 *   toString : "12.50 EUR" ; "-1.25 EUR" ; "0.05 EUR" ; "-0.05 EUR"
 *
 * -- Le plan --
 *
 *   1. equals : meme classe, meme cents, meme currency ; hashCode : java.util.Objects.hash(cents, currency).
 *   2. toString : signe ("-" si cents < 0), abs = Math.abs(cents), puis
 *      abs / 100 + "." + deux chiffres de abs % 100 (String.format("%02d", ...)) + " " + currency.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : Wallet(items)    et    TODO 8 : Wallet.with(money)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Le portefeuille recoit une liste : il en garde une COPIE non
 * modifiable (List.copyOf), pour que l'appelant ne puisse pas la
 * changer plus tard. with() rend un NOUVEAU portefeuille avec un
 * billet de plus.
 *
 * -- Le plan --
 *
 *   1. Constructeur : this.items = List.copyOf(items).
 *   2. with : copie modifiable (new ArrayList<>(items)), add, puis rendre new Wallet(copie).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Long.parseLong("12") ; "5".length() == 1 -> "5" + "0".
 */
public class Exercise17_ImmutableMoney {

    public static final class Money {
        private final long cents;
        private final String currency;

        private Money(long cents, String currency) {
            this.cents = cents;
            this.currency = currency;
        }

        public long cents() {
            return cents;
        }

        public String currency() {
            return currency;
        }

        public static Money of(String amount, String currency) {
            throw new UnsupportedOperationException("TODO 1 : implementer of()");
        }

        public Money plus(Money other) {
            throw new UnsupportedOperationException("TODO 2 : implementer plus()");
        }

        public Money times(int factor) {
            throw new UnsupportedOperationException("TODO 3 : implementer times()");
        }

        public Money[] allocate(int parts) {
            throw new UnsupportedOperationException("TODO 4 : implementer allocate()");
        }

        @Override
        public boolean equals(Object other) {
            throw new UnsupportedOperationException("TODO 5 : implementer equals()");
        }

        @Override
        public int hashCode() {
            throw new UnsupportedOperationException("TODO 5 : implementer hashCode()");
        }

        @Override
        public String toString() {
            throw new UnsupportedOperationException("TODO 6 : implementer toString()");
        }
    }

    public static final class Wallet {
        private final List<Money> items;

        public Wallet(List<Money> items) {
            throw new UnsupportedOperationException("TODO 7 : implementer Wallet()");
        }

        public List<Money> items() {
            return items;
        }

        public Wallet with(Money money) {
            throw new UnsupportedOperationException("TODO 8 : implementer with()");
        }
    }

    public static void main(String[] args) {
        ExerciseChecker.check("of : 1250, 300, 50, -125",
                Money.of("12.50", "EUR").cents() == 1250 && Money.of("3", "EUR").cents() == 300
                        && Money.of("0.5", "EUR").cents() == 50 && Money.of("-1.25", "EUR").cents() == -125);
        String error = null;
        try {
            Money.of("1", "euro");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        ExerciseChecker.check("of refuse une devise invalide", "devise invalide".equals(error));

        Money ten = Money.of("10", "EUR");
        Money sum = ten.plus(Money.of("2.50", "EUR"));
        ExerciseChecker.check("plus rend un NOUVEL objet, ten reste 1000", sum.cents() == 1250 && ten.cents() == 1000 && sum != ten);
        String currencyError = null;
        try {
            ten.plus(Money.of("1", "USD"));
        } catch (IllegalArgumentException e) {
            currencyError = e.getMessage();
        }
        ExerciseChecker.check("plus refuse des devises differentes", "devises differentes".equals(currencyError));
        ExerciseChecker.check("times(3) == 3000 et ten inchange", ten.times(3).cents() == 3000 && ten.cents() == 1000);

        Money[] shares = ten.allocate(3);
        ExerciseChecker.check("allocate(3) : 334, 333, 333 (somme 1000)",
                Arrays.equals(new long[] {shares[0].cents(), shares[1].cents(), shares[2].cents()}, new long[] {334, 333, 333}));

        Set<Money> set = new HashSet<>(List.of(Money.of("12.5", "EUR"), Money.of("12.50", "EUR"), Money.of("12.50", "USD")));
        ExerciseChecker.check("equals/hashCode : 12.5 EUR == 12.50 EUR, pas USD -> 2 dans le Set",
                Money.of("12.5", "EUR").equals(Money.of("12.50", "EUR")) && set.size() == 2);
        ExerciseChecker.check("toString : 4 formats",
                Money.of("12.50", "EUR").toString().equals("12.50 EUR") && Money.of("-1.25", "EUR").toString().equals("-1.25 EUR")
                        && Money.of("0.05", "EUR").toString().equals("0.05 EUR") && Money.of("-0.05", "EUR").toString().equals("-0.05 EUR"));

        List<Money> source = new ArrayList<>(List.of(ten));
        Wallet wallet = new Wallet(source);
        source.add(Money.of("99", "EUR"));
        boolean readOnly;
        try {
            wallet.items().add(ten);
            readOnly = false;
        } catch (UnsupportedOperationException e) {
            readOnly = true;
        }
        ExerciseChecker.check("Wallet : copie a l'entree et liste non modifiable", wallet.items().size() == 1 && readOnly);
        Wallet bigger = wallet.with(Money.of("5", "EUR"));
        ExerciseChecker.check("with : nouveau Wallet de 2 billets, l'ancien en garde 1", bigger.items().size() == 2 && wallet.items().size() == 1);

        ExerciseChecker.summary();
    }
}
