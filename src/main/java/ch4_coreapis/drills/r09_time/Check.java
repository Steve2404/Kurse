package ch4_coreapis.drills.r09_time;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 9 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall09, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : PT24H PT36H PT1H15M PT1M1S PT1.5S",
            "D02 : PT8H45M 525 8 PT-8H",
            "D03 : 2 150 2026-05-04T10:30 2026-05-04T00:00",
            "D04 : 2026-06-01T09:00+02:00[Europe/Paris] 2026-06-01T16:00+09:00[Asia/Tokyo] 2026-06-01T09:00+09:00[Asia/Tokyo]",
            "D05 : 2026-06-01T07:00:00Z 1970-01-02T00:00:00Z 2026-06-01T08:00:00Z 20605",
            "D06 : 2026-03-29T03:30+02:00[Europe/Paris] 2026-03-29T04:30+02:00[Europe/Paris]",
            "D07 : 2026-10-25T12:00+01:00[Europe/Paris] 2026-10-25T11:00+01:00[Europe/Paris] PT25H",
            "D08 : 03:15 +02:00 +01:00 +02:00");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Duration.ofDays(", "Duration.ofHours(", "Duration.ofMinutes(", "Duration.ofSeconds(",
            "Duration.ofMillis(", "Duration.between(", ".toMinutes()", ".toHours()",
            "ChronoUnit.HOURS.between(", ".truncatedTo(", ".withZoneSameInstant(", ".withZoneSameLocal(",
            ".toInstant()", "Instant.ofEpochSecond(", "Instant.EPOCH", ".getOffset()",
            ".withLaterOffsetAtOverlap()", ".withEarlierOffsetAtOverlap()",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall09", args, EXPECTED, API);
    }
}
