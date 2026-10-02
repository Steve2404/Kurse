package ch4_coreapis.drills.r01_string;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 7 a s s",
            "D02 : 0 4 4 -1 4",
            "D03 : mals m [] animals",
            "D04 : JAVA ROCKS java rocks Java Rocks",
            "D05 : false true true true true true",
            "D06 : bonono bANANa abcd ---",
            "D07 : [pad] [pad  ] [  pad] [pad] true false true",
            "D08 : a/b/c 4 -33");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".length()", ".charAt(", "indexOf('", "indexOf(\"",
            "re:indexOf\\('.', \\d\\)##indexOf(char, depart)", "re:indexOf\\(\"\\w+\", \\d\\)##indexOf(texte, depart)", ".lastIndexOf(", "re:substring\\(\\d\\)##substring(debut)",
            "re:substring\\(\\d, \\d\\)##substring(debut, fin)", ".toUpperCase()", ".toLowerCase()", ".equalsIgnoreCase(",
            ".startsWith(", ".endsWith(", ".contains(", "replace('",
            "replace(\"", ".concat(", ".repeat(", ".stripLeading()",
            ".stripTrailing()", ".trim()", ".isEmpty()", ".isBlank()",
            "String.join(", ".compareTo(",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall01", args, EXPECTED, API);
    }
}
