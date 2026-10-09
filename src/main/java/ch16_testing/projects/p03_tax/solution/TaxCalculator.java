package ch16_testing.projects.p03_tax.solution;

/**
 * L'impot sur le revenu de Javaland, en euros entiers, par tranches (bareme marginal).
 * Jusqu'a 10 000 : 0 % ; de 10 001 a 25 000 : 10 % ; de 25 001 a 60 000 : 25 % ; au-dela : 40 %.
 */
public final class TaxCalculator {

    private static final long[] LIMITS = {10_000, 25_000, 60_000};
    private static final int[] RATES = {0, 10, 25, 40};

    private TaxCalculator() {
    }

    // Pourquoi un taux MARGINAL : chaque tranche n'est taxee qu'a son propre taux ; gagner 1 euro de plus
    // ne fait jamais payer plus que ce euro. Pourquoi "+ 50" en centiemes : un arrondi a l'euro le plus proche.
    public static long tax(long income) {
        checkIncome(income);
        long hundredths = 0;
        long lower = 0;
        for (int i = 0; i < RATES.length; i++) {
            long upper = i < LIMITS.length ? LIMITS[i] : Long.MAX_VALUE;
            if (income > lower) {
                hundredths += (Math.min(income, upper) - lower) * RATES[i];
            }
            lower = upper;
        }
        return (hundredths + 50) / 100;
    }

    // Piege : les limites appartiennent a la tranche du DESSOUS (10 000 est encore a 0 %).
    public static int marginalRate(long income) {
        checkIncome(income);
        for (int i = 0; i < LIMITS.length; i++) {
            if (income <= LIMITS[i]) {
                return RATES[i];
            }
        }
        return RATES[RATES.length - 1];
    }

    // Pourquoi des DEMI-parts : 1 part par adulte, une demi-part pour chacun des deux premiers enfants,
    // une part entiere a partir du troisieme ; tout reste entier.
    public static long taxWithShares(long income, int adults, int children) {
        if (adults < 1 || adults > 2) {
            throw new IllegalArgumentException("adultes : 1 ou 2 (recu " + adults + ")");
        }
        if (children < 0) {
            throw new IllegalArgumentException("enfants negatif : " + children);
        }
        int halfShares = 2 * adults + Math.min(children, 2) + 2 * Math.max(0, children - 2);
        long perShare = income * 2 / halfShares;
        return tax(perShare) * halfShares / 2;
    }

    private static void checkIncome(long income) {
        if (income < 0) {
            throw new IllegalArgumentException("revenu negatif : " + income);
        }
    }
}
