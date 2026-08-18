package exceptions.solutions;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans exceptions.exercises.Exercise09_LocaleSensitiveFormatting.
 */
public class Solution09_LocaleSensitiveFormatting {

    public static String formatCurrency(double amount, Locale locale) {
        return NumberFormat.getCurrencyInstance(locale).format(amount);
    }

    public static String formatPercent(double ratio, Locale locale) {
        return NumberFormat.getPercentInstance(locale).format(ratio);
    }

    public static String formatCompact(long value, Locale locale) {
        return NumberFormat.getCompactNumberInstance(locale, NumberFormat.Style.SHORT).format(value);
    }
}
