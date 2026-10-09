package ch17_algorithms.projects.p05_stacks.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference de la calculatrice. */
class CalculatorTest {

    @ParameterizedTest(name = "\"{0}\" equilibre")
    @ValueSource(strings = {"", "()", "([]{})", "f(a[i]) + {x}", "((()))"})
    void balancedStrings(String s) {
        assertTrue(Calculator.balanced(s));
    }

    @ParameterizedTest(name = "\"{0}\" desequilibre")
    @ValueSource(strings = {"(", ")", "(]", "([)]", "((", "())(", "}{"})
    void unbalancedStrings(String s) {
        assertFalse(Calculator.balanced(s));
    }

    @ParameterizedTest(name = "\"{0}\" = {1}")
    @CsvSource({"3 4 +, 7", "8 2 -, 6", "8 2 /, 4", "2 3 4 * +, 14", "5 1 2 + 4 * + 3 -, 14", "42, 42", "7 2 /, 3"})
    void reversePolishNotation(String expr, long expected) {
        assertEquals(expected, Calculator.evalRpn(expr));
    }

    @ParameterizedTest(name = "\"{0}\" refusee")
    @ValueSource(strings = {"+", "1 +", "1 2", "1 x +", "1 2 + +"})
    void invalidRpnIsRejected(String expr) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> Calculator.evalRpn(expr));
        assertEquals("expression invalide : " + expr, e.getMessage());
    }

    @Test
    void divisionByZeroIsNotHidden() {
        assertThrows(ArithmeticException.class, () -> Calculator.evalRpn("1 0 /"));
    }

    @Test
    void shuntingYardRespectsPriorityAndParentheses() {
        assertAll(
                () -> assertEquals(List.of("3", "4", "2", "*", "+"), Calculator.toRpn("3 + 4 * 2")),
                () -> assertEquals(List.of("3", "4", "+", "2", "*"), Calculator.toRpn("(3 + 4) * 2")),
                () -> assertEquals(List.of("8", "3", "-", "2", "-"), Calculator.toRpn("8 - 3 - 2")),
                () -> assertEquals(List.of("12", "34", "+"), Calculator.toRpn("12+34")));
    }

    @ParameterizedTest(name = "{0} = {1}")
    @CsvSource({"'1 + 2 * 3', 7", "'(1 + 2) * 3', 9", "'8 - 3 - 2', 3", "'100 / 10 / 5', 2", "'2 * (3 + 4) * (5 - 1)', 56", "'((7))', 7"})
    void evaluateInfix(String infix, long expected) {
        assertEquals(expected, Calculator.evaluate(infix));
    }

    @Test
    void badParenthesesAndCharactersAreRejected() {
        IllegalArgumentException open = assertThrows(IllegalArgumentException.class, () -> Calculator.toRpn("(1 + 2"));
        IllegalArgumentException close = assertThrows(IllegalArgumentException.class, () -> Calculator.toRpn("1 + 2)"));
        IllegalArgumentException unknown = assertThrows(IllegalArgumentException.class, () -> Calculator.toRpn("2 ^ 3"));
        assertAll(
                () -> assertEquals("parentheses desequilibrees", open.getMessage()),
                () -> assertEquals("parentheses desequilibrees", close.getMessage()),
                () -> assertEquals("caractere inconnu : ^", unknown.getMessage()));
    }

    @Test
    void aLongExpressionIsEvaluatedInLinearTime() {
        String sum = "1" + " + 1".repeat(200_000);
        String nested = "(".repeat(50_000) + "1" + ")".repeat(50_000);
        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {
            assertEquals(200_001, Calculator.evaluate(sum));
            assertEquals(1, Calculator.evaluate(nested));
            assertTrue(Calculator.balanced("([{}])".repeat(200_000)));
        });
    }
}
