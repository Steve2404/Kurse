package ch4_coreapis.drills.r11_moredates.solution;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * SOLUTION du drill de rappel 11 - parse, combinaisons, epoch, constantes, unites, until, decalages.
 */
public class Recall11 {

    public static void main(String[] args) {
        // parse lit le format ISO de toString ; un autre format demande un DateTimeFormatter (chapitre 11).
        LocalDate date = LocalDate.parse("2026-02-28");
        LocalTime time = LocalTime.parse("23:59:30");
        System.out.println("D01 : " + date + " " + time + " " + LocalDateTime.parse("2026-02-28T23:59") + " " + Period.parse("P1Y2M") + " "
                + Duration.parse("PT1H30M").toMinutes());
        System.out.println("D02 : " + date.atTime(9, 30) + " " + date.atStartOfDay() + " " + time.atDate(date) + " "
                + date.atStartOfDay(ZoneId.of("Europe/Paris")));
        System.out.println("D03 : " + date.toEpochDay() + " " + LocalDate.ofEpochDay(0) + " " + Instant.ofEpochMilli(1500) + " "
                + ZonedDateTime.of(LocalDateTime.of(1970, 1, 1, 1, 0), ZoneOffset.UTC).toEpochSecond());
        System.out.println("D04 : " + LocalTime.MIDNIGHT + " " + LocalTime.NOON + " " + LocalTime.MAX + " " + LocalTime.MIN.equals(LocalTime.MIDNIGHT) + " "
                + ZoneOffset.UTC);
        System.out.println("D05 : " + date.plus(2, ChronoUnit.WEEKS) + " " + date.plus(1, ChronoUnit.DECADES) + " " + time.plusSeconds(45) + " "
                + time.plusNanos(1_000_000) + " " + date.minusDays(60));
        LocalDate christmas = LocalDate.of(2026, 12, 25);
        Duration d = Duration.ofMinutes(1500);
        System.out.println("D06 : " + date.until(christmas) + " " + date.until(christmas, ChronoUnit.DAYS) + " " + d.toDays() + " " + d.toHours() + " "
                + d.toSeconds() + " " + d.toHoursPart() + " " + d.toMinutesPart());
        ZonedDateTime india = LocalDateTime.of(2026, 7, 1, 12, 0).atZone(ZoneId.of("Asia/Kolkata"));
        System.out.println("D07 : " + india.getOffset() + " " + ZoneOffset.of("+05:30").equals(india.getOffset()) + " " + india.getZone() + " "
                + india.toLocalDateTime() + " " + india.withZoneSameInstant(ZoneOffset.UTC).toLocalTime());
        // DayOfWeek et Month sont des enum CYCLIQUES : plus/minus tournent en boucle.
        System.out.println("D08 : " + date.getMonth().plus(11) + " " + DayOfWeek.MONDAY.minus(1) + " " + date.with(DayOfWeek.MONDAY) + " "
                + Month.FEBRUARY.length(true) + " " + date.lengthOfYear() + " " + date.getDayOfWeek().getValue() + " " + Month.of(12));
    }
}
