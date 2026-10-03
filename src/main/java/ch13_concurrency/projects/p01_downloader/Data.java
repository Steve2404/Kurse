package ch13_concurrency.projects.p01_downloader;

/**
 * Les donnees du projet 1 (DONNEES, ne pas modifier) : un "fichier" de SIZE octets, genere de facon deterministe.
 */
public final class Data {

    public static final int SIZE = 1_000_000;
    public static final int CHUNKS = 4;

    private static final byte[] BYTES = new byte[SIZE];

    static {
        long x = 42;
        for (int i = 0; i < SIZE; i++) {
            x = (x * 6364136223846793005L + 1442695040888963407L);
            // Des valeurs 0..3, qui se repetent souvent : de longues series d'octets identiques.
            BYTES[i] = (byte) ((x >>> 61) % 4 == 0 ? (i > 0 ? BYTES[i - 1] : 0) : (x >>> 62));
        }
    }

    public static byte at(int i) {
        return BYTES[i];
    }

    private Data() {
    }
}
