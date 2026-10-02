package library.service.internal;

import java.util.Locale;

/**
 * SOLUTION - un utilitaire interne : seul library.app y a acces (export qualifie).
 */
public final class Normalizer {

    private Normalizer() {
    }

    // Minuscules, sans articles ("le", "la", "les", "l'") en tete : la cle de tri des titres.
    public static String sortKey(String title) {
        String t = title.toLowerCase(Locale.ROOT);
        for (String article : new String[] {"le ", "la ", "les ", "l'"}) {
            if (t.startsWith(article)) {
                return t.substring(article.length());
            }
        }
        return t;
    }
}
