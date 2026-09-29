package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * EXERCICE 23 - Duration et ChronoUnit : mesurer des heures, des minutes, des jours (niveau : difficile)
 * ======================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise21_LocalDateTimeBasics.java.
 *
 * -- Rappels verifies avec Java 17 --
 *
 *   Period   = annees/mois/jours (pour LocalDate)      toString : P1Y2M3D
 *   Duration = heures/minutes/secondes (pour l'heure)   toString : PT2H30M
 *   Duration.ofMinutes(150) -> PT2H30M     Duration.ofHours(25) -> PT25H (jamais de jours dans le texte)
 *   Duration.between(09:15, 17:45) -> PT8H30M ; toHours() 8 ; toMinutes() 510 ; toMinutesPart() 30
 *   Duration.between(22:00, 02:00) -> PT-20H (negatif : LocalTime ne sait pas qu'on a change de jour)
 *   Duration.between(LocalDate, LocalDate) -> UnsupportedTemporalTypeException: Unsupported unit: Seconds
 *   ChronoUnit.DAYS.between(2024-01-01, 2024-03-01)   -> 60 (2024 est bissextile)
 *   ChronoUnit.MONTHS.between(2024-01-31, 2024-02-29) -> 0 (un mois COMPLET n'est pas encore passe)
 *   ChronoUnit.MINUTES.between(09:50, 11:10) -> 80 ; ChronoUnit.HOURS.between(09:50, 11:10) -> 1
 *   LocalTime.of(10, 47, 33).truncatedTo(ChronoUnit.HOURS) -> 10:00
 *
 *
 * ==================================================================
 * TODO 1 : workTime(start, end)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Combien de temps dure un service ? Si on finit "avant" d'avoir
 * commence (22:00 -> 06:00), c'est qu'on a travaille la nuit : on
 * ajoute 24 heures au resultat negatif.
 *
 * -- Essayons a la main --
 *
 *   (09:15, 17:45) -> PT8H30M      (22:00, 06:00) -> PT-16H + 24H = PT8H
 *
 * -- Le plan --
 *
 *   1. d = Duration.between(start, end).
 *   2. Si d.isNegative(), d = d.plusHours(24). Rendre d.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : formatHm(d)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   PT8H30M -> "8h30"     PT8H -> "8h00"     Duration.ofMinutes(1505) -> "25h05"
 *
 * -- Le plan --
 *
 *   1. Rendre String.format("%dh%02d", d.toHours(), d.toMinutesPart()).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. toHours() = heures TOTALES ; toMinutesPart() = les minutes qui restent (0 a 59).
 *
 *
 * ==================================================================
 * TODO 3 : daysBetween(from, to)    et    TODO 4 : fullMonths(from, to)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Pour des dates, pas de Duration : on compte avec ChronoUnit. Un
 * "mois complet" n'est compte que si le meme numero de jour est atteint.
 *
 * -- Essayons a la main --
 *
 *   daysBetween(2024-01-01, 2024-03-01) = 31 + 29 = 60 ; en 2023 : 59
 *   fullMonths(2024-01-31, 2024-02-29) = 0 ; fullMonths(2024-01-31, 2024-03-31) = 2
 *
 * -- Le plan --
 *
 *   1. ChronoUnit.DAYS.between(from, to) ; ChronoUnit.MONTHS.between(from, to).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : endOf(start, minutes)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   (2024-03-10T23:30, 90) -> 2024-03-11T01:00 (LocalDateTime change de jour tout seul)
 *
 * -- Le plan --
 *
 *   1. Rendre start.plus(Duration.ofMinutes(minutes)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : totalDuration(parts...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Des durees ecrites en texte ISO ("PT1H30M") : Duration.parse les lit.
 * On les additionne en partant de Duration.ZERO (Duration est
 * immuable : d.plus(x) rend une NOUVELLE duree, a recuperer).
 *
 * -- Essayons a la main --
 *
 *   ("PT1H30M", "PT45M") -> PT2H15M      () -> PT0S
 *
 * -- Le plan --
 *
 *   1. total = Duration.ZERO ; pour chaque texte : total = total.plus(Duration.parse(texte)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : startOfHour(t)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   10:47:33 -> 10:00
 *
 * -- Le plan --
 *
 *   1. Rendre t.truncatedTo(ChronoUnit.HOURS).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - import java.time.temporal.ChronoUnit;
 *   - Comme String, les classes java.time sont IMMUABLES : d.plusHours(24) seul ne change rien.
 */
public class Exercise23_DurationAndChronoUnit {

    public static Duration workTime(LocalTime start, LocalTime end) {
        throw new UnsupportedOperationException("TODO 1 : implementer workTime()");
    }

    public static String formatHm(Duration d) {
        throw new UnsupportedOperationException("TODO 2 : implementer formatHm()");
    }

    public static long daysBetween(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("TODO 3 : implementer daysBetween()");
    }

    public static long fullMonths(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("TODO 4 : implementer fullMonths()");
    }

    public static LocalDateTime endOf(LocalDateTime start, int minutes) {
        throw new UnsupportedOperationException("TODO 5 : implementer endOf()");
    }

    public static Duration totalDuration(String... parts) {
        throw new UnsupportedOperationException("TODO 6 : implementer totalDuration()");
    }

    public static LocalTime startOfHour(LocalTime t) {
        throw new UnsupportedOperationException("TODO 7 : implementer startOfHour()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("workTime : PT8H30M et PT8H (service de nuit)",
                workTime(LocalTime.of(9, 15), LocalTime.of(17, 45)).toString().equals("PT8H30M")
                        && workTime(LocalTime.of(22, 0), LocalTime.of(6, 0)).toString().equals("PT8H"));
        ExerciseChecker.check("formatHm : 8h30, 8h00, 25h05",
                formatHm(Duration.ofMinutes(510)).equals("8h30") && formatHm(Duration.ofHours(8)).equals("8h00")
                        && formatHm(Duration.ofMinutes(1505)).equals("25h05"));
        ExerciseChecker.check("daysBetween : 60 en 2024, 59 en 2023",
                daysBetween(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 3, 1)) == 60
                        && daysBetween(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 3, 1)) == 59);
        ExerciseChecker.check("fullMonths : 0 puis 2",
                fullMonths(LocalDate.of(2024, 1, 31), LocalDate.of(2024, 2, 29)) == 0
                        && fullMonths(LocalDate.of(2024, 1, 31), LocalDate.of(2024, 3, 31)) == 2);
        ExerciseChecker.check("endOf : 2024-03-11T01:00",
                endOf(LocalDateTime.of(2024, 3, 10, 23, 30), 90).equals(LocalDateTime.of(2024, 3, 11, 1, 0)));
        ExerciseChecker.check("totalDuration : PT2H15M et PT0S",
                totalDuration("PT1H30M", "PT45M").toString().equals("PT2H15M") && totalDuration().toString().equals("PT0S"));
        ExerciseChecker.check("startOfHour(10:47:33) == 10:00", startOfHour(LocalTime.of(10, 47, 33)).equals(LocalTime.of(10, 0)));

        ExerciseChecker.summary();
    }
}
