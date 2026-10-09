package ch16_testing.drills.r01_assertions;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01Test, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "d01 : 1 executions, 1 reussies",
            "d02 : 1 executions, 1 reussies",
            "d03 : 1 executions, 1 reussies",
            "d04 : 1 executions, 1 reussies",
            "d05 : 1 executions, 1 reussies",
            "d06 : 1 executions, 1 reussies",
            "d07 : 1 executions, 1 reussies",
            "d08 : 1 executions, 1 reussies",
            "d09 : 1 executions, 1 reussies",
            "d10 : 1 executions, 1 reussies",
            "d11 : 1 executions, 1 reussies",
            "d12 : 1 executions, 1 reussies");
            // EXPECTED-END

    static final List<String> API = List.of(
            "assertEquals(", "assertNotEquals(", "assertTrue(", "assertFalse(", "assertNull(", "assertNotNull(",
            "assertSame(", "assertNotSame(", "assertArrayEquals(", "assertIterableEquals(", "assertThrows(",
            "assertDoesNotThrow(", "assertAll(", "assertTimeout(", "1e-9", ".getMessage()",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkDrill(Check.class, args, EXPECTED, API);
    }
}
