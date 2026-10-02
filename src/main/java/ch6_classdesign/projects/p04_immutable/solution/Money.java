package ch6_classdesign.projects.p04_immutable.solution;

/**
 * SOLUTION - une somme d'argent IMMUABLE.
 * Les 5 regles : classe final, champs private final, pas de setter, copies defensives des objets mutables,
 * et toute "modification" rend un NOUVEL objet.
 */
public final class Money {

    private final long cents;
    private final String currency;

    // Constructeur private : on passe par la fabrique of(...).
    private Money(long cents, String currency) {
        this.cents = cents;
        this.currency = currency;
    }

    public static Money of(long cents, String currency) {
        return new Money(cents, currency);
    }

    public long cents() {
        return cents;
    }

    // Rend null si les devises different (les exceptions sont au chapitre 11).
    public Money plus(Money other) {
        return currency.equals(other.currency) ? new Money(cents + other.cents, currency) : null;
    }

    public Money times(int percent) {
        return new Money(Math.round(cents * percent / 100.0), currency);
    }

    // Repartition sans perte : chaque part recoit sa part entiere, puis les centimes restants
    // vont un par un aux premieres parts. La somme des parts vaut toujours le total.
    public Money[] allocate(int... ratios) {
        long total = 0;
        for (int r : ratios) {
            total += r;
        }
        Money[] parts = new Money[ratios.length];
        long remainder = cents;
        long[] amounts = new long[ratios.length];
        for (int i = 0; i < ratios.length; i++) {
            amounts[i] = cents * ratios[i] / total;
            remainder -= amounts[i];
        }
        for (int i = 0; remainder > 0; i = (i + 1) % ratios.length) {
            amounts[i]++;
            remainder--;
        }
        for (int i = 0; i < ratios.length; i++) {
            parts[i] = new Money(amounts[i], currency);
        }
        return parts;
    }

    // equals redefini : deux Money sont egales si montant ET devise sont egaux.
    @Override
    public boolean equals(Object o) {
        return o instanceof Money m && m.cents == cents && m.currency.equals(currency);
    }

    // Contrat : des objets egaux DOIVENT avoir le meme hashCode.
    @Override
    public int hashCode() {
        return 31 * Long.hashCode(cents) + currency.hashCode();
    }

    @Override
    public String toString() {
        long abs = Math.abs(cents);
        long rest = abs % 100;
        return (cents < 0 ? "-" : "") + abs / 100 + "." + (rest < 10 ? "0" : "") + rest + " " + currency;
    }
}
