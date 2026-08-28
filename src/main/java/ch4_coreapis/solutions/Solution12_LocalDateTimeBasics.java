package ch4_coreapis.solutions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise12_LocalDateTimeBasics.
 */
public class Solution12_LocalDateTimeBasics {

    public static LocalDate buildBirthday(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    public static LocalDateTime oneWeekLater(LocalDateTime start) {
        return start.plusWeeks(1);
    }

    public static LocalDateTime combineDateAndTime(LocalDate date, LocalTime time) {
        return date.atTime(time);
    }
}
