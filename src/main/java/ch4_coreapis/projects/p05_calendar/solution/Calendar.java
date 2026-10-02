package ch4_coreapis.projects.p05_calendar.solution;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * SOLUTION du projet 5 - une conception possible.
 * Toutes les dates sont FIXES (jamais now()) pour que la sortie soit reproductible.
 */
public class Calendar {

    static final ZoneId PARIS = ZoneId.of("Europe/Paris");
    static final ZoneId NEW_YORK = ZoneId.of("America/New_York");

    static void month(int year, Month month) {
        LocalDate first = LocalDate.of(year, month, 1);
        System.out.println(month + " " + year + " (" + first.lengthOfMonth() + " jours)");
        System.out.println(" Lu Ma Me Je Ve Sa Di");
        // getValue() : lundi = 1 ... dimanche = 7 ; on decale la 1re ligne d'autant de cases vides.
        String line = "   ".repeat(first.getDayOfWeek().getValue() - 1);
        for (LocalDate d = first; d.getMonth() == month; d = d.plusDays(1)) {
            line += (d.getDayOfMonth() < 10 ? "  " : " ") + d.getDayOfMonth();
            if (d.getDayOfWeek() == DayOfWeek.SUNDAY) {
                System.out.println(line);
                line = "";
            }
        }
        if (!line.isEmpty()) {
            System.out.println(line);
        }
    }

    static LocalDate nextFriday13(LocalDate from) {
        LocalDate d = from.withDayOfMonth(13);
        if (!d.isAfter(from)) {
            d = d.plusMonths(1);
        }
        while (d.getDayOfWeek() != DayOfWeek.FRIDAY) {
            d = d.plusMonths(1);
        }
        return d;
    }

    static int workingDays(LocalDate from, LocalDate toExclusive) {
        int count = 0;
        for (LocalDate d = from; d.isBefore(toExclusive); d = d.plusDays(1)) {
            DayOfWeek day = d.getDayOfWeek();
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        System.out.println("=== DATES ===");
        // Fin de mois : plusMonths ajuste au dernier jour valide ; les objets sont IMMUABLES.
        LocalDate jan31 = LocalDate.of(2024, Month.JANUARY, 31);
        jan31.plusDays(1);
        System.out.println(jan31 + " +1 mois = " + jan31.plusMonths(1) + " (bissextile " + jan31.isLeapYear() + "), 2023 : "
                + LocalDate.of(2023, 1, 31).plusMonths(1) + ", ignore : " + jan31);
        LocalDate exam = LocalDate.of(2026, 10, 2);
        System.out.println(exam + " est un " + exam.getDayOfWeek() + ", jour " + exam.getDayOfYear() + " de l'annee, +3 semaines "
                + exam.plusWeeks(3) + ", fin du mois " + exam.withDayOfMonth(exam.lengthOfMonth()));

        System.out.println("=== PERIOD ===");
        LocalDate birth = LocalDate.of(1995, 7, 14);
        Period age = Period.between(birth, exam);
        System.out.println("age : " + age + " = " + age.getYears() + " ans " + age.getMonths() + " mois " + age.getDays() + " jours, en jours "
                + ChronoUnit.DAYS.between(birth, exam));
        // Piege : ofWeeks est STATIQUE ; l'appel chaine ignore le ofYears precedent.
        System.out.println("Period.of(1, 2, 3) " + Period.of(1, 2, 3) + ", ofYears(1).ofWeeks(2) " + Period.ofYears(1).ofWeeks(2)
                + ", ofMonths(14) " + Period.ofMonths(14) + ", normalise " + Period.ofMonths(14).normalized() + ", exam + P1M " + exam.plus(Period.ofMonths(1)));

        System.out.println("=== DURATION ET HEURES ===");
        LocalTime late = LocalTime.of(23, 30);
        Duration meeting = Duration.ofMinutes(90);
        System.out.println(late + " + 2 h = " + late.plusHours(2) + " (passe minuit), reunion " + meeting + ", " + meeting.toMinutes() + " min, "
                + Duration.ofSeconds(3725) + ", entre 08:15 et 17:40 : " + Duration.between(LocalTime.of(8, 15), LocalTime.of(17, 40)));
        LocalDateTime start = LocalDateTime.of(2026, 10, 2, 14, 47, 33);
        System.out.println("debut " + start + ", tronque a l'heure " + start.truncatedTo(ChronoUnit.HOURS) + ", minutes jusqu'a 18:00 "
                + ChronoUnit.MINUTES.between(start, start.withHour(18).withMinute(0).withSecond(0)));

        System.out.println("=== FUSEAUX ET CHANGEMENT D'HEURE ===");
        ZonedDateTime departure = ZonedDateTime.of(LocalDateTime.of(2026, 3, 29, 10, 0), PARIS);
        ZonedDateTime arrival = departure.plus(Duration.ofMinutes(510)).withZoneSameInstant(NEW_YORK);
        System.out.println("depart " + departure + ", arrivee " + arrival + ", meme instant " + departure.plusMinutes(510).toInstant().equals(arrival.toInstant()));
        ZonedDateTime gap = ZonedDateTime.of(LocalDateTime.of(2026, 3, 29, 2, 30), PARIS);
        ZonedDateTime overlap = ZonedDateTime.of(LocalDateTime.of(2026, 10, 25, 2, 30), PARIS);
        System.out.println("02:30 le 29/03 (heure qui n'existe pas) -> " + gap + " ; 02:30 le 25/10 (heure double) -> " + overlap
                + " puis " + overlap.withLaterOffsetAtOverlap());
        ZonedDateTime night = ZonedDateTime.of(LocalDateTime.of(2026, 3, 29, 1, 0), PARIS);
        System.out.println("de 01:00 a 04:00 le 29/03 : " + Duration.between(night, night.withHour(4)) + " reelles, "
                + ChronoUnit.HOURS.between(night.toLocalDateTime(), night.withHour(4).toLocalDateTime()) + " h d'horloge");
        Instant epoch = Instant.ofEpochSecond(0);
        Instant launch = Instant.parse("2026-10-02T08:00:00Z");
        System.out.println("epoch " + epoch + ", lancement " + launch + ", a Paris " + launch.atZone(PARIS).toLocalTime()
                + ", jours depuis l'epoch " + ChronoUnit.DAYS.between(epoch, launch));

        System.out.println("=== ALGORITHMES ===");
        System.out.println("prochain vendredi 13 apres " + exam + " : " + nextFriday13(exam) + ", apres le 13/02/2026 : " + nextFriday13(LocalDate.of(2026, 2, 13)));
        System.out.println("jours ouvres en octobre 2026 : " + workingDays(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 11, 1)));
        month(2026, Month.FEBRUARY);
    }
}
