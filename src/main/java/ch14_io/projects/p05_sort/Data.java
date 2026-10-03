package ch14_io.projects.p05_sort;

/**
 * Les donnees du projet 5 (DONNEES, ne pas modifier).
 */
public final class Data {

    public static final String SANDBOX = "build/ch14/p05_sort";

    /** Le gros fichier : LINES lignes "joueur;score", triees ensuite par paquets de CHUNK lignes. */
    public static final int LINES = 60_000;
    public static final int CHUNK = 7_000;

    /** La ligne i du fichier d'origine. */
    public static String line(int i) {
        long x = i * 6_364_136_223_846_793_005L + 1_442_695_040_888_963_407L;
        return "joueur" + (x >>> 40) % 100_000 + ";" + (x >>> 20) % 10_000;
    }

    private Data() {
    }
}
