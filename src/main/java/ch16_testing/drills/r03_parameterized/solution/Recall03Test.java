package ch16_testing.drills.r03_parameterized.solution;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DayOfWeek;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Le corrige du drill 3 : chaque source de cas, et le nombre d'executions qu'elle produit. */
class Recall03Test {

    @ParameterizedTest
    @ValueSource(ints = {2, 4, 6, 8})
    void d01(int n) {
        assertEquals(0, n % 2);
    }

    @ParameterizedTest
    @ValueSource(strings = {"radar", "kayak", "elle"})
    void d02(String word) {
        assertEquals(word, new StringBuilder(word).reverse().toString());
    }

    @ParameterizedTest
    @CsvSource({"1, 1", "2, 4", "3, 9"})
    void d03(int n, int square) {
        assertEquals(square, n * n);
    }

    // Piege : avec useHeadersInDisplayName, la 1re ligne est un EN-TETE, pas un cas.
    @ParameterizedTest(name = "{arguments}")
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
            mot,       longueur
            java,      4
            junit,     5
            mockito,   7
            """)
    void d04(String word, int length) {
        assertEquals(length, word.length());
    }

    // Piege : une virgule DANS une valeur se protege par des apostrophes.
    @ParameterizedTest
    @CsvSource({"'a,b,c', 3", "x, 1"})
    void d05(String text, int parts) {
        assertEquals(parts, text.split(",").length);
    }

    @ParameterizedTest
    @CsvSource(value = {"N/A, true", "abc, false"}, nullValues = "N/A")
    void d06(String value, boolean isNull) {
        assertEquals(isNull, value == null);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t"})
    void d07(String s) {
        assertTrue(s == null || s.isBlank());
    }

    @ParameterizedTest
    @EnumSource(DayOfWeek.class)
    void d08(DayOfWeek day) {
        assertTrue(day.getValue() >= 1 && day.getValue() <= 7);
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek.class, names = {"SATURDAY", "SUNDAY"})
    void d09(DayOfWeek day) {
        assertTrue(day.getValue() >= 6);
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek.class, names = {"SATURDAY", "SUNDAY"}, mode = EnumSource.Mode.EXCLUDE)
    void d10(DayOfWeek day) {
        assertTrue(day.getValue() <= 5);
    }

    @ParameterizedTest(name = "{index} : {0} + {1} = {2}")
    @MethodSource("sums")
    void d11(int a, int b, int sum) {
        assertEquals(sum, a + b);
    }

    static Stream<Arguments> sums() {
        return Stream.of(Arguments.of(1, 1, 2), Arguments.of(2, 3, 5), Arguments.of(-4, 4, 0));
    }

    // Une seule colonne : la methode peut rendre directement un IntStream (ou un Stream<String>...).
    @ParameterizedTest
    @MethodSource("smallNumbers")
    void d12(int n) {
        assertTrue(n >= 0 && n < 5);
    }

    static IntStream smallNumbers() {
        return IntStream.range(0, 5);
    }
}
