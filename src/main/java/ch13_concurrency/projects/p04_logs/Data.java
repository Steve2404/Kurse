package ch13_concurrency.projects.p04_logs;

/**
 * Les donnees du projet 4 (DONNEES, ne pas modifier) : un journal d'acces genere de facon deterministe.
 */
public final class Data {

    public static final int LINES = 6_000;
    public static final int PRODUCERS = 2;
    public static final int CONSUMERS = 3;
    public static final int CAPACITY = 64;
    public static final String POISON = "FIN";

    private static final String[] USERS = {"ana", "bob", "chloe", "dan", "eve", "fred", "gina"};
    private static final String[] PAGES = {"/", "/panier", "/produit", "/recherche", "/compte"};

    /** La ligne i : "user=<u> page=<p> ms=<duree> status=<code>". */
    public static String line(int i) {
        long x = i * 2_862_933_555_777_941_757L + 3_037_000_493L;
        x ^= x >>> 29;
        int status = Math.floorMod(x, 400) == 0 ? 500 : Math.floorMod(x, 9) == 0 ? 404 : 200;
        return "user=" + USERS[(int) Math.floorMod(x >>> 7, (long) USERS.length)] + " page=" + PAGES[(int) Math.floorMod(x >>> 13, (long) PAGES.length)]
                + " ms=" + (20 + Math.floorMod(x >>> 19, 990L)) + " status=" + status;
    }

    private Data() {
    }
}
