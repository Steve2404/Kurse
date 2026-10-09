package ch16_testing.projects.p04_roman.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;

/** Les tests de reference du projet 4 : ceux ecrits AVANT le code, cycle apres cycle, plus deux filets de securite. */
class RomanNumeralsTest {

    // Les cycles 1 a 8 du TDD, ranges en un seul tableau a la fin (le refactoring touche aussi les tests).
    @ParameterizedTest(name = "{0} = {1}")
    @CsvSource({
            "1, I", "2, II", "3, III", "4, IV", "5, V", "9, IX", "10, X", "14, XIV",
            "40, XL", "90, XC", "400, CD", "900, CM", "1994, MCMXCIV", "2024, MMXXIV", "3999, MMMCMXCIX"})
    void convertsBothWays(int n, String roman) {
        assertEquals(roman, RomanNumerals.toRoman(n));
        assertEquals(n, RomanNumerals.fromRoman(roman));
    }

    @ParameterizedTest(name = "{0} est hors limites")
    @ValueSource(ints = {0, -1, 4000})
    void onlyOneToThreeThousandNineHundredNinetyNine(int n) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> RomanNumerals.toRoman(n));
        assertEquals("hors limites : " + n, e.getMessage());
    }

    // Pourquoi ces chaines : chacune donne un nombre si on se contente d'additionner, mais aucune n'est correcte.
    @ParameterizedTest(name = "\"{0}\" est refuse")
    @ValueSource(strings = {"IIII", "IC", "VV", "IIV", "MMMM", "", "iv", "XZ", "MCMC"})
    void malformedNumeralsAreRejected(String s) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> RomanNumerals.fromRoman(s));
        assertEquals("chiffre romain invalide : " + s, e.getMessage());
    }

    @Test
    void nullIsRejectedWithAClearMessage() {
        NullPointerException e = assertThrows(NullPointerException.class, () -> RomanNumerals.fromRoman(null));
        assertEquals("chiffre absent", e.getMessage());
    }

    // Le filet de securite : la PROPRIETE "aller-retour", sur les 3999 nombres, en moins d'une seconde.
    @Test
    void everyNumberSurvivesTheRoundTrip() {
        assertTimeout(Duration.ofSeconds(1), () ->
                IntStream.rangeClosed(1, 3999).forEach(n -> assertEquals(n, RomanNumerals.fromRoman(RomanNumerals.toRoman(n)))));
    }
}
