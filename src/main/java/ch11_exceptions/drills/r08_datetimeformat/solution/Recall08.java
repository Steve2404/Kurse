package ch11_exceptions.drills.r08_datetimeformat.solution;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * SOLUTION du drill de rappel 8 - DateTimeFormatter : motifs, styles, echappement, erreurs.
 */
public class Recall08 {

    static String v(String text) {
        return text.replace(' ', '_').replace(' ', '_');
    }

    public static void main(String[] args) {
        LocalDateTime t = LocalDateTime.of(2026, 7, 4, 15, 5, 9);
        System.out.println("D01 : " + t.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " | " + t.format(DateTimeFormatter.ofPattern("d/M/yy h:m a", Locale.US))
                + " | " + t.format(DateTimeFormatter.ofPattern("MMM MMMM E EEEE", Locale.US)));
        DateTimeFormatter french = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.FRANCE);
        System.out.println("D02 : " + t.format(french) + " | " + french.format(t) + " | " + french.withLocale(Locale.GERMANY).format(t));
        System.out.println("D03 : " + t.format(DateTimeFormatter.ofPattern("'Le' dd 'a' HH'h'mm")) + " | " + t.format(DateTimeFormatter.ofPattern("hh 'o''clock'")));
        System.out.println(v("D04 : " + DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.US).format(t) + " | "
                + DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).withLocale(Locale.FRANCE).format(t) + " | "
                + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.US).format(t) + " | "
                + DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(Locale.GERMANY).format(t)));
        ZonedDateTime z = t.atZone(ZoneId.of("Europe/Paris"));
        System.out.println("D05 : " + z.format(DateTimeFormatter.ofPattern("HH:mm z VV xxx", Locale.US)) + " | " + DateTimeFormatter.ISO_LOCAL_DATE.format(t) + " | "
                + DateTimeFormatter.ISO_LOCAL_TIME.format(t));
        String a;
        try {
            a = LocalTime.of(8, 0).format(DateTimeFormatter.ofPattern("yyyy"));
        } catch (DateTimeException e) {
            a = e.getClass().getSimpleName();
        }
        String b;
        try {
            b = LocalDate.parse("04/07/2026", DateTimeFormatter.ofPattern("dd-MM-yyyy")).toString();
        } catch (DateTimeParseException e) {
            b = "index " + e.getErrorIndex();
        }
        String c = LocalDate.parse("04/07/2026", DateTimeFormatter.ofPattern("dd/MM/yyyy")).toString();
        System.out.println("D06 : " + a + " | " + b + " | " + c);
        // Deux styles : date SHORT, heure MEDIUM. LONG pour l'heure exige un fuseau : un ZonedDateTime convient.
        System.out.println(v("D07 : " + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM).withLocale(Locale.US).format(t) + " | "
                + DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).withLocale(Locale.US).format(z)));
    }
}
