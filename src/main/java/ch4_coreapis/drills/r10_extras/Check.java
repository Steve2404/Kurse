package ch4_coreapis.drills.r10_extras;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 10 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall10, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 0 -1 -7 25 -1",
            "D02 : a#b#c# a_b22c333 true 3 0",
            "D03 : Java 16 19 5 4 3 -1",
            "D04 : 3.0 true x ok 42 -17 8",
            "D05 : 0 2 true Misisippi 3 true",
            "D06 : -1.0 3.0 5.0 3.1416 true 3.0",
            "D07 : -2147483648 2147483648 0.30000000000000004 -0.0 0.0 3 -3",
            "D08 : b 25 48 true false Q 7");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".compareToIgnoreCase(", ".replaceAll(", ".replaceFirst(", ".matches(",
            ".setCharAt(", ".capacity()", ".lastIndexOf(", "String.valueOf(",
            "Integer.toString(", "Integer.parseInt(", "Integer.valueOf(", "Math.signum(",
            "Math.cbrt(", "Math.hypot(", "Math.PI", "Math.E",
            "Math.log10(", "Integer.MAX_VALUE", "Character.isDigit(", "Character.toUpperCase(",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall10", args, EXPECTED, API);
    }
}
