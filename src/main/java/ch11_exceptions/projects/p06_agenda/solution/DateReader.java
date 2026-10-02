package ch11_exceptions.projects.p06_agenda.solution;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * SOLUTION - une chaine de formats essayes l'un apres l'autre (repli).
 */
public class DateReader {

    private final List<DateTimeFormatter> formats = new ArrayList<>();

    public DateReader(String[] patterns) {
        formats.add(DateTimeFormatter.ISO_LOCAL_DATE_TIME);   // predefini, et STRICT
        for (String pattern : patterns) {
            // Les noms de mois ("March") dependent de la locale : on la fixe.
            formats.add(DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH));
        }
    }

    public LocalDateTime read(String text) throws UnreadableDateException {
        List<DateTimeParseException> failures = new ArrayList<>();
        for (DateTimeFormatter format : formats) {
            try {
                return LocalDateTime.parse(text, format);
            } catch (DateTimeParseException e) {          // non verifiee : on l'attrape quand meme pour essayer la suite
                failures.add(e);
            }
        }
        UnreadableDateException e = new UnreadableDateException(text);
        failures.forEach(e::addSuppressed);
        throw e;
    }
}
