package exceptions.solutions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans exceptions.exercises.Exercise06_NumberAndMessageFormatting.
 */
public class Solution06_NumberAndMessageFormatting {

    public static String formatAmount(double amount) {
        DecimalFormat format = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
        return format.format(amount);
    }

    public static String formatWithEscapedLiteral(double amount) {
        DecimalFormat format = new DecimalFormat("'Total:' #,##0.00 'EUR'", DecimalFormatSymbols.getInstance(Locale.US));
        return format.format(amount);
    }

    public static String formatMessage(String name, int count) {
        return MessageFormat.format("Bonjour {0}, vous avez {1} message(s).", name, count);
    }
}
