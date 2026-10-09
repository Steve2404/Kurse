package ch16_testing.drills.r05_kata_calculator.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Les tests du kata, dans l'ordre ou ils ont ete ecrits (un cycle rouge-vert chacun). */
class StringCalculatorTest {

    @Test
    void emptyIsZero() {
        assertEquals(0, StringCalculator.add(""));
    }

    // Piege : un \n dans un CsvSource est un vrai retour a la ligne, qui COUPE la ligne CSV ; d'ou des tests a part pour \n.
    // Pourquoi delimiter = '|' : les valeurs contiennent des virgules.
    @ParameterizedTest(name = "\"{0}\" -> {1}")
    @CsvSource(delimiter = '|', value = {"1|1", "1,2|3", "1,2,3,4|10", "2,1000|1002", "2,1001|2"})
    void sumsCommaSeparatedNumbers(String numbers, int expected) {
        assertEquals(expected, StringCalculator.add(numbers));
    }

    @Test
    void newlineIsASeparatorToo() {
        assertEquals(6, StringCalculator.add("1\n2,3"));
    }

    @Test
    void customDelimiter() {
        assertEquals(3, StringCalculator.add("//;\n1;2"));
        assertEquals(6, StringCalculator.add("//;\n1;2\n3"));
    }

    @Test
    void customDelimiterCanBeARegexCharacter() {
        assertEquals(3, StringCalculator.add("//.\n1.2"));
        assertEquals(7, StringCalculator.add("//|\n3|4"));
    }

    @Test
    void allNegativesAreListed() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> StringCalculator.add("1,-1,2,-3"));
        assertEquals("negatifs interdits : -1, -3", e.getMessage());
    }

    @Test
    void minusOneIsNegative() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> StringCalculator.add("-1"));
        assertEquals("negatifs interdits : -1", e.getMessage());
    }

    @Test
    void nullIsRejected() {
        NullPointerException e = assertThrows(NullPointerException.class, () -> StringCalculator.add(null));
        assertEquals("chaine absente", e.getMessage());
    }
}
