package ch16_testing.drills.r02_lifecycle;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall02Test, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "d01 : 1 executions, 1 reussies",
            "d02 : 1 executions, 1 reussies",
            "d03 : 1 executions, 1 reussies",
            "d04 : 3 executions, 3 reussies",
            "d06 : 1 executions, 1 reussies",
            "d07 : 1 executions, 1 reussies",
            "d08a : 1 executions, 1 reussies",
            "d08b : 1 executions, 1 reussies");
            // EXPECTED-END

    static final List<String> API = List.of(
            "@BeforeAll", "static void", "@BeforeEach", "@AfterEach", "@RepeatedTest(3)", "RepetitionInfo",
            "@Disabled(", "fail(", "@Nested", "@DisplayName(", "@Tag(", "@TestInstance(TestInstance.Lifecycle.PER_CLASS)",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkDrill(Check.class, args, EXPECTED, API);
    }
}
