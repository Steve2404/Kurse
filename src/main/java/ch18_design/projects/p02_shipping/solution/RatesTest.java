package ch18_design.projects.p02_shipping.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Chaque strategie se teste seule, sans le calculateur. */
class RatesTest {

    @ParameterizedTest
    @CsvSource({"1, FR, 495", "500, FR, 495", "501, FR, 750", "2001, FR, 1150", "5001, FR, 1270", "6000, FR, 1270",
            "6001, FR, 1390", "30000, FR, 4150", "500, DE, 990", "30000, US, 8300"})
    void post(int grams, String country, long price) {
        assertEquals(price, new PostRate().basePrice(new Parcel(grams, country, false)));
    }

    @ParameterizedTest
    @CsvSource({"1, FR, 1540", "1000, FR, 1540", "1001, FR, 1790", "1001, BE, 3290"})
    void express(int grams, String country, long price) {
        assertEquals(price, new ExpressRate().basePrice(new Parcel(grams, country, false)));
    }

    @Test
    void acceptance() {
        assertAll(
                () -> assertTrue(new PostRate().accepts(new Parcel(30_000, "US", false))),
                () -> assertFalse(new PostRate().accepts(new Parcel(30_001, "FR", false))),
                () -> assertFalse(new ExpressRate().accepts(new Parcel(30_001, "FR", false))),
                () -> assertTrue(new PickupRate().accepts(new Parcel(20_000, "BE", false))),
                () -> assertFalse(new PickupRate().accepts(new Parcel(20_001, "FR", false))),
                () -> assertFalse(new PickupRate().accepts(new Parcel(100, "DE", false))),
                () -> assertFalse(new FreightRate().accepts(new Parcel(30_000, "FR", false))),
                () -> assertTrue(new FreightRate().accepts(new Parcel(30_001, "FR", false))),
                () -> assertFalse(new FreightRate().accepts(new Parcel(40_000, "BE", false))));
    }

    @ParameterizedTest
    @CsvSource({"30001, 4990", "31000, 4990", "31001, 5080", "40000, 5800"})
    void freight(int grams, long price) {
        assertEquals(price, new FreightRate().basePrice(new Parcel(grams, "FR", false)));
    }

    @Test
    void surcharges() {
        assertAll(
                () -> assertEquals(59, Surcharges.fragile().amount(new Parcel(10, "FR", true), 390)),
                () -> assertEquals(0, Surcharges.fragile().amount(new Parcel(10, "FR", false), 390)),
                () -> assertEquals(800, Surcharges.customs().amount(new Parcel(10, "CH", false), 390)),
                () -> assertEquals(0, Surcharges.customs().amount(new Parcel(10, "LU", false), 390)));
    }

    @Test
    void parcelValidation() {
        assertAll(
                () -> assertEquals("poids invalide : 0",
                        assertThrows(IllegalArgumentException.class, () -> new Parcel(0, "FR", false)).getMessage()),
                () -> assertEquals("pays invalide : fr",
                        assertThrows(IllegalArgumentException.class, () -> new Parcel(10, "fr", false)).getMessage()),
                () -> assertEquals("pays invalide : null",
                        assertThrows(IllegalArgumentException.class, () -> new Parcel(10, null, false)).getMessage()),
                () -> assertEquals(2, new Parcel(1001, "FR", false).startedKilos()),
                () -> assertEquals(1, new Parcel(1000, "FR", false).startedKilos()));
    }
}
