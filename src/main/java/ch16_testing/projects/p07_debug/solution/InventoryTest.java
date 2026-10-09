package ch16_testing.projects.p07_debug.solution;

import ch16_testing.projects.p07_debug.Data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference du projet 7 : un test de non-regression par bug trouve au debogueur, plus le scenario de Data. */
class InventoryTest {

    private Inventory inv;

    @BeforeEach
    void newInventory() {
        inv = new Inventory();
    }

    @Test
    @DisplayName("le scenario de Data donne enfin la bonne sortie")
    void dataScenario() {
        for (String line : Data.DELIVERIES) {
            inv.receive(line);
        }
        assertAll(
                () -> assertEquals(200, inv.quantity("PEN")),
                () -> assertEquals(17016, inv.averagePriceCents()),
                () -> assertTrue(inv.ship("PEN", 200)),
                () -> assertEquals(0, inv.quantity("PEN")),
                () -> assertEquals(5_001_313_175L, inv.stockValueCents()),
                () -> assertTrue(inv.sameQuantity("INK", "CUP")),
                () -> assertEquals(List.of("GLUE", "PAD", "PEN"), inv.lowStock(100)));
    }

    @Test
    @DisplayName("BUG 1 : deux livraisons du meme code font UN article")
    void sameSkuTwiceMakesOneItem() {
        // split() fabrique une NOUVELLE chaine "PEN" a chaque ligne : == ne les reconnait jamais comme egales.
        inv.receive("PEN;10;100");
        inv.receive("PEN;5;100");
        inv.receive("INK;1;300");
        assertAll(
                () -> assertEquals(15, inv.quantity("PEN")),
                () -> assertEquals(200, inv.averagePriceCents()),
                () -> assertTrue(inv.ship("PEN", 15)));
    }

    @Test
    @DisplayName("BUG 2 : on peut expedier exactement tout le stock")
    void shipEverything() {
        inv.receive("PEN;10;100");
        assertAll(
                () -> assertTrue(inv.ship("PEN", 10)),
                () -> assertEquals(0, inv.quantity("PEN")),
                () -> assertFalse(inv.ship("PEN", 1)),
                () -> assertFalse(inv.ship("NOPE", 1)));
    }

    @Test
    @DisplayName("BUG 3 : la valeur du stock ne deborde pas")
    void stockValueDoesNotOverflow() {
        inv.receive("BOX;50000;99999");
        assertEquals(4_999_950_000L, inv.stockValueCents());
    }

    @Test
    @DisplayName("BUG 4 : le prix moyen est arrondi, pas tronque")
    void averageIsRounded() {
        inv.receive("A;1;100");
        inv.receive("B;1;101");
        assertEquals(101, inv.averagePriceCents());
    }

    @Test
    void averageOfNothingIsZero() {
        assertEquals(0, inv.averagePriceCents());
    }

    @Test
    @DisplayName("BUG 5 : 1000 et 1000 sont la meme quantite")
    void largeEqualQuantities() {
        inv.receive("INK;1000;899");
        inv.receive("CUP;1000;450");
        inv.receive("PAD;40;320");
        assertAll(
                () -> assertTrue(inv.sameQuantity("INK", "CUP")),
                () -> assertFalse(inv.sameQuantity("INK", "PAD")));
    }

    @Test
    @DisplayName("BUG 6 : le premier article compte aussi dans le stock bas")
    void lowStockIncludesTheFirstItem() {
        inv.receive("GLUE;5;275");
        inv.receive("INK;1000;899");
        assertEquals(List.of("GLUE"), inv.lowStock(100));
    }

    @ParameterizedTest(name = "livraison \"{0}\" refusee")
    @ValueSource(strings = {"PEN;0;100", "PEN;5;-1", "PEN;cinq;100", "PEN;5", "PEN;5;100;x"})
    void malformedDeliveriesAreRejectedWithoutChangingTheStock(String line) {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> inv.receive(line));
        assertAll(
                () -> assertEquals("livraison invalide : " + line, e.getMessage()),
                () -> assertEquals(0, inv.quantity("PEN")));
    }

    @Test
    void shippingZeroIsRejected() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> inv.ship("PEN", 0));
        assertEquals("quantite invalide : 0", e.getMessage());
    }
}
