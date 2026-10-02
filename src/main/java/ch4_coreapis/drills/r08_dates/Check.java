package ch4_coreapis.drills.r08_dates;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 8 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall08, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 2026-01-20 06:15 2026-01-20T06:15 06:15:30 2026-01-20T06:15",
            "D02 : 2026-01-20 2026-12-29",
            "D03 : 2025-02-28 2026-02-28 2027-01-01",
            "D04 : TUESDAY JANUARY 1 20 false true",
            "D05 : 07:05 23:15 01:15 2026-01-21",
            "D06 : P1Y2M3D P14D P3M P1Y6M P0D",
            "D07 : 2027-03-23 P25Y8M5D P-26D",
            "D08 : true false true 2026-01-01 2026-02-20");
            // EXPECTED-END

    static final List<String> API = List.of(
            "LocalDate.of(", "LocalTime.of(", "LocalDateTime.of(", ".plusDays(",
            ".plusWeeks(", ".minusMonths(", ".plusYears(", ".getDayOfWeek()",
            ".getMonthValue()", ".isLeapYear()", ".plusMinutes(", ".minusHours(",
            ".withHour(", ".toLocalDate()", "Period.of(", "Period.ofWeeks(",
            "Period.ofDays(1).ofMonths(", ".normalized()", "Period.ZERO", "Period.between(",
            ".isBefore(", ".isAfter(", ".withDayOfMonth(", ".withMonth(",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall08", args, EXPECTED, API);
    }
}
