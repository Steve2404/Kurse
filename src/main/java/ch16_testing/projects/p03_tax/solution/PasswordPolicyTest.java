package ch16_testing.projects.p03_tax.solution;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference de PasswordPolicy : une classe d'equivalence par regle, plus les limites de longueur. */
class PasswordPolicyTest {

    @ParameterizedTest(name = "[{index}] valide : \"{0}\"")
    @ValueSource(strings = {"Abcdefghij1!", "Mot-De-Passe-2024", "Zz9#Zz9#Zz9#"})
    void goodPasswordsAreValid(String password) {
        assertTrue(PasswordPolicy.isValid(password));
        assertEquals(List.of(), PasswordPolicy.violations(password));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void nullAndEmptyAreJustEmpty(String password) {
        assertEquals(List.of("vide"), PasswordPolicy.violations(password));
        assertFalse(PasswordPolicy.isValid(password));
    }

    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @MethodSource("oneRuleBroken")
    void eachRuleHasItsOwnMessage(String password, List<String> expected) {
        assertEquals(expected, PasswordPolicy.violations(password));
    }

    // Pourquoi une regle cassee a la fois : si un mot de passe cassait deux regles, on ne saurait pas laquelle est testee.
    static Stream<Arguments> oneRuleBroken() {
        return Stream.of(
                Arguments.of("Abcdefghi1!", List.of("trop court")),             // 11 caracteres
                Arguments.of("Abcdefghijk!", List.of("sans chiffre")),
                Arguments.of("abcdefghij1!", List.of("sans majuscule")),
                Arguments.of("ABCDEFGHIJ1!", List.of("sans minuscule")),
                Arguments.of("Abcdefghijk1", List.of("sans symbole")),
                Arguments.of("Abcde fghij1", List.of("espace interdit")));    // l'espace compte comme symbole
    }

    @ParameterizedTest(name = "longueur {0} : {1}")
    @MethodSource("lengths")
    void lengthLimitsAreTwelveAndSixtyFour(int length, List<String> expected) {
        String password = "Aa1!" + "x".repeat(length - 4);
        assertEquals(expected, PasswordPolicy.violations(password));
    }

    static Stream<Arguments> lengths() {
        return Stream.of(
                Arguments.of(11, List.of("trop court")),
                Arguments.of(12, List.of()),
                Arguments.of(64, List.of()),
                Arguments.of(65, List.of("trop long")));
    }

    @ParameterizedTest(name = "\"{0}\" -> toutes les violations, dans l'ordre")
    @ValueSource(strings = {" "})
    void allViolationsComeInTheFixedOrder(String password) {
        assertEquals(List.of("trop court", "sans chiffre", "sans majuscule", "sans minuscule", "espace interdit"),
                PasswordPolicy.violations(password));
    }
}
