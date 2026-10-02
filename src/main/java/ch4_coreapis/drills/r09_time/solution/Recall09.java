package ch4_coreapis.drills.r09_time.solution;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * SOLUTION du drill de rappel 9 - Duration, Instant, fuseaux, changement d'heure.
 */
public class Recall09 {

    public static void main(String[] args) {
        System.out.println("D01 : " + Duration.ofDays(1) + " " + Duration.ofHours(36) + " " + Duration.ofMinutes(75) + " " + Duration.ofSeconds(61) + " "
                + Duration.ofMillis(1500));
        Duration d = Duration.between(LocalTime.of(9, 0), LocalTime.of(17, 45));
        System.out.println("D02 : " + d + " " + d.toMinutes() + " " + d.toHours() + " " + Duration.between(LocalTime.of(17, 0), LocalTime.of(9, 0)));
        LocalDateTime start = LocalDateTime.of(2026, 5, 4, 10, 30, 45);
        System.out.println("D03 : " + ChronoUnit.HOURS.between(start, start.plusMinutes(150)) + " " + ChronoUnit.MINUTES.between(start, start.plusMinutes(150))
                + " " + start.truncatedTo(ChronoUnit.MINUTES) + " " + start.truncatedTo(ChronoUnit.DAYS));
        ZoneId paris = ZoneId.of("Europe/Paris");
        ZoneId tokyo = ZoneId.of("Asia/Tokyo");
        ZonedDateTime meeting = ZonedDateTime.of(LocalDateTime.of(2026, 6, 1, 9, 0), paris);
        System.out.println("D04 : " + meeting + " " + meeting.withZoneSameInstant(tokyo) + " " + meeting.withZoneSameLocal(tokyo));
        Instant instant = meeting.toInstant();
        System.out.println("D05 : " + instant + " " + Instant.ofEpochSecond(86_400) + " " + instant.plus(Duration.ofHours(1)) + " "
                + ChronoUnit.DAYS.between(Instant.EPOCH, instant));
        ZonedDateTime beforeChange = ZonedDateTime.of(LocalDateTime.of(2026, 3, 29, 1, 30), paris);
        System.out.println("D06 : " + beforeChange.plusHours(1) + " " + beforeChange.plusHours(2));
        ZonedDateTime autumn = ZonedDateTime.of(LocalDateTime.of(2026, 10, 24, 12, 0), paris);
        System.out.println("D07 : " + autumn.plusDays(1) + " " + autumn.plusHours(24) + " " + Duration.between(autumn, autumn.plusDays(1)));
        ZonedDateTime gap = ZonedDateTime.of(LocalDateTime.of(2026, 3, 29, 2, 15), paris);
        ZonedDateTime overlap = ZonedDateTime.of(LocalDateTime.of(2026, 10, 25, 2, 15), paris);
        System.out.println("D08 : " + gap.toLocalTime() + " " + overlap.getOffset() + " " + overlap.withLaterOffsetAtOverlap().getOffset() + " "
                + overlap.withEarlierOffsetAtOverlap().getOffset());
    }
}
