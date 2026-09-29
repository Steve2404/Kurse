package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * EXERCICE 27 - Instant : un point sur la ligne du temps, sans fuseau ni calendrier (niveau : difficile)
 * =====================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise21_LocalDateTimeBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un Instant, c'est un nombre de secondes depuis le 1er janvier 1970 a
 * minuit, a Greenwich (l'"epoque"). Il n'a ni fuseau, ni jour de la
 * semaine, ni mois : c'est le MEME moment pour toute la planete. Pour
 * lire une heure "humaine", il faut lui donner un fuseau (atZone).
 *
 * -- Rappels verifies avec Java 17 --
 *
 *   Instant.parse("2024-06-01T07:00:00Z")  -> 2024-06-01T07:00:00Z ; getEpochSecond() 1717225200
 *   Instant.parse("2024-06-01T07:00Z")     -> DateTimeParseException (les SECONDES sont obligatoires)
 *   Instant.parse("2024-06-01T07:00:00")   -> DateTimeParseException (le Z est obligatoire)
 *   Instant.ofEpochSecond(0)               -> 1970-01-01T00:00:00Z ; Instant.ofEpochMilli(1500) -> 1970-01-01T00:00:01.500Z
 *   instant.plus(Duration.ofHours(30)), plus(2, ChronoUnit.DAYS) : OK
 *   instant.plus(1, ChronoUnit.YEARS)      -> UnsupportedTemporalTypeException: Unsupported unit: Years
 *   instant.get(ChronoField.HOUR_OF_DAY)   -> UnsupportedTemporalTypeException: Unsupported field: HourOfDay
 *   9h00 a Paris le 2024-06-01 (UTC+2)     -> toInstant() 2024-06-01T07:00:00Z
 *   9h00 a Paris le 2024-01-01 (UTC+1)     -> toInstant() 2024-01-01T08:00:00Z
 *   Paris 9h00 et New York 3h00 le 2024-06-01 : equals() false, mais toInstant() egaux (et isEqual() true)
 *   Le 2024-03-31 a Paris ne dure que 23 heures (passage a l'heure d'ete)
 *
 *
 * ==================================================================
 * TODO 1 : parseUtc(text)    et    TODO 2 : epochMillis(instant)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. parseUtc : Instant.parse(text).
 *   2. epochMillis : instant.toEpochMilli() (millisecondes depuis 1970).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : fromLocal(dateTime, zone)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * "9h00" ne veut rien dire tant qu'on ne sait pas OU. On colle le
 * fuseau (atZone), puis on lit l'instant (toInstant).
 *
 * -- Essayons a la main --
 *
 *   (2024-06-01T09:00, "Europe/Paris") -> 2024-06-01T07:00:00Z (ete : UTC+2)
 *   (2024-01-01T09:00, "Europe/Paris") -> 2024-01-01T08:00:00Z (hiver : UTC+1)
 *
 * -- Le plan --
 *
 *   1. Rendre dateTime.atZone(ZoneId.of(zone)).toInstant().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : sameMoment(a, b)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Deux montres dans deux pays : 9h00 a Paris et 3h00 a New York
 * montrent le MEME moment. equals() dit non (fuseaux differents) ; on
 * compare donc les instants.
 *
 * -- Le plan --
 *
 *   1. Rendre a.toInstant().equals(b.toInstant()) (ou a.isEqual(b)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 5 : minutesBetween(a, b)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   2024-06-01T07:00:00Z -> 2024-06-02T09:30:00Z : 26 h 30 = 1590 minutes
 *
 * -- Le plan --
 *
 *   1. Rendre Duration.between(a, b).toMinutes() (ou a.until(b, ChronoUnit.MINUTES)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : localTimeAt(instant, zone)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   (2024-06-01T07:00:00Z, "Asia/Tokyo") -> 16:00 (UTC+9)
 *
 * -- Le plan --
 *
 *   1. Rendre instant.atZone(ZoneId.of(zone)).toLocalTime().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : realDayLength(date, zone)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un jour dure-t-il toujours 24 heures ? Pas les jours de changement
 * d'heure ! On prend l'instant du debut du jour et celui du debut du
 * lendemain, dans le fuseau, et on mesure l'ecart REEL.
 *
 * -- Essayons a la main --
 *
 *   (2024-03-31, Paris) -> PT23H     (2024-10-27, Paris) -> PT25H     (2024-06-01, Paris) -> PT24H
 *
 * -- Le plan --
 *
 *   1. id = ZoneId.of(zone).
 *   2. start = date.atStartOfDay(id).toInstant() ; end = date.plusDays(1).atStartOfDay(id).toInstant().
 *   3. Rendre Duration.between(start, end).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 8 : earliest(isoTexts...)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Des evenements arrivent de serveurs du monde entier, tous en UTC
 * (le Z). On garde le plus ancien : un Instant se compare avec isBefore.
 *
 * -- Essayons a la main --
 *
 *   ("2024-06-01T07:00:00Z", "2024-05-31T23:59:59Z", "2024-06-01T00:00:00Z") -> 2024-05-31T23:59:59Z
 *
 * -- Le plan --
 *
 *   1. best = parse du premier ; pour les autres : si parse(t).isBefore(best), best = ce parse.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : parseUtc (TODO 1).
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - LocalDate.atStartOfDay(ZoneId) rend un ZonedDateTime (minuit dans ce fuseau).
 *   - Un Instant n'a pas getHour() : passer par atZone(...).
 */
public class Exercise27_InstantAndTimeline {

    public static Instant parseUtc(String text) {
        throw new UnsupportedOperationException("TODO 1 : implementer parseUtc()");
    }

    public static long epochMillis(Instant instant) {
        throw new UnsupportedOperationException("TODO 2 : implementer epochMillis()");
    }

    public static Instant fromLocal(LocalDateTime dateTime, String zone) {
        throw new UnsupportedOperationException("TODO 3 : implementer fromLocal()");
    }

    public static boolean sameMoment(ZonedDateTime a, ZonedDateTime b) {
        throw new UnsupportedOperationException("TODO 4 : implementer sameMoment()");
    }

    public static long minutesBetween(Instant a, Instant b) {
        throw new UnsupportedOperationException("TODO 5 : implementer minutesBetween()");
    }

    public static LocalTime localTimeAt(Instant instant, String zone) {
        throw new UnsupportedOperationException("TODO 6 : implementer localTimeAt()");
    }

    public static Duration realDayLength(LocalDate date, String zone) {
        throw new UnsupportedOperationException("TODO 7 : implementer realDayLength()");
    }

    public static Instant earliest(String... isoTexts) {
        throw new UnsupportedOperationException("TODO 8 : implementer earliest()");
    }

    public static void main(String[] args) {
        Instant june = parseUtc("2024-06-01T07:00:00Z");
        ExerciseChecker.check("parseUtc : getEpochSecond() == 1717225200", june.getEpochSecond() == 1717225200L);
        ExerciseChecker.check("epochMillis : 1717225200000 et 1500", epochMillis(june) == 1717225200000L
                && epochMillis(Instant.ofEpochMilli(1500)) == 1500);
        ExerciseChecker.check("fromLocal : ete UTC+2, hiver UTC+1",
                fromLocal(LocalDateTime.of(2024, 6, 1, 9, 0), "Europe/Paris").toString().equals("2024-06-01T07:00:00Z")
                        && fromLocal(LocalDateTime.of(2024, 1, 1, 9, 0), "Europe/Paris").toString().equals("2024-01-01T08:00:00Z"));
        ZonedDateTime paris = ZonedDateTime.of(2024, 6, 1, 9, 0, 0, 0, ZoneId.of("Europe/Paris"));
        ZonedDateTime newYork = ZonedDateTime.of(2024, 6, 1, 3, 0, 0, 0, ZoneId.of("America/New_York"));
        ExerciseChecker.check("sameMoment : Paris 9h00 et New York 3h00 (alors que equals() dit false)",
                sameMoment(paris, newYork) && !paris.equals(newYork) && !sameMoment(paris, newYork.plusMinutes(1)));
        ExerciseChecker.check("minutesBetween == 1590", minutesBetween(june, parseUtc("2024-06-02T09:30:00Z")) == 1590);
        ExerciseChecker.check("localTimeAt : Tokyo 16:00", localTimeAt(june, "Asia/Tokyo").equals(LocalTime.of(16, 0)));
        ExerciseChecker.check("realDayLength : 23 h, 25 h, 24 h a Paris",
                realDayLength(LocalDate.of(2024, 3, 31), "Europe/Paris").toString().equals("PT23H")
                        && realDayLength(LocalDate.of(2024, 10, 27), "Europe/Paris").toString().equals("PT25H")
                        && realDayLength(LocalDate.of(2024, 6, 1), "Europe/Paris").toString().equals("PT24H"));
        ExerciseChecker.check("earliest : 2024-05-31T23:59:59Z",
                earliest("2024-06-01T07:00:00Z", "2024-05-31T23:59:59Z", "2024-06-01T00:00:00Z").toString().equals("2024-05-31T23:59:59Z"));

        ExerciseChecker.summary();
    }
}
