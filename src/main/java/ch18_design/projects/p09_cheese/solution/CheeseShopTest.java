package ch18_design.projects.p09_cheese.solution;

import ch18_design.projects.p09_cheese.Data;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Le filet du legacy (maitre etalon sur plusieurs jours), les cas limites a la main, puis la nouveaute. */
class CheeseShopTest {

    private static final String[] NAMES = {"Tomme", "Brie", "Comte", "Billet degustation", "Sel de Guerande", "Chevre"};

    // Fait vieillir les deux versions du meme stock, et compare jour apres jour.
    private static void sameAsLegacy(List<Cheese> stock, int days) {
        List<Data.LegacyItem> legacy = new ArrayList<>(stock.stream()
                .map(c -> new Data.LegacyItem(c.name(), c.sellIn(), c.quality())).toList());
        List<Cheese> current = stock;
        for (int day = 1; day <= days; day++) {
            Data.LegacyCheeseShop.updateDay(legacy);
            current = CheeseShop.nextDay(current);
            assertEquals(legacy.stream().map(Data.LegacyItem::toString).toList(),
                    current.stream().map(c -> c.name() + " " + c.sellIn() + " " + c.quality()).toList(), "jour " + day);
        }
    }

    @RepeatedTest(300)
    void goldenMaster(RepetitionInfo info) {
        SplittableRandom random = new SplittableRandom(info.getCurrentRepetition());
        List<Cheese> stock = new ArrayList<>();
        int count = 1 + random.nextInt(6);
        for (int i = 0; i < count; i++) {
            String name = NAMES[random.nextInt(NAMES.length)];
            int quality = name.equals("Sel de Guerande") ? 80 : random.nextInt(51);
            stock.add(new Cheese(name, random.nextInt(-3, 16), quality));
        }
        sameAsLegacy(stock, 1 + random.nextInt(8));
    }

    // Les bords que le hasard touche rarement : les seuils du billet, 0, 50, et un article deja au-dessus de 50.
    @ParameterizedTest
    @CsvSource({"Billet degustation, 11, 30", "Billet degustation, 10, 30", "Billet degustation, 6, 30",
            "Billet degustation, 5, 30", "Billet degustation, 1, 30", "Billet degustation, 0, 30", "Billet degustation, 3, 49",
            "Comte, 1, 49", "Comte, 0, 49", "Comte, -1, 55", "Billet degustation, 3, 55", "Brie, 0, 3", "Brie, 1, 1",
            "Tomme, 0, 1", "Tomme, -5, 60", "Sel de Guerande, -2, 80"})
    void edgesLikeLegacy(String name, int sellIn, int quality) {
        sameAsLegacy(List.of(new Cheese(name, sellIn, quality)), 3);
    }

    @Test
    void sampleAfterThreeDays() {
        List<Cheese> sample = Data.sample().stream().map(i -> new Cheese(i.name, i.sellIn, i.quality)).toList();
        assertEquals(List.of(new Cheese("Tomme", -1, 6), new Cheese("Brie", -2, 0), new Cheese("Comte", -2, 50),
                new Cheese("Billet degustation", 8, 35), new Cheese("Billet degustation", -2, 0),
                new Cheese("Sel de Guerande", 0, 80)), CheeseShop.afterDays(sample, 3));
        assertEquals(sample, CheeseShop.afterDays(sample, 0));
    }

    @Test
    void negativeQualityIsRefused() {
        assertEquals("qualite negative : -1", assertThrows(IllegalArgumentException.class, () -> new Cheese("Tomme", 3, -1)).getMessage());
    }

    // La nouveaute (etape 7) : le legacy ne la connait pas, on la teste a la main.
    @ParameterizedTest
    @CsvSource({"Bio Brie, 5, 10, 6", "Bio Tomme, 5, 10, 8", "Bio Tomme, 0, 10, 6", "Bio Brie, 1, 3, 0",
            "Bio Comte, 5, 10, 11", "Bio Billet degustation, 3, 10, 13", "Bio Billet degustation, 0, 10, 0",
            "Bio Sel de Guerande, 4, 80, 80"})
    void organicProductsDecayTwiceAsFast(String name, int sellIn, int quality, int expected) {
        assertEquals(expected, CheeseShop.nextDay(List.of(new Cheese(name, sellIn, quality))).get(0).quality());
    }
}
