package ch16_testing.projects.p01_vat.solution;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Les tests de reference du projet 1.
 * Chaque test suit le schema Arrange / Act / Assert, et son nom dit ce qu'il verifie.
 */
class VatCalculatorTest {

    // ---- etape 2 : les cas normaux

    @Test
    void vatOfTenEurosAtTwentyPercentIsTwoEuros() {
        long vat = VatCalculator.vat(1000, 20);
        assertEquals(200, vat);
    }

    @Test
    void grossAddsTheVatToTheNetAmount() {
        assertEquals(1200, VatCalculator.gross(1000, 20));
    }

    @Test
    void grossMinusNetIsAlwaysTheVat() {
        // Pourquoi ce test : il relie deux methodes ; un arrondi different dans l'une le casse.
        assertEquals(VatCalculator.vat(1999, 20), VatCalculator.gross(1999, 20) - 1999);
    }

    // ---- etape 3 : les exceptions

    @Test
    void negativeAmountIsRejectedWithItsValueInTheMessage() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> VatCalculator.vat(-1, 20));
        assertEquals("montant negatif : -1", e.getMessage());
    }

    @Test
    void rateAboveOneHundredIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> VatCalculator.vat(1000, 101));
        assertEquals("taux invalide : 101", e.getMessage());
    }

    @Test
    void negativeRateIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> VatCalculator.gross(1000, -1));
    }

    // ---- etape 4 : les valeurs limites

    @Test
    void zeroCentIsAllowed() {
        assertEquals(0, VatCalculator.gross(0, 20));
    }

    @Test
    void zeroAndOneHundredPercentAreAllowed() {
        assertEquals(1000, VatCalculator.gross(1000, 0));
        assertEquals(2000, VatCalculator.gross(1000, 100));
    }

    @Test
    void halfACentRoundsUp() {
        // 25 x 2 % = 0,50 centime -> 1 ; 24 x 2 % = 0,48 centime -> 0.
        assertEquals(1, VatCalculator.vat(25, 2));
        assertEquals(0, VatCalculator.vat(24, 2));
    }

    @Test
    void roundingGoesToTheNearestCent() {
        // 1999 x 20 % = 399,8 centimes -> 400 (un arrondi par troncature donnerait 399).
        assertEquals(400, VatCalculator.vat(1999, 20));
    }

    // ---- etape 5 : les categories et l'affichage

    @Test
    void eachCategoryHasItsRate() {
        assertEquals(20, VatCalculator.rateFor("standard"));
        assertEquals(10, VatCalculator.rateFor("intermediaire"));
        assertEquals(5, VatCalculator.rateFor("reduit"));
    }

    @Test
    void categoryIgnoresCaseAndSurroundingSpaces() {
        assertEquals(10, VatCalculator.rateFor("  INTERMEDIAIRE "));
    }

    @Test
    void unknownAndMissingCategoriesAreRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> VatCalculator.rateFor("luxe"));
        assertEquals("categorie inconnue : luxe", e.getMessage());
        NullPointerException n = assertThrows(NullPointerException.class, () -> VatCalculator.rateFor(null));
        assertEquals("categorie absente", n.getMessage());
    }

    @Test
    void formatShowsEurosAndTwoDigitsOfCents() {
        assertEquals("12,34 EUR", VatCalculator.format(1234));
        assertEquals("0,05 EUR", VatCalculator.format(5));
        assertEquals("0,00 EUR", VatCalculator.format(0));
    }

    @Test
    void formatKeepsTheSignOfANegativeAmount() {
        assertEquals("-1,50 EUR", VatCalculator.format(-150));
        assertEquals("-0,05 EUR", VatCalculator.format(-5));
    }
}
