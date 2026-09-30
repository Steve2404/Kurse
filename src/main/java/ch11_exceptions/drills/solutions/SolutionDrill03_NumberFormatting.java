package ch11_exceptions.drills.solutions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.drills.exercises.Drill03_NumberFormatting.
 */
public class SolutionDrill03_NumberFormatting {

    public static String us(double value) {
        // Symboles US explicites : sinon la Locale par defaut choisit ',' ou '.'.
        return new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US)).format(value);
    }

    public static String padded(int value) {
        // Chaque 0 est un chiffre obligatoire : on complete a gauche.
        return new DecimalFormat("000", DecimalFormatSymbols.getInstance(Locale.US)).format(value);
    }

    public static String optional(double value) {
        // Les # ne s'affichent que s'ils servent : 3 -> "3", sans point.
        return new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.US)).format(value);
    }

    public static String halfEven(double value) {
        // DecimalFormat arrondit au pair : 0.125 -> 0.12 (egalite exacte), 0.135 -> 0.14 (le double est un peu au-dessus).
        return new DecimalFormat("0.00", DecimalFormatSymbols.getInstance(Locale.US)).format(value);
    }

    public static String currencyUs(double value) {
        // La Locale decide du symbole et de sa place ; le signe passe devant le $.
        return NumberFormat.getCurrencyInstance(Locale.US).format(value);
    }

    public static String currencyFr(double value) {
        // En France : espace fine insecable pour les milliers, insecable avant l'euro, virgule decimale.
        return NumberFormat.getCurrencyInstance(Locale.FRANCE).format(value);
    }

    public static String percentFr(double ratio) {
        // Le ratio est multiplie par 100 et arrondi (0 decimale par defaut).
        return NumberFormat.getPercentInstance(Locale.FRANCE).format(ratio);
    }

    public static String german(double value) {
        // Allemand : '.' groupe les milliers, ',' separe les decimales (3 decimales max par defaut).
        return NumberFormat.getInstance(Locale.GERMANY).format(value);
    }

    public static String compact(long value) {
        // Format court : 1 200 000 arrondi a "1M".
        return NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT).format(value);
    }

    public static int parsePrefix(String text) {
        // parse lit le debut valide et ignore la suite ; rien de valide au debut -> ParseException (checked).
        try {
            return NumberFormat.getInstance(Locale.US).parse(text).intValue();
        } catch (ParseException e) {
            return -1;
        }
    }

    public static String inbox(String name, int count) {
        // {0}, {1} : les arguments dans l'ordre.
        return MessageFormat.format("{0} a {1} messages", name, count);
    }

    public static String apostrophe() {
        // Une apostrophe seule ouvrirait un texte litteral ("It's {0}" -> "Its {0}") : on la double.
        return MessageFormat.format("It''s {0}", "ok");
    }
}
