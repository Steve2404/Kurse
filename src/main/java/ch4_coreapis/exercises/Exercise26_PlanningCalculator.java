package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * EXERCICE 26 - Calculateur de planning : echeances, jours ouvres, age, dernier vendredi, fuseaux (niveau : avance)
 * =================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise21_LocalDateTimeBasics.java.
 *
 * -- Rappels verifies avec Java 17 --
 *
 *   LocalDate.of(2024, 1, 31).plusMonths(1)                -> 2024-02-29 (se cale sur le dernier jour valide)
 *   LocalDate.of(2024, 1, 31).plusMonths(1).plusMonths(1)  -> 2024-03-29 (le 31 est PERDU en route)
 *   LocalDate.of(2024, 1, 31).plusMonths(2)                -> 2024-03-31
 *   LocalDate.of(2024, 2, 29).plusYears(1)                 -> 2025-02-28
 *   LocalDate.of(2024, 1, 31).getDayOfWeek()               -> WEDNESDAY
 *   date.with(TemporalAdjusters.lastDayOfMonth()), date.withDayOfMonth(1), date.lengthOfMonth()
 *   9h00 a Paris le 2024-06-01 -> withZoneSameInstant(Asia/Tokyo) -> 16:00 ; (America/New_York) -> 03:00
 *   withZoneSameLocal(Asia/Tokyo) garde 09:00 : ce n'est PLUS le meme instant
 *
 *
 * ==================================================================
 * TODO 1 : installments(start, count)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un pret se rembourse chaque mois, le meme jour que le premier
 * paiement. Si on enchaine plusMonths(1) depuis l'echeance PRECEDENTE,
 * un 31 devient 29 en fevrier et reste 29 pour toujours. Il faut
 * toujours repartir de la date de DEPART : start.plusMonths(i).
 *
 * -- Essayons a la main --
 *
 *   (2024-01-31, 4) -> [2024-01-31, 2024-02-29, 2024-03-31, 2024-04-30]
 *
 * -- Le plan --
 *
 *   1. dates = new LocalDate[count] ; dates[i] = start.plusMonths(i).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : isWeekend(date)    et    TODO 3 : nextBusinessDay(date)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   isWeekend(2024-03-09) -> true (samedi)
 *   nextBusinessDay(2024-03-08, vendredi) -> 2024-03-11 (lundi)
 *   nextBusinessDay(2024-03-09, samedi)   -> 2024-03-11
 *   nextBusinessDay(2024-03-06, mercredi) -> 2024-03-07
 *
 * -- Le plan --
 *
 *   1. isWeekend : getDayOfWeek() vaut SATURDAY ou SUNDAY.
 *   2. nextBusinessDay : next = date.plusDays(1) ; tant que isWeekend(next), next = next.plusDays(1).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isWeekend sert aux TODO 3 et 4.
 *
 *
 * ==================================================================
 * TODO 4 : businessDaysBetween(from, to)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   (lundi 2024-03-04, lundi 2024-03-18) -> 2 semaines -> 10 (to EXCLU)
 *   (d, d) -> 0
 *
 * -- Le plan --
 *
 *   1. Pour d de from (inclus) a to (exclu), jour par jour (isBefore) : compter si !isWeekend(d).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isWeekend.
 *
 *
 * ==================================================================
 * TODO 5 : age(birth, today)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   ne le 2000-02-29 : le 2024-02-28 -> 23 ans ; le 2024-02-29 -> 24 ans
 *
 * -- Le plan --
 *
 *   1. Rendre Period.between(birth, today).getYears().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : lastFridayOfMonth(anyDay)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   2024-03-10 -> dernier jour 2024-03-31 (dimanche) -> on recule -> 2024-03-29
 *   2024-05-01 -> 2024-05-31 est deja un vendredi -> 2024-05-31
 *
 * -- Le plan --
 *
 *   1. d = anyDay.withDayOfMonth(anyDay.lengthOfMonth()).
 *   2. Tant que d n'est pas FRIDAY : d = d.minusDays(1).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : localTimeIn(meeting, zone)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une reunion a 9h a Paris : quelle heure est-il pour un collegue a
 * Tokyo ? MEME instant, autre horloge : withZoneSameInstant.
 *
 * -- Le plan --
 *
 *   1. Rendre meeting.withZoneSameInstant(ZoneId.of(zone)).toLocalTime().
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
 *   - import java.time.DayOfWeek; import java.time.Period;
 *   - Boucle sur des dates : for (LocalDate d = from; d.isBefore(to); d = d.plusDays(1))
 */
public class Exercise26_PlanningCalculator {

    public static LocalDate[] installments(LocalDate start, int count) {
        throw new UnsupportedOperationException("TODO 1 : implementer installments()");
    }

    public static boolean isWeekend(LocalDate date) {
        throw new UnsupportedOperationException("TODO 2 : implementer isWeekend()");
    }

    public static LocalDate nextBusinessDay(LocalDate date) {
        throw new UnsupportedOperationException("TODO 3 : implementer nextBusinessDay()");
    }

    public static int businessDaysBetween(LocalDate from, LocalDate to) {
        throw new UnsupportedOperationException("TODO 4 : implementer businessDaysBetween()");
    }

    public static int age(LocalDate birth, LocalDate today) {
        throw new UnsupportedOperationException("TODO 5 : implementer age()");
    }

    public static LocalDate lastFridayOfMonth(LocalDate anyDay) {
        throw new UnsupportedOperationException("TODO 6 : implementer lastFridayOfMonth()");
    }

    public static LocalTime localTimeIn(ZonedDateTime meeting, String zone) {
        throw new UnsupportedOperationException("TODO 7 : implementer localTimeIn()");
    }

    public static void main(String[] args) {
        LocalDate[] dates = installments(LocalDate.of(2024, 1, 31), 4);
        ExerciseChecker.check("installments : 01-31, 02-29, 03-31, 04-30",
                dates.length == 4 && dates[1].equals(LocalDate.of(2024, 2, 29)) && dates[2].equals(LocalDate.of(2024, 3, 31))
                        && dates[3].equals(LocalDate.of(2024, 4, 30)));
        ExerciseChecker.check("isWeekend : samedi oui, vendredi non",
                isWeekend(LocalDate.of(2024, 3, 9)) && !isWeekend(LocalDate.of(2024, 3, 8)));
        ExerciseChecker.check("nextBusinessDay : vendredi et samedi -> lundi, mercredi -> jeudi",
                nextBusinessDay(LocalDate.of(2024, 3, 8)).equals(LocalDate.of(2024, 3, 11))
                        && nextBusinessDay(LocalDate.of(2024, 3, 9)).equals(LocalDate.of(2024, 3, 11))
                        && nextBusinessDay(LocalDate.of(2024, 3, 6)).equals(LocalDate.of(2024, 3, 7)));
        ExerciseChecker.check("businessDaysBetween : 10 et 0",
                businessDaysBetween(LocalDate.of(2024, 3, 4), LocalDate.of(2024, 3, 18)) == 10
                        && businessDaysBetween(LocalDate.of(2024, 3, 4), LocalDate.of(2024, 3, 4)) == 0);
        ExerciseChecker.check("age : 23 puis 24 (ne un 29 fevrier)",
                age(LocalDate.of(2000, 2, 29), LocalDate.of(2024, 2, 28)) == 23
                        && age(LocalDate.of(2000, 2, 29), LocalDate.of(2024, 2, 29)) == 24);
        ExerciseChecker.check("lastFridayOfMonth : 2024-03-29 et 2024-05-31",
                lastFridayOfMonth(LocalDate.of(2024, 3, 10)).equals(LocalDate.of(2024, 3, 29))
                        && lastFridayOfMonth(LocalDate.of(2024, 5, 1)).equals(LocalDate.of(2024, 5, 31)));
        ZonedDateTime meeting = ZonedDateTime.of(2024, 6, 1, 9, 0, 0, 0, ZoneId.of("Europe/Paris"));
        ExerciseChecker.check("localTimeIn : Tokyo 16:00, New York 03:00",
                localTimeIn(meeting, "Asia/Tokyo").equals(LocalTime.of(16, 0))
                        && localTimeIn(meeting, "America/New_York").equals(LocalTime.of(3, 0)));

        ExerciseChecker.summary();
    }
}
