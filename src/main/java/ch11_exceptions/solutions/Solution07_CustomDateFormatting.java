package ch11_exceptions.solutions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans exceptions.exercises.Exercise07_CustomDateFormatting.
 */
public class Solution07_CustomDateFormatting {

    public static String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formatter.format(date);
    }

    public static String formatWithFrenchMonthName(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Le' dd MMMM yyyy", Locale.FRENCH);
        return formatter.format(date);
    }
}
