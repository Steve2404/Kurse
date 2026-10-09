package ch18_design.projects.p01_invoice.solution;

/**
 * Un montant en centimes, jamais negatif. Un OBJET VALEUR : immuable, compare par sa valeur (record),
 * et il porte ses propres calculs : l'arrondi et le format ne sont plus copies huit fois.
 */
public record Money(long cents) {

    public static final Money ZERO = new Money(0);

    // Le constructeur compact valide : un Money negatif ne peut pas exister (la remise s'affiche avec un "-" a part).
    public Money {
        if (cents < 0) {
            throw new IllegalArgumentException("montant negatif : " + cents);
        }
    }

    public static Money ofCents(long cents) {
        return new Money(cents);
    }

    public Money plus(Money other) {
        return new Money(cents + other.cents);
    }

    public Money minus(Money other) {
        return new Money(cents - other.cents);
    }

    public Money times(int factor) {
        return new Money(cents * factor);
    }

    // Arrondi au centime le plus proche (la moitie monte), comme le legacy : (t * 10 + 50) / 100.
    public Money percent(int percent) {
        return new Money((cents * percent + 50) / 100);
    }

    public boolean isZero() {
        return cents == 0;
    }

    // Le piege du legacy, regle une fois : 5 centimes s'ecrivent ",05", pas ",5".
    public String format() {
        return String.format("%d,%02d", cents / 100, cents % 100);
    }
}
