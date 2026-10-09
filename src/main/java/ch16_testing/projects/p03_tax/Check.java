package ch16_testing.projects.p03_tax;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("TaxCalculator.java", "return (hundredths + 50) / 100;", "return hundredths / 100;"),
            new Mutant("TaxCalculator.java", "return RATES[RATES.length - 1];", "return RATES[RATES.length - 2];"),
            new Mutant("TaxCalculator.java", "{10_000, 25_000, 60_000}", "{10_000, 25_000, 50_000}"),
            new Mutant("TaxCalculator.java", "{0, 10, 25, 40}", "{0, 10, 25, 45}"),
            new Mutant("TaxCalculator.java", "if (income <= LIMITS[i]) {", "if (income < LIMITS[i]) {"),
            new Mutant("TaxCalculator.java", "2 * Math.max(0, children - 2)", "Math.max(0, children - 2)"),
            new Mutant("TaxCalculator.java", "Math.min(children, 2)", "Math.min(children, 3)"),
            new Mutant("TaxCalculator.java", "adults < 1 || adults > 2", "adults > 2"),
            new Mutant("TaxCalculator.java", "if (income < 0) {", "if (income < -1) {"),
            new Mutant("TaxCalculator.java", "hundredths += (Math.min(income, upper) - lower) * RATES[i];",
                    "hundredths = income * RATES[i];"),
            new Mutant("PasswordPolicy.java", "password.length() < 12", "password.length() < 11"),
            new Mutant("PasswordPolicy.java", "password.length() > 64", "password.length() >= 64"),
            new Mutant("PasswordPolicy.java", "password == null || password.isEmpty()", "password.isEmpty()"),
            new Mutant("PasswordPolicy.java", "allMatch(Character::isLetterOrDigit)", "allMatch(Character::isLetter)"),
            new Mutant("PasswordPolicy.java", "v.add(\"espace interdit\");", ""),
            new Mutant("PasswordPolicy.java", "noneMatch(Character::isLowerCase)", "noneMatch(Character::isLetter)"),
            new Mutant("PasswordPolicy.java", "return List.of(\"vide\");", "return List.of(\"trop court\");"));

    static final List<String> API_CODE = List.of(
            "final class TaxCalculator", "static long tax(long income)", "static int marginalRate(long income)",
            "static long taxWithShares(long income, int adults, int children)",
            "final class PasswordPolicy", "static List<String> violations(String password)",
            "static boolean isValid(String password)", "!double", "!float");

    static final List<String> API_TESTS = List.of(
            "@ParameterizedTest", "@CsvSource(", "textBlock = \"\"\"", "@ValueSource(", "@NullAndEmptySource",
            "@MethodSource(", "Arguments.of(", "Stream<Arguments>", "name = \"", "{0}",
            "re:@ParameterizedTest[^@]*@ValueSource\\(longs##@ValueSource(longs = ...)",
            "re:@ValueSource\\(strings##@ValueSource(strings = ...)",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 40, MUTANTS, API_CODE, API_TESTS);
    }
}
