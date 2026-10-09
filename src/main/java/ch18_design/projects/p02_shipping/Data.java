package ch18_design.projects.p02_shipping;

import java.util.Set;

/**
 * FOURNI (ne pas modifier) : le calcul des frais de port de la boutique de the.
 * Chaque nouveau transporteur a ajoute un "case" au switch, et chaque nouvelle regle un "if" a la fin.
 * Sa sortie est la specification : ton code devra donner exactement les memes prix et les memes refus.
 */
public final class Data {

    private Data() {
    }

    public static void main(String[] args) {
        System.out.println("POST 1,2 kg FR : " + LegacyShipping.price("POST", 1200, "FR", false));
        System.out.println("EXPRESS 1,2 kg DE fragile : " + LegacyShipping.price("EXPRESS", 1200, "DE", true));
        System.out.println("PICKUP 3 kg BE : " + LegacyShipping.price("PICKUP", 3000, "BE", false));
        System.out.println("POST 800 g CH : " + LegacyShipping.price("POST", 800, "CH", false));
        System.out.println("le moins cher, 2 kg FR : " + LegacyShipping.cheapest(2000, "FR", false));
        System.out.println("le moins cher, 25 kg US : " + LegacyShipping.cheapest(25_000, "US", false));
        System.out.println("le moins cher, 31 kg FR : " + LegacyShipping.cheapest(31_000, "FR", false));
    }

    /** Le code d'origine. */
    public static final class LegacyShipping {

        private static final Set<String> EU = Set.of("FR", "BE", "DE", "ES", "IT", "LU", "NL");

        private LegacyShipping() {
        }

        /** Le prix en centimes, ou une IllegalArgumentException. */
        public static long price(String carrier, int grams, String country, boolean fragile) {
            if (grams < 1) {
                throw new IllegalArgumentException("poids invalide : " + grams);
            }
            if (country == null || !country.matches("[A-Z]{2}")) {
                throw new IllegalArgumentException("pays invalide : " + country);
            }
            long p;
            switch (carrier) {
                case "POST":
                    if (grams > 30_000) {
                        throw new IllegalArgumentException("colis refuse par POST");
                    }
                    if (grams <= 500) {
                        p = 495;
                    } else if (grams <= 2000) {
                        p = 750;
                    } else if (grams <= 5000) {
                        p = 1150;
                    } else {
                        p = 1150 + (grams - 5000 + 999) / 1000 * 120;
                    }
                    if (!country.equals("FR")) {
                        p = p * 2;
                    }
                    break;
                case "EXPRESS":
                    if (grams > 30_000) {
                        throw new IllegalArgumentException("colis refuse par EXPRESS");
                    }
                    p = 1290 + (grams + 999) / 1000 * 250;
                    if (!country.equals("FR")) {
                        p = p + 1500;
                    }
                    break;
                case "PICKUP":
                    if (!(country.equals("FR") || country.equals("BE")) || grams > 20_000) {
                        throw new IllegalArgumentException("colis refuse par PICKUP");
                    }
                    p = 390;
                    break;
                default:
                    throw new IllegalArgumentException("transporteur inconnu : " + carrier);
            }
            long extra = 0;
            if (fragile) {
                extra = extra + (p * 15 + 50) / 100;
            }
            if (!EU.contains(country)) {
                extra = extra + 800;
            }
            return p + extra;
        }

        /** "TRANSPORTEUR:prix" pour le moins cher, ou "AUCUN". Les exceptions servent a sauter un transporteur. */
        public static String cheapest(int grams, String country, boolean fragile) {
            String best = "AUCUN";
            long bestPrice = Long.MAX_VALUE;
            for (String c : new String[]{"POST", "EXPRESS", "PICKUP"}) {
                try {
                    long p = price(c, grams, country, fragile);
                    if (p < bestPrice) {
                        bestPrice = p;
                        best = c + ":" + p;
                    }
                } catch (IllegalArgumentException e) {
                    // transporteur refuse : on passe au suivant
                }
            }
            return best;
        }
    }
}
