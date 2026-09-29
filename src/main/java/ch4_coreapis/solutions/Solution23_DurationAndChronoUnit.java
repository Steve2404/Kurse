package ch4_coreapis.solutions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Corrige de l'exercice 23. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch4_coreapis.exercises.Exercise23_DurationAndChronoUnit.
 */
public class Solution23_DurationAndChronoUnit {

    public static Duration workTime(LocalTime start, LocalTime end) {
        // LocalTime ignore le changement de jour : un resultat negatif signifie "le lendemain".
        Duration d = Duration.between(start, end);
        if (d.isNegative()) {
            d = d.plusHours(24);
        }
        return d;
    }

    public static String formatHm(Duration d) {
        // toHours = heures totales ; toMinutesPart = le reste (0 a 59) ; %02d ajoute le 0.
        return String.format("%dh%02d", d.toHours(), d.toMinutesPart());
    }

    public static long daysBetween(LocalDate from, LocalDate to) {
        // Duration refuse les LocalDate (pas de secondes) : ChronoUnit compte les jours.
        return ChronoUnit.DAYS.between(from, to);
    }

    public static long fullMonths(LocalDate from, LocalDate to) {
        // Seuls les mois COMPLETS comptent : 31 janvier -> 29 fevrier = 0.
        return ChronoUnit.MONTHS.between(from, to);
    }

    public static LocalDateTime endOf(LocalDateTime start, int minutes) {
        // LocalDateTime accepte une Duration et passe minuit tout seul.
        return start.plus(Duration.ofMinutes(minutes));
    }

    public static Duration totalDuration(String... parts) {
        // Immuable : on RECUPERE le resultat de plus() a chaque tour.
        Duration total = Duration.ZERO;
        for (String part : parts) {
            total = total.plus(Duration.parse(part));
        }
        return total;
    }

    public static LocalTime startOfHour(LocalTime t) {
        // truncatedTo met a zero tout ce qui est plus petit que l'unite donnee.
        return t.truncatedTo(ChronoUnit.HOURS);
    }
}
