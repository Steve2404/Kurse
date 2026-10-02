package ch4_coreapis.drills.r11_moredates;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 11 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall11, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 2026-02-28 23:59:30 2026-02-28T23:59 P1Y2M 90",
            "D02 : 2026-02-28T09:30 2026-02-28T00:00 2026-02-28T23:59:30 2026-02-28T00:00+01:00[Europe/Paris]",
            "D03 : 20512 1970-01-01 1970-01-01T00:00:01.500Z 3600",
            "D04 : 00:00 12:00 23:59:59.999999999 true Z",
            "D05 : 2026-03-14 2036-02-28 00:00:15 23:59:30.001 2025-12-30",
            "D06 : P9M27D 300 1 25 90000 1 0",
            "D07 : +05:30 true Asia/Kolkata 2026-07-01T12:00 06:30",
            "D08 : JANUARY SUNDAY 2026-02-23 29 365 6 DECEMBER");
            // EXPECTED-END

    static final List<String> API = List.of(
            "LocalDate.parse(", "LocalTime.parse(", "LocalDateTime.parse(", "Period.parse(",
            "Duration.parse(", ".atTime(", ".atStartOfDay()", ".atDate(",
            ".atStartOfDay(ZoneId", ".toEpochDay()", "LocalDate.ofEpochDay(", "Instant.ofEpochMilli(",
            ".toEpochSecond()", "LocalTime.MIDNIGHT", "LocalTime.NOON", "LocalTime.MAX",
            "ZoneOffset.UTC", "ChronoUnit.WEEKS", "ChronoUnit.DECADES", ".plusSeconds(",
            ".plusNanos(", ".minusDays(", ".until(", ".toDays()",
            ".toSeconds()", ".toHoursPart()", ".toMinutesPart()", ".atZone(",
            "ZoneOffset.of(", ".getZone()", ".toLocalDateTime()", ".with(DayOfWeek.",
            "Month.FEBRUARY.length(", ".lengthOfYear()", "Month.of(",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall11", args, EXPECTED, API);
    }
}
