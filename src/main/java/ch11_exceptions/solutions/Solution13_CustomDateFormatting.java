package ch11_exceptions.solutions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.exercises.Exercise13_CustomDateFormatting.
 */
public class Solution13_CustomDateFormatting {

    public static String formatDate(LocalDate date) {
        // MM = mois (mm serait les minutes : exception sur un LocalDate).
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return formatter.format(date);
    }

    public static String formatWithFrenchMonthName(LocalDate date) {
        // MMMM + Locale.FRENCH = nom complet du mois en francais ; 'Le' est du texte recopie.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Le' dd MMMM yyyy", Locale.FRENCH);
        return formatter.format(date);
    }
}
