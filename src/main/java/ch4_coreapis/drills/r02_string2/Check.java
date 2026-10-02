package ch4_coreapis.drills.r02_string2;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall02, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 12 3 195 ab xnull",
            "D02 : start3ctrue",
            "D03 : abc CDEF",
            "D04 : [  a|    b|] [  x|y|]",
            "D05 : [x|  y] [a	b\\c]",
            "D06 : n=42 [ab  ][  ab][ab] 00042",
            "D07 : Ab Abc true",
            "D08 : valeur null 4");
            // EXPECTED-END

    static final List<String> API = List.of(
            "+=", ".indent(2)", ".indent(-2)", ".stripIndent()",
            ".translateEscapes()", ".formatted(", "String.format(", "%05d",
            "%.2s", "null",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall02", args, EXPECTED, API);
    }
}
