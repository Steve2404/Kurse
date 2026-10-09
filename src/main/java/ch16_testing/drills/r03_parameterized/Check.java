package ch16_testing.drills.r03_parameterized;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall03Test, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "d01 : 4 executions, 4 reussies",
            "d02 : 3 executions, 3 reussies",
            "d03 : 3 executions, 3 reussies",
            "d04 : 3 executions, 3 reussies",
            "d05 : 2 executions, 2 reussies",
            "d06 : 2 executions, 2 reussies",
            "d07 : 4 executions, 4 reussies",
            "d08 : 7 executions, 7 reussies",
            "d09 : 2 executions, 2 reussies",
            "d10 : 5 executions, 5 reussies",
            "d11 : 3 executions, 3 reussies",
            "d12 : 5 executions, 5 reussies");
            // EXPECTED-END

    static final List<String> API = List.of(
            "@ValueSource(ints", "@ValueSource(strings", "@CsvSource({", "useHeadersInDisplayName = true",
            "textBlock = \"\"\"", "nullValues = \"N/A\"", "@NullAndEmptySource", "@EnumSource(DayOfWeek.class)",
            "names = {", "EnumSource.Mode.EXCLUDE", "@MethodSource(", "Stream<Arguments>", "IntStream",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkDrill(Check.class, args, EXPECTED, API);
    }
}
