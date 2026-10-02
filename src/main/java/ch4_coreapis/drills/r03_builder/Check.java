package ch4_coreapis.drills.r03_builder;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall03, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : ab1ctrue2.5 11",
            "D02 : -ani+mals-",
            "D03 : def",
            "D04 : abcd pigsty dirty 12X",
            "D05 : desserts str 2 e",
            "D06 : xyz true",
            "D07 : 0 [he] false",
            "D08 : level true true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "new StringBuilder()", "new StringBuilder(\"", "re:new StringBuilder\\(\\d+\\)##new StringBuilder(capacite)", ".append(",
            ".insert(", ".delete(", ".deleteCharAt(", ".replace(",
            ".reverse()", ".substring(", ".indexOf(", ".charAt(",
            ".setLength(", ".contentEquals(",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall03", args, EXPECTED, API);
    }
}
