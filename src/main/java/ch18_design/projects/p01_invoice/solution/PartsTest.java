package ch18_design.projects.p01_invoice.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Les petites pieces, testees seules : chacune a UNE responsabilite, donc un test court. */
class PartsTest {

    @ParameterizedTest
    @CsvSource({"0, '0,00'", "5, '0,05'", "50, '0,50'", "123456, '1234,56'"})
    void moneyFormat(long cents, String text) {
        assertEquals(text, Money.ofCents(cents).format());
    }

    @ParameterizedTest
    @CsvSource({"15, 10, 2", "14, 10, 1", "1000, 20, 200", "2150, 10, 215"})
    void moneyPercentRoundsHalfUp(long cents, int percent, long expected) {
        assertEquals(Money.ofCents(expected), Money.ofCents(cents).percent(percent));
    }

    @Test
    void moneyArithmeticAndValidation() {
        assertAll(
                () -> assertEquals(Money.ofCents(30), Money.ofCents(10).plus(Money.ofCents(20))),
                () -> assertEquals(Money.ofCents(5), Money.ofCents(20).minus(Money.ofCents(15))),
                () -> assertEquals(Money.ofCents(60), Money.ofCents(20).times(3)),
                () -> assertEquals(Money.ZERO, Money.ofCents(0)),
                () -> assertThrows(IllegalArgumentException.class, () -> Money.ofCents(-1)),
                () -> assertThrows(IllegalArgumentException.class, () -> Money.ofCents(1).minus(Money.ofCents(2))));
    }

    @ParameterizedTest
    @CsvSource({"1, 15, 1800, '(0 h 15)'", "15, 15, 1800, '(0 h 15)'", "16, 30, 3600, '(0 h 30)'",
            "61, 75, 9000, '(1 h 15)'", "240, 240, 28800, '(4 h 00)'"})
    void laborIsBilledByStartedQuarter(int minutes, int billed, long cents, String time) {
        Labor labor = new Labor("Montage", minutes);
        assertAll(
                () -> assertEquals(billed, labor.billedMinutes()),
                () -> assertEquals(Money.ofCents(cents), labor.price()),
                () -> assertEquals("Montage " + time, labor.label()),
                () -> assertEquals(Category.LABOR, labor.category()));
    }

    @Test
    void partAndFee() {
        Part part = new Part("Disque", 2, Money.ofCents(6250));
        Fee fee = new Fee("Recyclage", Money.ofCents(350));
        assertAll(
                () -> assertEquals("Disque x2", part.label()),
                () -> assertEquals(Money.ofCents(12500), part.price()),
                () -> assertEquals(Category.PART, part.category()),
                () -> assertEquals(0, part.billedMinutes()),
                () -> assertEquals("Recyclage (forfait)", fee.label()),
                () -> assertEquals(Money.ofCents(350), fee.price()),
                () -> assertEquals(Category.FEE, fee.category()),
                () -> assertThrows(IllegalArgumentException.class, () -> new Part("X", 0, Money.ZERO)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Labor("X", 0)));
    }

    @Test
    void parser() {
        assertAll(
                () -> assertEquals(new Part("Vis", 3, Money.ofCents(20)), LineParser.parse("PART;Vis;3;20")),
                () -> assertEquals(new Labor("Pose", 50), LineParser.parse("LABOR;Pose;50")),
                () -> assertEquals(new Fee("Taxe", Money.ofCents(100)), LineParser.parse("FEE;Taxe;100")));
    }

    // Le calcul seul, sans texte : on lit directement chaque montant.
    @Test
    void calculatorWithBothDiscounts() {
        List<InvoiceLine> lines = List.of(new Part("Moteur", 1, Money.ofCents(30_000)), new Labor("Pose", 300),
                new Fee("Taxe", Money.ofCents(1000)));
        InvoiceTotals t = new InvoiceCalculator().compute(new Customer("X", true), lines);
        assertEquals(new InvoiceTotals(Money.ofCents(30_000), Money.ofCents(36_000), Money.ofCents(1000),
                Money.ofCents(3000), Money.ofCents(1800), Money.ofCents(62_200), Money.ofCents(12_440),
                Money.ofCents(74_640)), t);
    }

    @Test
    void notLoyalMeansNoLoyaltyDiscount() {
        InvoiceTotals t = new InvoiceCalculator().compute(new Customer("X", false),
                List.of(new Part("Moteur", 1, Money.ofCents(30_000))));
        assertEquals(Money.ZERO, t.loyaltyDiscount());
        assertEquals(Money.ofCents(30_000), t.net());
    }
}
