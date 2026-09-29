package ch4_coreapis.solutions;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Corrige de l'exercice 26. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise26_PlanningCalculator.
 */
public class Solution26_PlanningCalculator {

    public static LocalDate[] installments(LocalDate start, int count) {
        // Toujours depuis start : enchainer plusMonths(1) ferait perdre le 31 apres fevrier.
        LocalDate[] dates = new LocalDate[count];
        for (int i = 0; i < count; i++) {
            dates[i] = start.plusMonths(i);
        }
        return dates;
    }

    public static boolean isWeekend(LocalDate date) {
        // DayOfWeek est un enum : == suffit.
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    public static LocalDate nextBusinessDay(LocalDate date) {
        // Immuable : on RECUPERE chaque plusDays.
        LocalDate next = date.plusDays(1);
        while (isWeekend(next)) {
            next = next.plusDays(1);
        }
        return next;
    }

    public static int businessDaysBetween(LocalDate from, LocalDate to) {
        // isBefore : to est EXCLU ; la boucle avance d'un jour a chaque tour.
        int count = 0;
        for (LocalDate d = from; d.isBefore(to); d = d.plusDays(1)) {
            if (!isWeekend(d)) {
                count++;
            }
        }
        return count;
    }

    public static int age(LocalDate birth, LocalDate today) {
        // Period.between compte les annees COMPLETES (un 29 fevrier attend le 29, ou le 1er mars).
        return Period.between(birth, today).getYears();
    }

    public static LocalDate lastFridayOfMonth(LocalDate anyDay) {
        // Depuis le dernier jour du mois, on recule jusqu'au vendredi.
        LocalDate d = anyDay.withDayOfMonth(anyDay.lengthOfMonth());
        while (d.getDayOfWeek() != DayOfWeek.FRIDAY) {
            d = d.minusDays(1);
        }
        return d;
    }

    public static LocalTime localTimeIn(ZonedDateTime meeting, String zone) {
        // SameInstant : le meme moment, lu sur l'horloge d'un autre fuseau.
        return meeting.withZoneSameInstant(ZoneId.of(zone)).toLocalTime();
    }
}
