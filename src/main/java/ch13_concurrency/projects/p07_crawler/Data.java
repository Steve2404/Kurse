package ch13_concurrency.projects.p07_crawler;

import java.util.ArrayList;
import java.util.List;

/**
 * Les donnees du projet 7 (DONNEES, ne pas modifier) : un petit "web" de PAGES pages, genere de facon deterministe.
 */
public final class Data {

    public static final int PAGES = 80;
    public static final int MAX_DEPTH = 4;
    public static final int THREADS = 6;
    public static final String START = "p0";

    private static final String[] WORDS = {"java", "thread", "verrou", "file", "flux", "module", "carte", "tache", "pool", "atome"};

    /** Les liens de la page "pN". Les pages dont le numero est un multiple de 13 (sauf 0) sont CASSEES. */
    public static List<String> links(String page) {
        int n = Integer.parseInt(page.substring(1));
        if (n != 0 && n % 13 == 0) {
            throw new IllegalStateException("404 " + page);
        }
        List<String> out = new ArrayList<>();
        for (int k = 1; k <= 3; k++) {
            out.add("p" + (n * 3 + k * k + n % 5) % PAGES);
        }
        return out;
    }

    /** Les mots de la page "pN". */
    public static List<String> words(String page) {
        int n = Integer.parseInt(page.substring(1));
        return List.of(WORDS[n % WORDS.length], WORDS[(n * 7 + 3) % WORDS.length]);
    }

    private Data() {
    }
}
