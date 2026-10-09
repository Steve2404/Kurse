package ch18_design.projects.p09_cheese;

import java.util.List;

/**
 * FOURNI (ne pas modifier) : le programme de la cave a fromages. Chaque nuit, updateDay fait vieillir le stock.
 * Il marche depuis des annees, mais chaque nouveau produit a ajoute des "if" dans les "if".
 * Sa sortie EST la specification. Lance main pour voir trois jours d'un stock d'exemple.
 */
public final class Data {

    private Data() {
    }

    /** Un article du stock : le nom, les jours avant la date limite, la qualite (0 a 50). */
    public static final class LegacyItem {
        public final String name;
        public int sellIn;
        public int quality;

        public LegacyItem(String name, int sellIn, int quality) {
            this.name = name;
            this.sellIn = sellIn;
            this.quality = quality;
        }

        @Override
        public String toString() {
            return name + " " + sellIn + " " + quality;
        }
    }

    public static List<LegacyItem> sample() {
        return List.of(new LegacyItem("Tomme", 2, 10), new LegacyItem("Brie", 1, 9), new LegacyItem("Comte", 1, 47),
                new LegacyItem("Billet degustation", 11, 30), new LegacyItem("Billet degustation", 1, 40),
                new LegacyItem("Sel de Guerande", 0, 80));
    }

    public static void main(String[] args) {
        List<LegacyItem> stock = sample();
        for (int day = 1; day <= 3; day++) {
            LegacyCheeseShop.updateDay(stock);
            System.out.println("jour " + day + " : " + stock);
        }
    }

    public static final class LegacyCheeseShop {

        private LegacyCheeseShop() {
        }

        public static void updateDay(List<LegacyItem> items) {
            for (LegacyItem i : items) {
                if (!i.name.equals("Sel de Guerande")) {
                    i.sellIn = i.sellIn - 1;
                }
                if (i.name.equals("Comte")) {
                    if (i.quality < 50) {
                        i.quality = i.quality + 1;
                    }
                    if (i.sellIn < 0 && i.quality < 50) {
                        i.quality = i.quality + 1;
                    }
                } else if (i.name.equals("Billet degustation")) {
                    if (i.sellIn < 0) {
                        i.quality = 0;
                    } else {
                        if (i.quality < 50) {
                            i.quality = i.quality + 1;
                        }
                        if (i.sellIn < 10 && i.quality < 50) {
                            i.quality = i.quality + 1;
                        }
                        if (i.sellIn < 5 && i.quality < 50) {
                            i.quality = i.quality + 1;
                        }
                    }
                } else if (!i.name.equals("Sel de Guerande")) {
                    int loss = i.name.equals("Brie") ? 2 : 1;
                    if (i.sellIn < 0) {
                        loss = loss * 2;
                    }
                    i.quality = i.quality - loss;
                    if (i.quality < 0) {
                        i.quality = 0;
                    }
                }
            }
        }
    }
}
