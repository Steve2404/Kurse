package ch16_testing.projects.p02_cart.solution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference du projet 2, ranges par situation avec @Nested. */
@DisplayName("Le panier")
class CartTest {

    // Pourquoi un champ + @BeforeEach : chaque test recoit un panier NEUF (JUnit cree une instance par test).
    private Cart cart;

    @BeforeEach
    void newCart() {
        cart = new Cart();
    }

    @Nested
    @DisplayName("quand il est vide")
    class WhenEmpty {

        @Test
        @DisplayName("n'a ni ligne, ni total, ni livraison")
        void hasNothing() {
            assertAll(
                    () -> assertTrue(cart.isEmpty()),
                    () -> assertEquals(List.of(), cart.lines()),
                    () -> assertEquals(0, cart.subtotalCents()),
                    () -> assertEquals(0, cart.shippingCents()),
                    () -> assertEquals(0, cart.totalCents()));
        }

        @Test
        @DisplayName("rend 0 pour un article inconnu")
        void unknownQuantityIsZero() {
            assertEquals(0, cart.quantity("APPLE"));
        }

        @Test
        @DisplayName("refuse de retirer un article absent")
        void removingAnAbsentItemFails() {
            NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> cart.remove("APPLE", 1));
            assertEquals("article absent : APPLE", e.getMessage());
        }
    }

    @Nested
    @DisplayName("avec trois articles")
    class WithItems {

        @BeforeEach
        void fill() {
            // Pourquoi cet ordre : ajoutes dans le desordre, pour tester le tri de lines().
            cart.add("PEAR", 2, 120);
            cart.add("APPLE", 3, 50);
            cart.add("MILK", 1, 99);
        }

        @Test
        @DisplayName("liste les lignes triees par code article")
        void linesAreSortedBySku() {
            assertEquals(List.of("APPLE x 3 = 150", "MILK x 1 = 99", "PEAR x 2 = 240"), cart.lines());
        }

        @Test
        @DisplayName("additionne les lignes et ajoute la livraison sous 30 EUR")
        void totals() {
            assertAll(
                    () -> assertFalse(cart.isEmpty()),
                    () -> assertEquals(489, cart.subtotalCents()),
                    () -> assertEquals(490, cart.shippingCents()),
                    () -> assertEquals(979, cart.totalCents()));
        }

        @Test
        @DisplayName("cumule les quantites d'un meme article")
        void addingTheSameItemAccumulates() {
            cart.add("APPLE", 2, 50);
            assertEquals(5, cart.quantity("APPLE"));
        }

        @Test
        @DisplayName("retire une partie d'une ligne")
        void removePartOfALine() {
            cart.remove("PEAR", 1);
            assertEquals(1, cart.quantity("PEAR"));
        }

        @Test
        @DisplayName("supprime la ligne quand on retire tout, ou plus")
        void removeAllDeletesTheLine() {
            cart.remove("PEAR", 2);
            cart.remove("APPLE", 10);
            assertEquals(List.of("MILK x 1 = 99"), cart.lines());
        }

        @Test
        @DisplayName("rend une liste qu'on ne peut pas modifier")
        void linesCannotBeModified() {
            List<String> lines = cart.lines();
            assertThrows(UnsupportedOperationException.class, () -> lines.add("FRAUDE x 1 = 0"));
        }

        @Test
        @DisplayName("refuse un autre prix pour le meme article, sans rien changer")
        void differentPriceIsRejected() {
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> cart.add("APPLE", 1, 60));
            assertAll(
                    () -> assertEquals("prix different pour APPLE", e.getMessage()),
                    () -> assertEquals(3, cart.quantity("APPLE")));
        }
    }

    @Nested
    @DisplayName("refuse les ajouts absurdes")
    class InvalidAdds {

        @Test
        void missingOrBlankSku() {
            NullPointerException n = assertThrows(NullPointerException.class, () -> cart.add(null, 1, 10));
            IllegalArgumentException b = assertThrows(IllegalArgumentException.class, () -> cart.add("  ", 1, 10));
            assertAll(
                    () -> assertEquals("sku absent", n.getMessage()),
                    () -> assertEquals("sku vide", b.getMessage()));
        }

        @Test
        void zeroQuantityAndNegativePrice() {
            IllegalArgumentException q = assertThrows(IllegalArgumentException.class, () -> cart.add("APPLE", 0, 10));
            IllegalArgumentException p = assertThrows(IllegalArgumentException.class, () -> cart.add("APPLE", 1, -1));
            assertAll(
                    () -> assertEquals("quantite invalide : 0", q.getMessage()),
                    () -> assertEquals("prix negatif : -1", p.getMessage()));
        }

        @Test
        void freeItemIsAllowed() {
            cart.add("SAMPLE", 1, 0);
            assertEquals(1, cart.quantity("SAMPLE"));
        }

        @Test
        @DisplayName("99 par article au plus, et un refus ne change rien")
        void ninetyNineIsTheMaximum() {
            cart.add("APPLE", 99, 50);
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> cart.add("APPLE", 1, 50));
            assertAll(
                    () -> assertEquals("maximum 99 par article : APPLE", e.getMessage()),
                    () -> assertEquals(99, cart.quantity("APPLE")));
        }

        @Test
        void removingZeroIsRejected() {
            cart.add("APPLE", 1, 50);
            assertThrows(IllegalArgumentException.class, () -> cart.remove("APPLE", 0));
        }
    }

    @Nested
    @DisplayName("les seuils de livraison et de remise")
    class Thresholds {

        @Test
        @DisplayName("livraison gratuite a partir de 30 EUR pile")
        void freeShippingFromExactlyThirtyEuros() {
            cart.add("BOX", 1, 2999);
            assertEquals(490, cart.shippingCents());
            cart.remove("BOX", 1);
            cart.add("BOX", 1, 3000);
            assertEquals(0, cart.shippingCents());
        }

        @Test
        @DisplayName("MOINS10 : rien sous 50 EUR, 10 % a partir de 50 EUR pile")
        void discountFromExactlyFiftyEuros() {
            cart.applyCode("MOINS10");
            cart.add("BOX", 1, 4999);
            assertEquals(0, cart.discountCents());
            cart.remove("BOX", 1);
            cart.add("BOX", 1, 5000);
            assertAll(
                    () -> assertEquals(500, cart.discountCents()),
                    () -> assertEquals(4500, cart.totalCents()));
        }

        @Test
        @DisplayName("la remise est arrondie au centime le plus proche")
        void discountIsRounded() {
            cart.applyCode("MOINS10");
            cart.add("BOX", 1, 5005);
            assertEquals(501, cart.discountCents());
        }

        @Test
        @DisplayName("LIVRAISON offre le port, meme pour un petit panier")
        void deliveryCodeMakesShippingFree() {
            cart.add("MILK", 1, 99);
            cart.applyCode("LIVRAISON");
            assertAll(
                    () -> assertEquals(0, cart.shippingCents()),
                    () -> assertEquals(99, cart.totalCents()));
        }

        @Test
        @DisplayName("un seul code, et seulement un code connu")
        void codesAreChecked() {
            IllegalArgumentException u = assertThrows(IllegalArgumentException.class, () -> cart.applyCode("GRATUIT"));
            cart.applyCode("MOINS10");
            IllegalStateException s = assertThrows(IllegalStateException.class, () -> cart.applyCode("LIVRAISON"));
            assertAll(
                    () -> assertEquals("code inconnu : GRATUIT", u.getMessage()),
                    () -> assertEquals("un seul code par panier", s.getMessage()));
        }
    }
}
