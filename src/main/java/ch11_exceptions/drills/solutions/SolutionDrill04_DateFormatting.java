package ch11_exceptions.drills.solutions;

import ch11_exceptions.drills.Ledger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.drills.exercises.Drill04_DateFormatting.
 */
public class SolutionDrill04_DateFormatting {

    public static String iso() {
        // Les constantes ISO ne dependent d'aucune Locale.
        return DateTimeFormatter.ISO_LOCAL_DATE.format(Ledger.WHEN.toLocalDate());
    }

    public static String isoDateTime() {
        // Le T separe la date et l'heure ; les secondes sont affichees car non nulles.
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(Ledger.WHEN);
    }

    public static String dayMonthYear() {
        // MM = mois (mm serait les minutes).
        return Ledger.WHEN.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String time12() {
        // hh = heure 1-12 sur 2 chiffres ; a = AM/PM, dont le texte depend de la Locale.
        return Ledger.WHEN.format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));
    }

    public static String monthName() {
        // MMMM = nom complet du mois, dans la langue de la Locale.
        return Ledger.WHEN.format(DateTimeFormatter.ofPattern("MMMM", Locale.FRENCH));
    }

    public static String withText() {
        // Le texte entre apostrophes est recopie ; d (sans zero) donne 7.
        return Ledger.WHEN.format(DateTimeFormatter.ofPattern("'Le' d MMMM", Locale.FRENCH));
    }

    public static String shortStyle() {
        // Un style localise : c'est la Locale qui choisit l'ordre et les separateurs.
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.US).format(Ledger.WHEN);
    }

    public static String mediumStyle() {
        // MEDIUM en US : mois abrege, jour, annee.
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.US).format(Ledger.WHEN);
    }

    public static LocalDate parse(String text) {
        // Le meme motif sert dans les deux sens.
        return LocalDate.parse(text, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static String wrongField() {
        // Compile sans probleme : c'est A L'EXECUTION que l'heure manque au LocalDate.
        try {
            return Ledger.WHEN.toLocalDate().format(DateTimeFormatter.ofPattern("HH:mm"));
        } catch (RuntimeException e) {
            return e.getClass().getSimpleName();
        }
    }
}
