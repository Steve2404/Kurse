package ch4_coreapis.solutions;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Corrige de l'exercice 24. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise24_ZonedDateTimeAndDst.
 */
public class Solution24_ZonedDateTimeAndDst {

    public static ZonedDateTime toNewYorkZone(LocalDateTime localDateTime) {
        // atZone attache un fuseau ; une heure inexistante (heure d'ete) est avancee.
        return localDateTime.atZone(ZoneId.of("America/New_York"));
    }

    public static long hoursElapsed(ZonedDateTime start, ZonedDateTime end) {
        // Duration.between sur des ZonedDateTime compte les VRAIES heures ecoulees, heure d'ete comprise.
        return Duration.between(start, end).toHours();
    }
}
