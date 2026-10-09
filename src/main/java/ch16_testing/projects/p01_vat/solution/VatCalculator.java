package ch16_testing.projects.p01_vat.solution;

import java.util.Locale;
import java.util.Objects;

/**
 * La TVA du pays de Javaland, en centimes.
 * Pourquoi des long et jamais de double : 0.1 + 0.2 vaut 0.30000000000000004 ; en centimes, un compte reste juste.
 */
public final class VatCalculator {

    private VatCalculator() {
    }

    // Pourquoi "+ 50" : on arrondit au centime le plus PROCHE (un demi-centime monte), au lieu de tronquer.
    // Piege : les deux controles sont des LIMITES ; 0 centime et 100 % sont permis.
    public static long vat(long netCents, int ratePercent) {
        if (netCents < 0) {
            throw new IllegalArgumentException("montant negatif : " + netCents);
        }
        if (ratePercent < 0 || ratePercent > 100) {
            throw new IllegalArgumentException("taux invalide : " + ratePercent);
        }
        return (netCents * ratePercent + 50) / 100;
    }

    // Pourquoi passer par vat : une seule regle d'arrondi, donc gross - net == vat, toujours.
    public static long gross(long netCents, int ratePercent) {
        return netCents + vat(netCents, ratePercent);
    }

    // Pourquoi Locale.ROOT : toLowerCase() sans Locale depend de la machine (en turc, "I" ne devient pas "i").
    // Piege : requireNonNull AVANT strip, pour un message clair au lieu d'une NullPointerException anonyme.
    public static int rateFor(String category) {
        Objects.requireNonNull(category, "categorie absente");
        return switch (category.strip().toLowerCase(Locale.ROOT)) {
            case "standard" -> 20;
            case "intermediaire" -> 10;
            case "reduit" -> 5;
            default -> throw new IllegalArgumentException("categorie inconnue : " + category);
        };
    }

    // Pourquoi le signe a part : -5 / 100 vaut 0 et -5 % 100 vaut -5 ; on travaille sur la valeur absolue.
    // Piege : %02d, sinon 5 centimes s'affichent "0,5".
    public static String format(long cents) {
        String sign = cents < 0 ? "-" : "";
        long abs = Math.abs(cents);
        return String.format("%s%d,%02d EUR", sign, abs / 100, abs % 100);
    }
}
