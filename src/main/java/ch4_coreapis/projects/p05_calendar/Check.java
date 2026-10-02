package ch4_coreapis.projects.p05_calendar;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Calendar, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "=== DATES ===",
            "2024-01-31 +1 mois = 2024-02-29 (bissextile true), 2023 : 2023-02-28, ignore : 2024-01-31",
            "2026-10-02 est un FRIDAY, jour 275 de l'annee, +3 semaines 2026-10-23, fin du mois 2026-10-31",
            "=== PERIOD ===",
            "age : P31Y2M18D = 31 ans 2 mois 18 jours, en jours 11403",
            "Period.of(1, 2, 3) P1Y2M3D, ofYears(1).ofWeeks(2) P14D, ofMonths(14) P14M, normalise P1Y2M, exam + P1M 2026-11-02",
            "=== DURATION ET HEURES ===",
            "23:30 + 2 h = 01:30 (passe minuit), reunion PT1H30M, 90 min, PT1H2M5S, entre 08:15 et 17:40 : PT9H25M",
            "debut 2026-10-02T14:47:33, tronque a l'heure 2026-10-02T14:00, minutes jusqu'a 18:00 192",
            "=== FUSEAUX ET CHANGEMENT D'HEURE ===",
            "depart 2026-03-29T10:00+02:00[Europe/Paris], arrivee 2026-03-29T12:30-04:00[America/New_York], meme instant true",
            "02:30 le 29/03 (heure qui n'existe pas) -> 2026-03-29T03:30+02:00[Europe/Paris] ; 02:30 le 25/10 (heure double) -> 2026-10-25T02:30+02:00[Europe/Paris] puis 2026-10-25T02:30+01:00[Europe/Paris]",
            "de 01:00 a 04:00 le 29/03 : PT2H reelles, 3 h d'horloge",
            "epoch 1970-01-01T00:00:00Z, lancement 2026-10-02T08:00:00Z, a Paris 10:00, jours depuis l'epoch 20728",
            "=== ALGORITHMES ===",
            "prochain vendredi 13 apres 2026-10-02 : 2026-11-13, apres le 13/02/2026 : 2026-03-13",
            "jours ouvres en octobre 2026 : 22",
            "FEBRUARY 2026 (28 jours)",
            " Lu Ma Me Je Ve Sa Di",
            "                    1",
            "  2  3  4  5  6  7  8",
            "  9 10 11 12 13 14 15",
            " 16 17 18 19 20 21 22",
            " 23 24 25 26 27 28");
            // EXPECTED-END

    static final List<String> API = List.of(
            "LocalDate.of(", "LocalTime.of(", "LocalDateTime.of(", "ZonedDateTime.of(",
            "ZoneId.of(", "Instant.ofEpochSecond(", "Instant.parse(", "Period.between(",
            "Period.of(", "Period.ofYears(1).ofWeeks(", ".normalized()", "Duration.ofMinutes(",
            "Duration.between(", "ChronoUnit.DAYS.between(", ".truncatedTo(", ".plusMonths(",
            ".withDayOfMonth(", ".isLeapYear()", ".getDayOfWeek()", ".withZoneSameInstant(",
            ".toInstant()", ".withLaterOffsetAtOverlap()", ".lengthOfMonth()", "DayOfWeek.",
            "Month.",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Calendar", args, EXPECTED, API);
    }
}
