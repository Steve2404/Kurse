package ch4_coreapis.projects.p06_hotel;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 6 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Hotel, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "=== FACTURES ===",
            "B1  Lea Martin    SIMPLE 2026-12-18 -> 2026-12-21  3 nuit(s)    272.00  ref LEA-352-101",
            "B2  Hugo Durand   DOUBLE 2026-12-20 -> 2026-12-24  4 nuit(s)    482.00  ref HUG-354-102",
            "B3  Ines Petit    SIMPLE 2026-12-21 -> 2026-12-23  2 nuit(s)    160.00  ref INE-355-101",
            "B4  Tom Robert    DOUBLE 2026-12-23 -> 2026-12-26  3 nuit(s)    385.60  ref TOM-357-102",
            "B5  Zoe Bernard   SUITE  2026-12-24 -> 2026-12-27  3 nuit(s)    833.00  ref ZOE-358-201",
            "B6  Adam Leroy    SIMPLE 2026-12-22 -> 2026-12-24  2 nuit(s)    160.00  ref ADA-356-101",
            "CHIFFRE D'AFFAIRES : 2292.60",
            "=== CONFLITS ===",
            "B2 et B4 : chambre 102, 1 nuit(s) en commun a partir du 2026-12-23",
            "B3 et B6 : chambre 101, 1 nuit(s) en commun a partir du 2026-12-22",
            "2 conflit(s)",
            "=== PLANNING (nombre de reservations par nuit) ===",
            "      18 19 20 21 22 23 24 25 26 27",
            "101    #  #  #  #  !  #  .  .  .  .",
            "102    .  .  #  #  #  !  #  #  .  .",
            "201    .  .  .  .  .  .  #  #  #  .",
            "CLIENTS : Adam Leroy, Hugo Durand, Ines Petit, Lea Martin, Tom Robert, Zoe Bernard");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".split(", "LocalDate.parse(", "ChronoUnit.DAYS.between(", "new StringBuilder(",
            "String.format(", "Arrays.sort(", "Arrays.copyOf(", "String.join(",
            "Math.round(", "re:new int\\[[^\\]]+\\]\\[[^\\]]+\\]##planning en tableau 2D", ".isBefore(", ".isAfter(",
            ".getDayOfYear()", ".substring(", ".strip()",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Hotel", args, EXPECTED, API);
    }
}
