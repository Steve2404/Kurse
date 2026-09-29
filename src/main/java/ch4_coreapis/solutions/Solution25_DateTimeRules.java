package ch4_coreapis.solutions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Corrige de l'exercice 25. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise25_DateTimeRules.
 */
public class Solution25_DateTimeRules {

    public static boolean supports(String type, ChronoUnit unit) {
        // Calendrier (jours et plus) contre horloge (moins d'un jour) ; Instant s'arrete a DAYS (24 h fixes).
        boolean dateUnit = unit == ChronoUnit.DAYS || unit == ChronoUnit.WEEKS
                || unit == ChronoUnit.MONTHS || unit == ChronoUnit.YEARS;
        return switch (type) {
            case "LocalDate" -> dateUnit;
            case "LocalTime" -> !dateUnit;
            case "LocalDateTime", "ZonedDateTime" -> true;
            case "Instant" -> !dateUnit || unit == ChronoUnit.DAYS;
            default -> throw new IllegalArgumentException(type);
        };
    }

    public static boolean isValidDate(int year, int month, int day) {
        // Le mois d'abord : daysInMonth n'a pas de sens pour le mois 13.
        if (month < 1 || month > 12) {
            return false;
        }
        return day >= 1 && day <= daysInMonth(year, month);
    }

    private static boolean isLeap(int year) {
        // Divisible par 4, sauf les siecles, sauf ceux divisibles par 400.
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    private static int daysInMonth(int year, int month) {
        // Un switch expression par groupe de mois.
        return switch (month) {
            case 2 -> isLeap(year) ? 29 : 28;
            case 4, 6, 9, 11 -> 30;
            default -> 31;
        };
    }

    public static Period yearAndTwoWeeks() {
        // plusDays est une methode d'INSTANCE : elle garde l'annee (ofWeeks, statique, l'effacerait).
        return Period.ofYears(1).plusDays(14);
    }

    public static LocalDateTime startPlusHours(LocalDate date, int hours) {
        // LocalDate n'a pas plusHours : on passe par un LocalDateTime a minuit.
        return date.atStartOfDay().plusHours(hours);
    }
}
