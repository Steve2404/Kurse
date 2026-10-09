package ch16_testing.projects.p04_roman;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON RomanNumerals et TES tests, ou avec l'argument "solution".
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("RomanNumerals.java", "\"M\", \"CM\", \"D\"", "\"M\", \"DCCCC\", \"D\""),
            new Mutant("RomanNumerals.java", "\"V\", \"IV\", \"I\"", "\"V\", \"IIII\", \"I\""),
            new Mutant("RomanNumerals.java", "n > 3999", "n > 4000"),
            new Mutant("RomanNumerals.java", "n < 1 ||", "n < 0 ||"),
            new Mutant("RomanNumerals.java", " || !toRoman(total).equals(s)", ""),
            new Mutant("RomanNumerals.java", "value < next ? -value : value", "value <= next ? -value : value"),
            new Mutant("RomanNumerals.java", "int next = i + 1 < s.length()", "int next = i + 1 < s.length() - 1"),
            new Mutant("RomanNumerals.java", "\"hors limites : \" + n", "\"hors limites\""),
            new Mutant("RomanNumerals.java", "total < 1 || total > 3999 || ", "total > 3999 || "),
            new Mutant("RomanNumerals.java", "case 'L' -> 50;", "case 'L' -> 40;"),
            new Mutant("RomanNumerals.java", "while (rest >= VALUES[i]) {", "if (rest >= VALUES[i]) {"));

    static final List<String> API_CODE = List.of(
            "final class RomanNumerals", "static String toRoman(int n)", "static int fromRoman(String s)",
            "StringBuilder", "Objects.requireNonNull(", "!double", "!float");

    static final List<String> API_TESTS = List.of(
            "@ParameterizedTest", "@CsvSource(", "@ValueSource(ints", "@ValueSource(strings", "@Test",
            "assertTimeout(", "Duration.ofSeconds(", "IntStream.rangeClosed(1, 3999)",
            "NullPointerException.class", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 25, MUTANTS, API_CODE, API_TESTS);
    }
}
