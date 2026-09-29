package ch4_coreapis.exercises;

import ch4_coreapis.ExerciseChecker;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

/**
 * EXERCICE 25 - Les regles de java.time ecrites par toi : quelle classe accepte quelle unite, dates valides, Period (niveau : difficile)
 * ======================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise21_LocalDateTimeBasics.java.
 *
 * -- Ce qui ne compile pas (verifie avec javac 17, en anglais) --
 *
 *   LocalDate.now().getHour()             -> error: cannot find symbol
 *   LocalTime.now().getYear()             -> error: cannot find symbol
 *   LocalDate.now().plusHours(1)          -> error: cannot find symbol
 *   new LocalDate(2024, 1, 1)             -> error: LocalDate(int,int,int) has private access in LocalDate
 *   Period.of(1, 2)                       -> error: method of in class Period cannot be applied to given types;
 *   Duration.ofMonths(1)                  -> error: cannot find symbol
 *
 * -- Ce qui compile mais EXPLOSE a l'execution (verifie avec Java 17) --
 *
 *   LocalDate.of(2023, 2, 29)             -> DateTimeException: Invalid date 'February 29' as '2023' is not a leap year
 *   LocalDate.of(2023, 13, 1)             -> DateTimeException: Invalid value for MonthOfYear (valid values 1 - 12): 13
 *   LocalDate.plus(1, ChronoUnit.HOURS)   -> UnsupportedTemporalTypeException: Unsupported unit: Hours
 *   LocalTime.plus(Period.ofDays(1))      -> UnsupportedTemporalTypeException: Unsupported unit: Days
 *   Instant.plus(1, ChronoUnit.YEARS)     -> UnsupportedTemporalTypeException: Unsupported unit: Years
 *   Period.ofYears(1).ofWeeks(2)          -> P14D (!) : of... est STATIQUE, le ofYears(1) est jete
 *
 * Ici, c'est TOI qui ecris les regles ; main() les compare au VRAI
 * comportement de Java (essai reel + try/catch).
 *
 *
 * ==================================================================
 * TODO 1 : supports(type, unit)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Chaque classe ne connait que certaines unites :
 *   - LocalDate   : un calendrier sans horloge -> DAYS, WEEKS, MONTHS, YEARS
 *   - LocalTime   : une horloge sans calendrier -> SECONDS, MINUTES, HOURS, HALF_DAYS
 *   - LocalDateTime et ZonedDateTime : les deux -> toutes
 *   - Instant     : un point sur la ligne du temps, sans calendrier -> jusqu'a DAYS
 *                   (un jour = 24 h exactes), mais pas WEEKS, MONTHS, YEARS
 *
 * main() essaie VRAIMENT plus(1, unite) sur les 5 classes et 8 unites
 * (40 cas) et compare avec ta reponse.
 *
 * -- Le plan --
 *
 *   1. boolean dateUnit = unit est DAYS, WEEKS, MONTHS ou YEARS.
 *   2. switch sur type : "LocalDate" -> dateUnit ; "LocalTime" -> !dateUnit ;
 *      "LocalDateTime", "ZonedDateTime" -> true ; "Instant" -> !dateUnit || unit == DAYS.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 2 : isValidDate(year, month, day)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * SANS java.time : ecris la regle du calendrier. Un mois va de 1 a 12.
 * Fevrier a 29 jours les annees bissextiles, 28 sinon. Avril, juin,
 * septembre, novembre ont 30 jours ; les autres 31. Une annee est
 * bissextile si divisible par 4, SAUF les siecles (divisibles par 100),
 * SAUF encore ceux divisibles par 400 (2000 l'est, 2100 non).
 * main() compare avec LocalDate.of sur 5 annees x 14 mois x 33 jours.
 *
 * -- Essayons a la main --
 *
 *   (2024, 2, 29) -> true   (2023, 2, 29) -> false   (2100, 2, 29) -> false   (2000, 2, 29) -> true
 *   (2023, 4, 31) -> false  (2023, 13, 1) -> false   (2023, 1, 0)  -> false
 *
 * -- Le plan --
 *
 *   1. Mois hors 1..12 -> false.
 *   2. max = daysInMonth(year, month) ; rendre 1 <= day <= max.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : isLeap(year) et daysInMonth(year, month) (un switch expression).
 *
 *
 * ==================================================================
 * TODO 3 : yearAndTwoWeeks()
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * On veut "1 an et 2 semaines". Period.ofYears(1).ofWeeks(2) compile
 * mais rend P14D : ofWeeks est une methode STATIQUE, elle ignore
 * l'objet a sa gauche. Il faut les methodes d'INSTANCE : plusDays...
 *
 * -- Le plan --
 *
 *   1. Rendre Period.ofYears(1).plusDays(14) (toString : "P1Y14D").
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : startPlusHours(date, hours)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * date.plusHours n'existe pas (une LocalDate n'a pas d'horloge). On la
 * transforme d'abord en LocalDateTime a minuit, puis on ajoute.
 *
 * -- Essayons a la main --
 *
 *   (2024-03-10, 30) -> 2024-03-11T06:00
 *
 * -- Le plan --
 *
 *   1. Rendre date.atStartOfDay().plusHours(hours).
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
 *   - Comparer des ChronoUnit avec == (ce sont des constantes d'enum).
 *   - Un switch expression sur un String type est parfait pour le TODO 1.
 */
public class Exercise25_DateTimeRules {

    public static boolean supports(String type, ChronoUnit unit) {
        throw new UnsupportedOperationException("TODO 1 : implementer supports()");
    }

    public static boolean isValidDate(int year, int month, int day) {
        throw new UnsupportedOperationException("TODO 2 : implementer isValidDate()");
    }

    public static Period yearAndTwoWeeks() {
        throw new UnsupportedOperationException("TODO 3 : implementer yearAndTwoWeeks()");
    }

    public static LocalDateTime startPlusHours(LocalDate date, int hours) {
        throw new UnsupportedOperationException("TODO 4 : implementer startPlusHours()");
    }

    public static void main(String[] args) {
        Temporal[] samples = {LocalDate.of(2024, 1, 1), LocalTime.of(10, 0), LocalDateTime.of(2024, 1, 1, 10, 0),
                Instant.EPOCH, ZonedDateTime.of(2024, 1, 1, 10, 0, 0, 0, ZoneId.of("UTC"))};
        ChronoUnit[] units = {ChronoUnit.SECONDS, ChronoUnit.MINUTES, ChronoUnit.HOURS, ChronoUnit.HALF_DAYS,
                ChronoUnit.DAYS, ChronoUnit.WEEKS, ChronoUnit.MONTHS, ChronoUnit.YEARS};
        int agree = 0;
        for (Temporal sample : samples) {
            for (ChronoUnit unit : units) {
                if (supports(sample.getClass().getSimpleName(), unit) == reallySupports(sample, unit)) {
                    agree++;
                }
            }
        }
        ExerciseChecker.check("supports() == le vrai comportement sur 40 cas (" + agree + "/40)", agree == 40);

        int[] years = {1900, 2000, 2023, 2024, 2100};
        int wrong = 0;
        for (int y : years) {
            for (int m = 0; m <= 13; m++) {
                for (int d = 0; d <= 32; d++) {
                    if (isValidDate(y, m, d) != reallyValid(y, m, d)) {
                        wrong++;
                    }
                }
            }
        }
        ExerciseChecker.check("isValidDate() == LocalDate.of sur 2310 dates (erreurs : " + wrong + ")", wrong == 0);

        ExerciseChecker.check("yearAndTwoWeeks() == P1Y14D (et le piege donne P14D)",
                yearAndTwoWeeks().toString().equals("P1Y14D") && trapPeriod().toString().equals("P14D"));
        ExerciseChecker.check("startPlusHours(2024-03-10, 30) == 2024-03-11T06:00",
                startPlusHours(LocalDate.of(2024, 3, 10), 30).equals(LocalDateTime.of(2024, 3, 11, 6, 0)));

        ExerciseChecker.summary();
    }

    // Deja ecrit : le verdict REEL de Java (on essaie, et on regarde si ca explose).
    private static boolean reallySupports(Temporal sample, ChronoUnit unit) {
        try {
            sample.plus(1, unit);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    // Deja ecrit : le verdict REEL de LocalDate.of.
    private static boolean reallyValid(int y, int m, int d) {
        try {
            LocalDate.of(y, m, d);
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    // Deja ecrit : le piege, pour comparaison (ofWeeks est statique).
    @SuppressWarnings("static-access")
    private static Period trapPeriod() {
        return Period.ofYears(1).ofWeeks(2);
    }
}
