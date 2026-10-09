package ch18_design.drills.r02_strategies.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Les tests de reference du drill 2. Le 13 octobre 2026 est un mardi, le 14 un mercredi. */
class Recall02Test {

    static final LocalDate TUESDAY = LocalDate.of(2026, 10, 13);
    static final LocalDate WEDNESDAY = LocalDate.of(2026, 10, 14);

    private static Clock at(LocalDate day) {
        return Clock.fixed(Instant.parse(day + "T12:00:00Z"), ZoneOffset.UTC);
    }

    @Test
    void d01() {
        ThreeForTwo promo = new ThreeForTwo();
        assertEquals(0, promo.discount(List.of(500L, 300L), WEDNESDAY));
        assertEquals(200, promo.discount(List.of(200L, 500L, 300L), WEDNESDAY));
        assertEquals(500, promo.discount(List.of(100L, 400L, 200L, 300L, 600L, 500L, 50L), WEDNESDAY));
    }

    @ParameterizedTest
    @CsvSource({"2026-10-13, 1005, 101", "2026-10-13, 1004, 100", "2026-10-14, 1005, 0"})
    void d02(LocalDate day, long total, long expected) {
        assertEquals(expected, new HappyTuesday().discount(List.of(total), day));
    }

    @ParameterizedTest
    @CsvSource({"4999, 0", "5000, 700", "8000, 700"})
    void d03(long total, long expected) {
        assertEquals(expected, new Threshold(5000, 700).discount(List.of(total - 1000, 1000L), WEDNESDAY));
    }

    // Toutes les promotions, chacune sur les prix d'origine : 6 000 - 1 000 (3 pour 2) - 600 (mardi) - 700 (seuil).
    @Test
    void d04() {
        Checkout checkout = new Checkout(List.of(new ThreeForTwo(), new HappyTuesday(), new Threshold(5000, 700)), at(TUESDAY));
        assertEquals(3700, checkout.total(List.of(3000L, 2000L, 1000L)));
        Checkout wednesday = new Checkout(List.of(new ThreeForTwo(), new HappyTuesday(), new Threshold(5000, 700)), at(WEDNESDAY));
        assertEquals(4300, wednesday.total(List.of(3000L, 2000L, 1000L)));
    }

    @Test
    void d05() {
        assertEquals(0, new Checkout(List.of(new Threshold(100, 5000)), at(TUESDAY)).total(List.of(300L)));
        assertEquals(300, new Checkout(List.of(), at(TUESDAY)).total(List.of(300L)));
    }

    // Une promotion ecrite en lambda, inconnue du code : la caisse est ouverte a l'extension.
    @Test
    void d06() {
        Promotion firstItemHalfPrice = (prices, today) -> prices.isEmpty() ? 0 : prices.get(0) / 2;
        assertEquals(1500, new Checkout(List.of(firstItemHalfPrice), at(WEDNESDAY)).total(List.of(1000L, 1000L)));
    }
}
