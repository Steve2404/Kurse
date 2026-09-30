package ch11_exceptions.solutions;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Corrige de l'exercice 17. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.exercises.Exercise17_LocaleSensitiveFormatting.
 */
public class Solution17_LocaleSensitiveFormatting {

    public static String formatCurrency(double amount, Locale locale) {
        // La Locale choisit le symbole, sa place et les separateurs : jamais de motif a la main pour une devise.
        return NumberFormat.getCurrencyInstance(locale).format(amount);
    }

    public static String formatPercent(double ratio, Locale locale) {
        // Le ratio est multiplie par 100 : 0.25 -> 25 %.
        return NumberFormat.getPercentInstance(locale).format(ratio);
    }

    public static String formatCompact(long value, Locale locale) {
        // Format compact (Java 12+) : 1200000 -> 1M en US ; arrondi selon la Locale.
        return NumberFormat.getCompactNumberInstance(locale, NumberFormat.Style.SHORT).format(value);
    }
}
