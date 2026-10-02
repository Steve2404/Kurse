package ch4_coreapis.drills.r07_math;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 7 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall07, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 7 -2 7.5 4",
            "D02 : 4 4 -3 -4 2",
            "D03 : 4.0 3.0 -3.0 -4.0",
            "D04 : 256.0 3.0 4.0 4.5 4",
            "D05 : true true",
            "D06 : 1000 12.35 1230",
            "D07 : -2147483648 NaN NaN",
            "D08 : -4 -3 2 -1");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Math.max(", "Math.min(", "5xMath.round(", "Math.ceil(",
            "Math.floor(", "Math.pow(", "Math.sqrt(", "Math.abs(",
            "Math.random()", "Math.floorDiv(", "Math.floorMod(", "long fromDouble",
            "int fromFloat",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall07", args, EXPECTED, API);
    }
}
