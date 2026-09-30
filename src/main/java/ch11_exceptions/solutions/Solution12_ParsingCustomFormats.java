package ch11_exceptions.solutions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.exercises.Exercise12_ParsingCustomFormats.
 */
public class Solution12_ParsingCustomFormats {

    public static double parseAmount(String text) throws ParseException {
        // Le meme motif qu'au formatage, en sens inverse ; parse lance une ParseException CHECKED.
        DecimalFormat format = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
        return format.parse(text).doubleValue();
    }

    public static LocalDate parseDate(String text) {
        // LocalDate.parse lance DateTimeParseException (unchecked) si le texte ne suit pas le motif.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(text, formatter);
    }
}
