package ch18_design.projects.p02_shipping.solution;

import ch18_design.projects.p02_shipping.Data;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Optional;
import java.util.SplittableRandom;
import java.util.function.LongSupplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Le filet du legacy (maitre etalon), puis la preuve que le calculateur est ouvert a l'extension. */
class ShippingTest {

    private static final String[] CARRIERS = {"POST", "EXPRESS", "PICKUP", "TRUCK"};
    private static final String[] COUNTRIES = {"FR", "BE", "DE", "LU", "CH", "US", "fr", "FRA"};

    private final ShippingCalculator shop = Shop.standard();

    // Un prix OU un refus, sous forme de texte : on compare les deux programmes meme quand ils refusent.
    private static String outcome(LongSupplier price) {
        try {
            return String.valueOf(price.getAsLong());
        } catch (IllegalArgumentException e) {
            return "refus : " + e.getMessage();
        }
    }

    @RepeatedTest(400)
    void goldenMasterPrice(RepetitionInfo info) {
        SplittableRandom random = new SplittableRandom(info.getCurrentRepetition());
        String carrier = CARRIERS[random.nextInt(CARRIERS.length)];
        int grams = random.nextInt(32_001);
        String country = COUNTRIES[random.nextInt(COUNTRIES.length)];
        boolean fragile = random.nextBoolean();
        assertEquals(outcome(() -> Data.LegacyShipping.price(carrier, grams, country, fragile)),
                outcome(() -> shop.price(carrier, new Parcel(grams, country, fragile))),
                () -> carrier + " " + grams + " g " + country + (fragile ? " fragile" : ""));
    }

    @RepeatedTest(200)
    void goldenMasterCheapest(RepetitionInfo info) {
        SplittableRandom random = new SplittableRandom(1000 + info.getCurrentRepetition());
        int grams = 1 + random.nextInt(30_000);
        String country = COUNTRIES[random.nextInt(6)];
        boolean fragile = random.nextBoolean();
        Optional<Quote> best = shop.cheapest(new Parcel(grams, country, fragile));
        assertEquals(Data.LegacyShipping.cheapest(grams, country, fragile),
                best.map(q -> q.carrier() + ":" + q.priceCents()).orElse("AUCUN"));
    }

    // Les paliers et les limites, que le hasard touche rarement pile.
    @ParameterizedTest
    @CsvSource({"POST, 500", "POST, 501", "POST, 2000", "POST, 2001", "POST, 5000", "POST, 5001", "POST, 6000",
            "POST, 6001", "POST, 30000", "POST, 30001", "EXPRESS, 1000", "EXPRESS, 1001", "EXPRESS, 30000",
            "EXPRESS, 30001", "PICKUP, 20000", "PICKUP, 20001", "POST, 1", "POST, 0"})
    void limitsLikeLegacy(String carrier, int grams) {
        for (String country : List.of("FR", "BE", "LU", "NL", "ES", "IT", "DE", "CH")) {
            for (boolean fragile : new boolean[]{false, true}) {
                assertEquals(outcome(() -> Data.LegacyShipping.price(carrier, grams, country, fragile)),
                        outcome(() -> shop.price(carrier, new Parcel(grams, country, fragile))),
                        () -> carrier + " " + grams + " " + country + " " + fragile);
            }
        }
    }

    @Test
    void quotesAreSortedByPriceThenCarrier() {
        assertEquals(List.of(new Quote("PICKUP", 390), new Quote("POST", 750), new Quote("EXPRESS", 1540)),
                shop.quotes(new Parcel(1000, "FR", false)));
    }

    @Test
    void sameStrategyCodeTwiceIsRefused() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> new ShippingCalculator(List.of(new PostRate(), new ExpressRate(), new PostRate()), List.of()));
        assertEquals("transporteur en double : POST", e.getMessage());
    }

    // L'OUVERTURE : un transporteur inconnu du code de production, branche sans le modifier.
    @Test
    void newCarrierWithoutTouchingTheCalculator() {
        ShippingRate bike = new ShippingRate() {
            @Override
            public String code() {
                return "BIKE";
            }

            @Override
            public boolean accepts(Parcel parcel) {
                return parcel.domestic() && parcel.grams() <= 3000;
            }

            @Override
            public long basePrice(Parcel parcel) {
                return 300;
            }
        };
        ShippingCalculator calculator = new ShippingCalculator(List.of(new PostRate(), bike), List.of(Surcharges.fragile()));
        assertEquals(List.of(new Quote("BIKE", 345), new Quote("POST", 569)), calculator.quotes(new Parcel(400, "FR", true)));
        assertEquals(List.of(new Quote("POST", 1150)), calculator.quotes(new Parcel(4000, "FR", false)));
    }

    // Une surcharge en lambda, et chaque surcharge calculee sur le prix de BASE.
    @Test
    void surchargesAreComputedOnTheBasePrice() {
        Surcharge flat = (parcel, base) -> 100;
        Surcharge half = (parcel, base) -> base / 2;
        ShippingCalculator calculator = new ShippingCalculator(List.of(new PickupRate()), List.of(flat, half));
        assertEquals(390 + 100 + 195, calculator.price("PICKUP", new Parcel(10, "FR", false)));
    }

    @Test
    void freight() {
        assertEquals(List.of(new Quote("FREIGHT", 4990)), shop.quotes(new Parcel(30_001, "FR", false)));
        assertEquals(Optional.of(new Quote("FREIGHT", 5800)), shop.cheapest(new Parcel(40_000, "FR", false)));
        assertEquals(Optional.of(new Quote("POST", 4150)), shop.cheapest(new Parcel(30_000, "FR", false)));
        assertEquals(Optional.empty(), shop.cheapest(new Parcel(30_001, "BE", false)));
        assertEquals(6670, shop.price("FREIGHT", new Parcel(40_000, "FR", true)));
    }
}
