package ch16_testing.projects.p01_vat;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON VatCalculator et TES tests, ou avec l'argument "solution".
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("VatCalculator.java", "(netCents * ratePercent + 50) / 100", "(netCents * ratePercent) / 100"),
            new Mutant("VatCalculator.java", "ratePercent > 100", "ratePercent >= 100"),
            new Mutant("VatCalculator.java", "netCents < 0", "netCents <= 0"),
            new Mutant("VatCalculator.java", "ratePercent < 0 || ", ""),
            new Mutant("VatCalculator.java", "%s%d,%02d EUR", "%s%d,%d EUR"),
            new Mutant("VatCalculator.java", "String sign = cents < 0 ? \"-\" : \"\";", "String sign = \"\";"),
            new Mutant("VatCalculator.java", "category.strip().toLowerCase(Locale.ROOT)", "category.strip()"),
            new Mutant("VatCalculator.java", "case \"reduit\" -> 5;", "case \"reduit\" -> 10;"),
            new Mutant("VatCalculator.java", "\"montant negatif : \" + netCents", "\"montant negatif\""));

    static final List<String> API_CODE = List.of(
            "final class VatCalculator", "static long vat(long netCents, int ratePercent)",
            "static long gross(long netCents, int ratePercent)", "static int rateFor(String category)",
            "static String format(long cents)", "throw new IllegalArgumentException(", "Objects.requireNonNull(",
            "Locale.ROOT", "String.format(",
            // L'argent se compte en centimes : pas de nombres a virgule.
            "!double", "!float");

    static final List<String> API_TESTS = List.of(
            "@Test", "import static org.junit.jupiter.api.Assertions.", "assertEquals(", "assertThrows(",
            "IllegalArgumentException.class", "NullPointerException.class", ".getMessage()",
            // Un test VERIFIE, il n'affiche pas.
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 12, MUTANTS, API_CODE, API_TESTS);
    }
}
