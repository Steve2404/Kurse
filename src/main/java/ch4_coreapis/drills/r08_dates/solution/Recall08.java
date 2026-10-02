package ch4_coreapis.drills.r08_dates.solution;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;

/**
 * SOLUTION du drill de rappel 8 - LocalDate, LocalTime, LocalDateTime, Period.
 */
public class Recall08 {

    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2026, Month.JANUARY, 20);
        LocalTime time = LocalTime.of(6, 15);
        LocalDateTime both = LocalDateTime.of(date, time);
        System.out.println("D01 : " + date + " " + time + " " + both + " " + LocalTime.of(6, 15, 30) + " " + LocalDateTime.of(2026, 1, 20, 6, 15, 0));
        LocalDate d = date;
        d.plusDays(10);
        d = d.plusDays(2).plusWeeks(1).minusMonths(1).plusYears(1);
        System.out.println("D02 : " + date + " " + d);
        System.out.println("D03 : " + LocalDate.of(2024, 2, 29).plusYears(1) + " " + LocalDate.of(2026, 3, 31).minusMonths(1) + " " + LocalDate.of(2026, 12, 31).plusDays(1));
        System.out.println("D04 : " + date.getDayOfWeek() + " " + date.getMonth() + " " + date.getMonthValue() + " " + date.getDayOfYear() + " " + date.isLeapYear()
                + " " + (date.getDayOfWeek() == DayOfWeek.TUESDAY));
        System.out.println("D05 : " + time.plusMinutes(50) + " " + time.minusHours(7) + " " + time.withHour(23).plusHours(2) + " " + both.plusHours(20).toLocalDate());
        Period p = Period.of(1, 2, 3);
        System.out.println("D06 : " + p + " " + Period.ofWeeks(2) + " " + Period.ofDays(1).ofMonths(3) + " " + Period.ofMonths(18).normalized() + " " + Period.ZERO);
        System.out.println("D07 : " + date.plus(p) + " " + Period.between(LocalDate.of(2000, 5, 15), date) + " " + Period.between(date, LocalDate.of(2025, 12, 25)));
        System.out.println("D08 : " + date.isBefore(d) + " " + date.isAfter(d) + " " + date.equals(LocalDate.of(2026, 1, 20)) + " " + date.withDayOfMonth(1) + " " + date.withMonth(2));
    }
}
