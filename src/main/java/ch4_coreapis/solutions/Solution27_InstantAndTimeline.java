package ch4_coreapis.solutions;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Corrige de l'exercice 27. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise27_InstantAndTimeline.
 */
public class Solution27_InstantAndTimeline {

    public static Instant parseUtc(String text) {
        // Format ISO strict : secondes et Z obligatoires.
        return Instant.parse(text);
    }

    public static long epochMillis(Instant instant) {
        // toEpochMilli : millisecondes depuis 1970-01-01T00:00:00Z (getEpochSecond pour les secondes).
        return instant.toEpochMilli();
    }

    public static Instant fromLocal(LocalDateTime dateTime, String zone) {
        // Une heure locale n'est un instant qu'une fois le fuseau connu.
        return dateTime.atZone(ZoneId.of(zone)).toInstant();
    }

    public static boolean sameMoment(ZonedDateTime a, ZonedDateTime b) {
        // equals compare aussi le fuseau ; les instants, eux, sont universels.
        return a.toInstant().equals(b.toInstant());
    }

    public static long minutesBetween(Instant a, Instant b) {
        // Duration marche entre deux Instant (ce sont des secondes, pas des dates).
        return Duration.between(a, b).toMinutes();
    }

    public static LocalTime localTimeAt(Instant instant, String zone) {
        // Instant n'a pas d'heure lisible : atZone la calcule pour un fuseau.
        return instant.atZone(ZoneId.of(zone)).toLocalTime();
    }

    public static Duration realDayLength(LocalDate date, String zone) {
        // Mesurer entre deux INSTANTS : le changement d'heure donne 23 ou 25 heures.
        ZoneId id = ZoneId.of(zone);
        Instant start = date.atStartOfDay(id).toInstant();
        Instant end = date.plusDays(1).atStartOfDay(id).toInstant();
        return Duration.between(start, end);
    }

    public static Instant earliest(String... isoTexts) {
        // isBefore compare deux points de la ligne du temps.
        Instant best = parseUtc(isoTexts[0]);
        for (String text : isoTexts) {
            Instant current = parseUtc(text);
            if (current.isBefore(best)) {
                best = current;
            }
        }
        return best;
    }
}
