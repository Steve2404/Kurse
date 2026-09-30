package ch11_exceptions.drills.exercises;

import ch11_exceptions.ExerciseChecker;

import java.time.LocalDate;

/**
 * DRILL 04 - Formater et relire des dates : DateTimeFormatter
 * ===========================================================
 *
 * Mode d'emploi : voir Drill01_ExceptionBasics. Donnee : Ledger.WHEN =
 * 2024-03-07T14:05:09 (un jeudi).
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1  : iso()           [DateTimeFormatter.ISO_LOCAL_DATE] la date de WHEN -> 2024-03-07.
 * TODO 2  : isoDateTime()   [ISO_LOCAL_DATE_TIME] WHEN -> 2024-03-07T14:05:09.
 * TODO 3  : dayMonthYear()  [ofPattern("dd/MM/yyyy")] -> 07/03/2024.
 * TODO 4  : time12()        [ofPattern("hh:mm a", Locale.US)] -> 02:05 PM.
 * TODO 5  : monthName()     [MMMM + Locale.FRENCH] -> mars.
 * TODO 6  : withText()      [texte entre apostrophes] "'Le' d MMMM" en francais -> Le 7 mars.
 * TODO 7  : shortStyle()    [ofLocalizedDate(FormatStyle.SHORT).withLocale(Locale.US)] -> 3/7/24.
 * TODO 8  : mediumStyle()   [FormatStyle.MEDIUM] -> Mar 7, 2024.
 * TODO 9  : parse(text)     [LocalDate.parse(texte, formatter)] "25/12/2024" -> 2024-12-25.
 * TODO 10 : wrongField()    [UnsupportedTemporalTypeException] formater la DATE de WHEN avec "HH:mm" ; rendre le nom simple de l'exception.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   y annee ; M mois (MM 03, MMM Mar, MMMM March) ; d jour ; E jour de semaine (EEE / EEEE)
 *   H 0-23 ; h 1-12 ; m MINUTES ; s secondes ; a AM/PM ; 'texte' ; '' = une apostrophe
 *   DateTimeFormatter.ofPattern(motif) / ofPattern(motif, locale) ; ISO_LOCAL_DATE, ISO_LOCAL_DATE_TIME...
 *   ofLocalizedDate / ofLocalizedTime / ofLocalizedDateTime(FormatStyle.SHORT | MEDIUM | LONG | FULL) + withLocale
 *   formatter.format(t) == t.format(formatter) ; LocalDate.parse(texte, formatter)
 *   un champ absent (HH sur un LocalDate) -> UnsupportedTemporalTypeException A L'EXECUTION
 *   texte illisible au parse -> DateTimeParseException (unchecked)
 * ---------------------------------------------------------------------
 */
public class Drill04_DateFormatting {

    public static String iso() {
        throw new UnsupportedOperationException("TODO 1 : implementer iso()");
    }

    public static String isoDateTime() {
        throw new UnsupportedOperationException("TODO 2 : implementer isoDateTime()");
    }

    public static String dayMonthYear() {
        throw new UnsupportedOperationException("TODO 3 : implementer dayMonthYear()");
    }

    public static String time12() {
        throw new UnsupportedOperationException("TODO 4 : implementer time12()");
    }

    public static String monthName() {
        throw new UnsupportedOperationException("TODO 5 : implementer monthName()");
    }

    public static String withText() {
        throw new UnsupportedOperationException("TODO 6 : implementer withText()");
    }

    public static String shortStyle() {
        throw new UnsupportedOperationException("TODO 7 : implementer shortStyle()");
    }

    public static String mediumStyle() {
        throw new UnsupportedOperationException("TODO 8 : implementer mediumStyle()");
    }

    public static LocalDate parse(String text) {
        throw new UnsupportedOperationException("TODO 9 : implementer parse()");
    }

    public static String wrongField() {
        throw new UnsupportedOperationException("TODO 10 : implementer wrongField()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  iso == 2024-03-07", iso().equals("2024-03-07"));
        ExerciseChecker.check("2  isoDateTime == 2024-03-07T14:05:09", isoDateTime().equals("2024-03-07T14:05:09"));
        ExerciseChecker.check("3  dayMonthYear == 07/03/2024", dayMonthYear().equals("07/03/2024"));
        ExerciseChecker.check("4  time12 == 02:05 PM", time12().equals("02:05 PM"));
        ExerciseChecker.check("5  monthName == mars", monthName().equals("mars"));
        ExerciseChecker.check("6  withText == Le 7 mars", withText().equals("Le 7 mars"));
        ExerciseChecker.check("7  shortStyle == 3/7/24", shortStyle().equals("3/7/24"));
        ExerciseChecker.check("8  mediumStyle == Mar 7, 2024", mediumStyle().equals("Mar 7, 2024"));
        ExerciseChecker.check("9  parse(25/12/2024) == 2024-12-25", parse("25/12/2024").equals(LocalDate.of(2024, 12, 25)));
        ExerciseChecker.check("10 wrongField == UnsupportedTemporalTypeException", wrongField().equals("UnsupportedTemporalTypeException"));

        ExerciseChecker.summary();
    }
}
