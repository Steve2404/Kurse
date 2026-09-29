package ch4_coreapis.drills.exercises;

import ch4_coreapis.ExerciseChecker;
import ch4_coreapis.drills.Journal;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;

/**
 * DRILL 05 - L'API java.time : une methode par TODO
 * =================================================
 *
 * Mode d'emploi : voir Drill01_StringApi. Toutes ces classes sont
 * IMMUABLES : chaque plus/minus/with rend un NOUVEL objet.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : build()             [LocalDate.of] le 31 janvier 2024 -> egal a Journal.START.
 * TODO 2  : oneMonthLater()     [plusMonths] START -> 2024-02-29 (se cale sur le dernier jour).
 * TODO 3  : weekday()           [getDayOfWeek] START -> WEDNESDAY.
 * TODO 4  : leap()              [isLeapYear] START -> true.
 * TODO 5  : inFebruary()        [withMonth] START.withMonth(2) -> 2024-02-29.
 * TODO 6  : openingOfStart()    [atTime] START a OPENING -> 2024-01-31T09:15.
 * TODO 7  : workDay()           [Duration.between] OPENING -> CLOSING -> PT8H30M.
 * TODO 8  : workMinutesPart()   [toMinutesPart] de workDay() -> 30.
 * TODO 9  : periodToMarch10()   [Period.between] START -> 2024-03-10 -> P1M10D.
 * TODO 10 : daysToMarch10()     [ChronoUnit.DAYS.between] START -> 2024-03-10 -> 39.
 * TODO 11 : parsed()            [LocalDate.parse] "2024-03-10".
 * TODO 12 : oneWeekLater()      [plus(Period)] START + Period.ofWeeks(1) -> 2024-02-07.
 * TODO 13 : periodText()        [Period.of] (1, 2, 3).toString() -> "P1Y2M3D".
 * TODO 14 : durationText()      [Duration.ofMinutes] 150 -> "PT2H30M".
 * TODO 15 : closingHour()       [truncatedTo] CLOSING -> 17:00.
 * TODO 16 : hourAfterMeetingNy() [atZone + plusHours] MEETING a New York + 1 h -> getHour() == 3 (heure d'ete !).
 * TODO 17 : meetingHourInParis() [withZoneSameInstant] MEETING a New York, vu de Paris -> 7.
 * TODO 18 : endOfMonth()        [lengthOfMonth + withDayOfMonth] 2024-02-10 -> 2024-02-29.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   of(...) now() parse(...)   (jamais new)
 *   plusDays/Weeks/Months/Years, plusHours/Minutes/Seconds, minus..., with..., plus(Period / Duration)
 *   getYear getMonth getMonthValue getDayOfMonth getDayOfWeek getHour isLeapYear lengthOfMonth
 *   isBefore isAfter isEqual   date.atTime(t)   time.atDate(d)   date.atStartOfDay()
 *   Period (dates) : of(a, m, j) ofDays ofWeeks ofMonths ofYears between -> "P1Y2M3D"
 *   Duration (heures) : ofMinutes ofHours ofSeconds between toHours toMinutes toMinutesPart -> "PT2H30M"
 *   ChronoUnit.DAYS.between(a, b)      truncatedTo(ChronoUnit.HOURS)
 *   ZonedDateTime : ldt.atZone(zone), withZoneSameInstant(zone) ; heure inexistante -> avancee
 * ---------------------------------------------------------------------
 */
public class Drill05_DateTimeApi {

    public static LocalDate build() {
        throw new UnsupportedOperationException("TODO 1 : implementer build()");
    }

    public static LocalDate oneMonthLater() {
        throw new UnsupportedOperationException("TODO 2 : implementer oneMonthLater()");
    }

    public static DayOfWeek weekday() {
        throw new UnsupportedOperationException("TODO 3 : implementer weekday()");
    }

    public static boolean leap() {
        throw new UnsupportedOperationException("TODO 4 : implementer leap()");
    }

    public static LocalDate inFebruary() {
        throw new UnsupportedOperationException("TODO 5 : implementer inFebruary()");
    }

    public static LocalDateTime openingOfStart() {
        throw new UnsupportedOperationException("TODO 6 : implementer openingOfStart()");
    }

    public static Duration workDay() {
        throw new UnsupportedOperationException("TODO 7 : implementer workDay()");
    }

    public static int workMinutesPart() {
        throw new UnsupportedOperationException("TODO 8 : implementer workMinutesPart()");
    }

    public static Period periodToMarch10() {
        throw new UnsupportedOperationException("TODO 9 : implementer periodToMarch10()");
    }

    public static long daysToMarch10() {
        throw new UnsupportedOperationException("TODO 10 : implementer daysToMarch10()");
    }

    public static LocalDate parsed() {
        throw new UnsupportedOperationException("TODO 11 : implementer parsed()");
    }

    public static LocalDate oneWeekLater() {
        throw new UnsupportedOperationException("TODO 12 : implementer oneWeekLater()");
    }

    public static String periodText() {
        throw new UnsupportedOperationException("TODO 13 : implementer periodText()");
    }

    public static String durationText() {
        throw new UnsupportedOperationException("TODO 14 : implementer durationText()");
    }

    public static LocalTime closingHour() {
        throw new UnsupportedOperationException("TODO 15 : implementer closingHour()");
    }

    public static int hourAfterMeetingNy() {
        throw new UnsupportedOperationException("TODO 16 : implementer hourAfterMeetingNy()");
    }

    public static int meetingHourInParis() {
        throw new UnsupportedOperationException("TODO 17 : implementer meetingHourInParis()");
    }

    public static LocalDate endOfMonth() {
        throw new UnsupportedOperationException("TODO 18 : implementer endOfMonth()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  build() == START", build().equals(Journal.START));
        ExerciseChecker.check("2  oneMonthLater() == 2024-02-29", oneMonthLater().equals(LocalDate.of(2024, 2, 29)));
        ExerciseChecker.check("3  weekday() == WEDNESDAY", weekday() == DayOfWeek.WEDNESDAY);
        ExerciseChecker.check("4  leap()", leap());
        ExerciseChecker.check("5  inFebruary() == 2024-02-29", inFebruary().equals(LocalDate.of(2024, 2, 29)));
        ExerciseChecker.check("6  openingOfStart() == 2024-01-31T09:15", openingOfStart().equals(LocalDateTime.of(2024, 1, 31, 9, 15)));
        ExerciseChecker.check("7  workDay() == PT8H30M", workDay().toString().equals("PT8H30M"));
        ExerciseChecker.check("8  workMinutesPart() == 30", workMinutesPart() == 30);
        ExerciseChecker.check("9  periodToMarch10() == P1M10D", periodToMarch10().toString().equals("P1M10D"));
        ExerciseChecker.check("10 daysToMarch10() == 39", daysToMarch10() == 39);
        ExerciseChecker.check("11 parsed() == 2024-03-10", parsed().equals(LocalDate.of(2024, 3, 10)));
        ExerciseChecker.check("12 oneWeekLater() == 2024-02-07", oneWeekLater().equals(LocalDate.of(2024, 2, 7)));
        ExerciseChecker.check("13 periodText() == \"P1Y2M3D\"", periodText().equals("P1Y2M3D"));
        ExerciseChecker.check("14 durationText() == \"PT2H30M\"", durationText().equals("PT2H30M"));
        ExerciseChecker.check("15 closingHour() == 17:00", closingHour().equals(LocalTime.of(17, 0)));
        ExerciseChecker.check("16 hourAfterMeetingNy() == 3", hourAfterMeetingNy() == 3);
        ExerciseChecker.check("17 meetingHourInParis() == 7", meetingHourInParis() == 7);
        ExerciseChecker.check("18 endOfMonth() == 2024-02-29", endOfMonth().equals(LocalDate.of(2024, 2, 29)));

        ExerciseChecker.summary();
    }
}
