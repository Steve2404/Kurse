package ch4_coreapis.solutions;

import java.time.LocalDate;
import java.time.Period;

/**
 * Corrige de l'exercice 22. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise22_PeriodClass.
 */
public class Solution22_PeriodClass {

    public static int yearsBetween(LocalDate start, LocalDate end) {
        // Period.between compte des annees COMPLETES ; getYears n'est qu'une des 3 composantes.
        return Period.between(start, end).getYears();
    }

    public static LocalDate addOneMonthClamped(LocalDate date) {
        // plus(Period) se cale sur le dernier jour valide du mois (31 janvier -> 29 fevrier).
        return date.plus(Period.ofMonths(1));
    }
}
