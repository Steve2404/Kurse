package ch4_coreapis.solutions;

import java.time.LocalDate;
import java.time.Period;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise13_PeriodClass.
 */
public class Solution13_PeriodClass {

    public static int yearsBetween(LocalDate start, LocalDate end) {
        return Period.between(start, end).getYears();
    }

    public static LocalDate addOneMonthClamped(LocalDate date) {
        return date.plus(Period.ofMonths(1));
    }
}
