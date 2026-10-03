package ch14_io.projects.p03_rle;

/**
 * Les donnees du projet 3 (DONNEES, ne pas modifier).
 */
public final class Data {

    public static final String SANDBOX = "build/ch14/p03_rle";

    /** Le fichier binaire a compresser : RUNS series ; la serie i repete l'octet value(i), length(i) fois. */
    public static final int RUNS = 400;

    public static int value(int i) {
        return i * 37 % 7 * 30;
    }

    public static int length(int i) {
        return i * 13 % 300 + 1;
    }

    /** Les lignes du rapport texte. */
    public static final String[] ITEMS = {"stylo;3;1.20", "cahier;5;2.50", "gomme;10;0.80"};

    public static final String ACCENTS = "été à Noël";

    private Data() {
    }
}
