package ch6_classdesign.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Corrige de l'exercice 17. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise17_ImmutableMoney.
 */
public class Solution17_ImmutableMoney {

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
            // Fabrique qui valide, puis convertit le texte en centimes (jamais de double pour l'argent).
            if (currency == null || !currency.matches("[A-Z]{3}")) {
                throw new IllegalArgumentException("devise invalide");
            }
            boolean negative = amount.startsWith("-");
            String digits = negative ? amount.substring(1) : amount;
            String[] parts = digits.split("\\.");
            String decimals = parts.length > 1 ? parts[1] : "00";
            if (decimals.length() == 1) {
                decimals += "0";
            }
            long cents = Long.parseLong(parts[0]) * 100 + Long.parseLong(decimals);
            return new Money(negative ? -cents : cents, currency);
        }

        public Money plus(Money other) {
            // Un NOUVEL objet : this ne change jamais.
            if (!currency.equals(other.currency)) {
                throw new IllegalArgumentException("devises differentes");
            }
            return new Money(cents + other.cents, currency);
        }

        public Money times(int factor) {
            // Meme principe : on rend un nouvel objet.
            return new Money(cents * factor, currency);
        }

        public Money[] allocate(int parts) {
            // Le reste est distribue centime par centime : la somme redonne toujours le total.
            Money[] shares = new Money[parts];
            long base = cents / parts;
            long remainder = cents % parts;
            for (int i = 0; i < parts; i++) {
                shares[i] = new Money(base + (i < remainder ? 1 : 0), currency);
            }
            return shares;
        }

        @Override
        public boolean equals(Object other) {
            // Objet valeur : egal si meme montant et meme devise.
            if (!(other instanceof Money m)) {
                return false;
            }
            return cents == m.cents && currency.equals(m.currency);
        }

        @Override
        public int hashCode() {
            // Coherent avec equals : les memes champs.
            return Objects.hash(cents, currency);
        }

        @Override
        public String toString() {
            // Le signe a part, puis deux chiffres de centimes (%02d).
            String sign = cents < 0 ? "-" : "";
            long abs = Math.abs(cents);
            return sign + abs / 100 + "." + String.format("%02d", abs % 100) + " " + currency;
        }
    }

    public static final class Wallet {
        private final List<Money> items;

        public Wallet(List<Money> items) {
            // Copie defensive a l'entree : l'appelant ne peut plus changer notre liste.
            this.items = List.copyOf(items);
        }

        public List<Money> items() {
            return items;
        }

        public Wallet with(Money money) {
            // "Modifier" un immuable = fabriquer un nouvel objet.
            List<Money> copy = new ArrayList<>(items);
            copy.add(money);
            return new Wallet(copy);
        }
    }
}
