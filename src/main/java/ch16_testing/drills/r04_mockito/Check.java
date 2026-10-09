package ch16_testing.drills.r04_mockito;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04Test, ou avec l'argument "solution".
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
            "@ExtendWith(MockitoExtension.class)", "@Mock", "mock(", "when(", ".thenReturn(", ".thenThrow(",
            ".thenAnswer(", "getArgument(", "verify(", "times(2)", "never()", "atLeastOnce()", "ArgumentCaptor",
            ".getAllValues()", "inOrder(", "spy(", "doReturn(", "doThrow(", "verifyNoInteractions(",
            "verifyNoMoreInteractions(", "eq(", "argThat(", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkDrill(Check.class, args, EXPECTED, API);
    }
}
