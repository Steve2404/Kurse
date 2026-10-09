package ch16_testing.drills.r05_kata_calculator;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5, un kata (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON StringCalculator et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("StringCalculator.java", "if (numbers.isEmpty()) {\n            return 0;", "if (numbers.isEmpty()) {\n            return -1;"),
            new Mutant("StringCalculator.java", "String separators = \"[,\\n]\";", "String separators = \",\";"),
            new Mutant("StringCalculator.java", "Pattern.quote(numbers.substring(2, 3)) + \"|\\n\"", "numbers.substring(2, 3) + \"|\\n\""),
            new Mutant("StringCalculator.java", "Pattern.quote(numbers.substring(2, 3)) + \"|\\n\"", "Pattern.quote(numbers.substring(2, 3))"),
            new Mutant("StringCalculator.java", "n <= 1000", "n < 1000"),
            new Mutant("StringCalculator.java", "if (n < 0) {", "if (n < -1) {"),
            new Mutant("StringCalculator.java", "negatives.add(n);", "if (negatives.isEmpty()) negatives.add(n);"),
            new Mutant("StringCalculator.java", "Collectors.joining(\", \")", "Collectors.joining(\",\")"),
            new Mutant("StringCalculator.java", "sum += n;", "sum = n;"));

    static final List<String> API_CODE = List.of(
            "final class StringCalculator", "static int add(String numbers)", "Objects.requireNonNull(", "Pattern.quote(",
            ".split(", "Integer.parseInt(");

    static final List<String> API_TESTS = List.of(
            "@Test", "@ParameterizedTest", "assertEquals(", "assertThrows(", ".getMessage()", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 10, MUTANTS, API_CODE, API_TESTS);
    }
}
