package ch4_coreapis.solutions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise14_ZonedDateTimeAndDst.
 */
public class Solution14_ZonedDateTimeAndDst {

    public static ZonedDateTime toNewYorkZone(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.of("America/New_York"));
    }

    public static long hoursElapsed(ZonedDateTime start, ZonedDateTime end) {
        return Duration.between(start, end).toHours();
    }
}
