package ch11_exceptions.solutions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans exceptions.exercises.Exercise11_ParsingCustomFormats.
 */
public class Solution11_ParsingCustomFormats {

    public static double parseAmount(String text) throws ParseException {
        DecimalFormat format = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
        return format.parse(text).doubleValue();
    }

    public static LocalDate parseDate(String text) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(text, formatter);
    }
}
