package ch11_exceptions.solutions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.exercises.Exercise10_NumberAndMessageFormatting.
 */
public class Solution10_NumberAndMessageFormatting {

    public static String formatAmount(double amount) {
        // Symboles US explicites : sinon la Locale par defaut (ex. de_DE) donnerait 1.234,50.
        DecimalFormat format = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
        return format.format(amount);
    }

    public static String formatWithEscapedLiteral(double amount) {
        // Le texte entre apostrophes est recopie tel quel, meme s'il contient des caracteres speciaux.
        DecimalFormat format = new DecimalFormat("'Total:' #,##0.00 'EUR'", DecimalFormatSymbols.getInstance(Locale.US));
        return format.format(amount);
    }

    public static String formatMessage(String name, int count) {
        // {0}, {1} : les arguments dans l'ordre ; MessageFormat convertit lui-meme les nombres.
        return MessageFormat.format("Bonjour {0}, vous avez {1} message(s).", name, count);
    }
}
