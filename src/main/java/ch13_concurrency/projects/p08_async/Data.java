package ch13_concurrency.projects.p08_async;

import java.util.Map;

/**
 * Les donnees du projet 8 (bonus, DONNEES, ne pas modifier).
 */
public final class Data {

    /** Les voyages a chiffrer ; "Atlantis" fait echouer le service des vols. */
    public static final String[] TRIPS = {"Rome", "Lisbonne", "Atlantis", "Oslo"};

    /** Prix en euros : vol aller-retour, et hotel par nuit. */
    public static final Map<String, Integer> FLIGHTS = Map.of("Rome", 180, "Lisbonne", 140, "Oslo", 260);
    public static final Map<String, Integer> HOTELS = Map.of("Rome", 95, "Lisbonne", 70, "Oslo", 150, "Atlantis", 999);
    public static final int NIGHTS = 3;

    /** Taux de change euro -> franc suisse, en pour mille. */
    public static final int CHF_PER_MILLE = 945;

    /** Le tableau des variations journalieres d'une action, pour le sous-tableau de somme maximale. */
    public static final int SIZE = 2_000_000;

    public static int delta(int i) {
        long x = i * 6_364_136_223_846_793_005L + 1_442_695_040_888_963_407L;
        return (int) ((x >>> 33) % 201) - 100;              // entre -100 et 100
    }

    private Data() {
    }
}
