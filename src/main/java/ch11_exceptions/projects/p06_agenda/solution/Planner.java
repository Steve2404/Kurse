package ch11_exceptions.projects.p06_agenda.solution;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * SOLUTION - les calculs de calendrier : N-ieme jour de semaine du mois, jours ouvres.
 */
public final class Planner {

    private Planner() {
    }

    // Le 1er jour de semaine voulu du mois, puis + (n - 1) semaines.
    public static List<LocalDate> nthWeekday(int n, DayOfWeek day, YearMonth first, int count) {
        List<LocalDate> out = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            LocalDate d = first.plusMonths(i).atDay(1);
            while (d.getDayOfWeek() != day) {
                d = d.plusDays(1);
            }
            out.add(d.plusWeeks(n - 1));
        }
        return out;
    }

    public static boolean isBusinessDay(LocalDate d, Set<LocalDate> holidays) {
        return d.getDayOfWeek() != DayOfWeek.SATURDAY && d.getDayOfWeek() != DayOfWeek.SUNDAY && !holidays.contains(d);
    }

    // Avancer de "days" jours OUVRES (ni week-end, ni ferie) ; on liste les jours sautes.
    public static LocalDate addBusinessDays(LocalDate start, int days, Set<LocalDate> holidays, List<LocalDate> skipped) {
        LocalDate d = start;
        int left = days;
        while (left > 0) {
            d = d.plusDays(1);
            if (isBusinessDay(d, holidays)) {
                left--;
            } else {
                skipped.add(d);
            }
        }
        return d;
    }
}
