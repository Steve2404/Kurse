package ch16_testing.projects.p03_tax.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference de TaxCalculator : un tableau de cas plutot que dix copies du meme test. */
class TaxCalculatorTest {

    // Pourquoi ces lignes : chaque limite de tranche, juste dessous et juste dessus, et l'arrondi du demi-euro.
    @ParameterizedTest(name = "impot de {0} = {1}")
    @CsvSource(textBlock = """
            0,       0
            10000,   0
            10004,   0
            10005,   1
            25000,   1500
            25001,   1500
            25004,   1501
            60000,   10250
            60001,   10250
            100000,  26250
            """)
    void taxFollowsTheBrackets(long income, long expected) {
        assertEquals(expected, TaxCalculator.tax(income));
    }

    @ParameterizedTest(name = "taux marginal de {0} = {1} %")
    @CsvSource({"0, 0", "10000, 0", "10001, 10", "25000, 10", "25001, 25", "60000, 25", "60001, 40"})
    void marginalRateChangesJustAfterEachLimit(long income, int expected) {
        assertEquals(expected, TaxCalculator.marginalRate(income));
    }

    @ParameterizedTest(name = "revenu {0} refuse")
    @ValueSource(longs = {-1, -10_000})
    void negativeIncomeIsRejected(long income) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> TaxCalculator.tax(income));
        assertEquals("revenu negatif : " + income, e.getMessage());
        assertThrows(IllegalArgumentException.class, () -> TaxCalculator.marginalRate(income));
    }

    @ParameterizedTest(name = "{0} euros, {1} adulte(s), {2} enfant(s) : {3}")
    @MethodSource("households")
    void taxWithSharesSplitsTheIncome(long income, int adults, int children, long expected) {
        assertEquals(expected, TaxCalculator.taxWithShares(income, adults, children));
    }

    // Pourquoi une methode : les cas ont 4 colonnes et un commentaire chacun ; ce serait illisible en CSV.
    static Stream<Arguments> households() {
        return Stream.of(
                Arguments.of(50_000, 1, 0, 7750),     // une part : comme tax(50 000)
                Arguments.of(50_000, 2, 0, 3000),     // 2 parts : 2 x tax(25 000)
                Arguments.of(50_000, 2, 1, 2500),     // 2,5 parts : 2,5 x tax(20 000)
                Arguments.of(50_000, 2, 2, 2001),     // 3 parts : 3 x tax(16 666), soit 3 x 667
                Arguments.of(50_000, 2, 3, 1000),     // 4 parts : le 3e enfant compte une part entiere
                Arguments.of(0, 2, 3, 0));
    }

    @Test
    void householdIsValidated() {
        IllegalArgumentException a = assertThrows(IllegalArgumentException.class, () -> TaxCalculator.taxWithShares(1, 3, 0));
        IllegalArgumentException z = assertThrows(IllegalArgumentException.class, () -> TaxCalculator.taxWithShares(1, 0, 0));
        IllegalArgumentException c = assertThrows(IllegalArgumentException.class, () -> TaxCalculator.taxWithShares(1, 1, -1));
        assertEquals("adultes : 1 ou 2 (recu 3)", a.getMessage());
        assertEquals("adultes : 1 ou 2 (recu 0)", z.getMessage());
        assertEquals("enfants negatif : -1", c.getMessage());
    }

    @ParameterizedTest(name = "gagner 1 euro de plus que {0} ne coute jamais plus de 1 euro")
    @ValueSource(longs = {9_999, 10_000, 24_999, 25_000, 59_999, 60_000, 123_456})
    void oneMoreEuroNeverCostsMoreThanOneEuro(long income) {
        // Pourquoi ce test : une PROPRIETE, vraie pour tout revenu ; il attrape un bareme qui taxerait tout au taux du haut.
        long extra = TaxCalculator.tax(income + 1) - TaxCalculator.tax(income);
        assertTrue(extra >= 0 && extra <= 1, "supplement " + extra);
    }
}
