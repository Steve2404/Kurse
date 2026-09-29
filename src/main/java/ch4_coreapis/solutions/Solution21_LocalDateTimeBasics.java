package ch4_coreapis.solutions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Corrige de l'exercice 21. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise21_LocalDateTimeBasics.
 */
public class Solution21_LocalDateTimeBasics {

    public static LocalDate buildBirthday(int year, int month, int day) {
        // Pas de constructeur public : LocalDate.of est la porte d'entree.
        return LocalDate.of(year, month, day);
    }

    public static LocalDateTime oneWeekLater(LocalDateTime start) {
        // Immuable : plusWeeks rend une NOUVELLE date, start ne change pas.
        return start.plusWeeks(1);
    }

    public static LocalDateTime combineDateAndTime(LocalDate date, LocalTime time) {
        // atTime assemble une date et une heure en LocalDateTime.
        return date.atTime(time);
    }
}
