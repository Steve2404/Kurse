package ch4_coreapis.drills.solutions;

import ch4_coreapis.drills.Journal;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Corrige du drill 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.drills.exercises.Drill05_DateTimeApi.
 */
public class SolutionDrill05_DateTimeApi {

    public static LocalDate build() {
        // Pas de new : les constructeurs sont prives, of() les remplace.
        return LocalDate.of(2024, 1, 31);
    }

    public static LocalDate oneMonthLater() {
        // Le 31 fevrier n'existe pas : plusMonths se cale sur le dernier jour valide.
        return Journal.START.plusMonths(1);
    }

    public static DayOfWeek weekday() {
        // DayOfWeek est un enum (MONDAY a SUNDAY).
        return Journal.START.getDayOfWeek();
    }

    public static boolean leap() {
        // 2024 est divisible par 4 et n'est pas un siecle.
        return Journal.START.isLeapYear();
    }

    public static LocalDate inFebruary() {
        // withMonth garde le jour s'il existe, sinon se cale (31 -> 29).
        return Journal.START.withMonth(2);
    }

    public static LocalDateTime openingOfStart() {
        // atTime assemble une date et une heure.
        return Journal.START.atTime(Journal.OPENING);
    }

    public static Duration workDay() {
        // Duration pour des heures ; Period refuserait des LocalTime.
        return Duration.between(Journal.OPENING, Journal.CLOSING);
    }

    public static int workMinutesPart() {
        // toMinutesPart = minutes restantes apres les heures (toMinutes donnerait 510).
        return workDay().toMinutesPart();
    }

    public static Period periodToMarch10() {
        // Period.between compte mois puis jours : 31/01 + 1 mois = 29/02, puis 10 jours.
        return Period.between(Journal.START, LocalDate.of(2024, 3, 10));
    }

    public static long daysToMarch10() {
        // Le nombre TOTAL de jours : ChronoUnit, pas Period.getDays().
        return ChronoUnit.DAYS.between(Journal.START, LocalDate.of(2024, 3, 10));
    }

    public static LocalDate parsed() {
        // parse attend le format ISO aaaa-mm-jj (avec les zeros).
        return LocalDate.parse("2024-03-10");
    }

    public static LocalDate oneWeekLater() {
        // plus(Period) marche sur une LocalDate (plus(Duration) lancerait une exception).
        return Journal.START.plus(Period.ofWeeks(1));
    }

    public static String periodText() {
        // toString ISO : P, puis annees, mois, jours.
        return Period.of(1, 2, 3).toString();
    }

    public static String durationText() {
        // toString ISO : PT, puis heures, minutes (jamais de jours).
        return Duration.ofMinutes(150).toString();
    }

    public static LocalTime closingHour() {
        // truncatedTo met a zero les minutes et secondes.
        return Journal.CLOSING.truncatedTo(ChronoUnit.HOURS);
    }

    public static int hourAfterMeetingNy() {
        // Le 10 mars a New York, 02:00 saute a 03:00 : 01:30 + 1 h = 03:30.
        return Journal.MEETING.atZone(Journal.NEW_YORK).plusHours(1).getHour();
    }

    public static int meetingHourInParis() {
        // Meme instant, autre horloge : 01:30 a New York (UTC-5) = 06:30 UTC = 07:30 a Paris (UTC+1).
        return Journal.MEETING.atZone(Journal.NEW_YORK).withZoneSameInstant(Journal.PARIS).getHour();
    }

    public static LocalDate endOfMonth() {
        // lengthOfMonth connait fevrier bissextile : 29.
        LocalDate d = LocalDate.of(2024, 2, 10);
        return d.withDayOfMonth(d.lengthOfMonth());
    }
}
