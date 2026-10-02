package blog.app;

import com.acme.text.Slugify;
import com.acme.utils.Strings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SOLUTION du projet 4 - un module nomme qui s'appuie sur deux modules automatiques.
 */
public class Main {

    static final String[] TITLES = {"L'Ete a Paris !", "Java 17 : les modules", "L'ete a Paris", "Ete a Paris", "Les modules, enfin..."};

    // Slugs UNIQUES : a la 2e occurrence on ajoute -2, a la 3e -3...
    static List<String> uniqueSlugs(String[] titles) {
        Map<String, Integer> seen = new HashMap<>();
        List<String> out = new ArrayList<>();
        for (String t : titles) {
            String slug = Slugify.slug(t);
            int n = seen.merge(slug, 1, Integer::sum);
            out.add(n == 1 ? slug : slug + "-" + n);
        }
        return out;
    }

    public static void main(String[] args) {
        System.out.println("slugs : " + uniqueSlugs(TITLES));
        System.out.println("courts : " + Strings.shorten("Les modules de Java 17", 12) + " | " + Strings.shorten("JPMS", 12));
        for (Class<?> c : List.of(Main.class, Slugify.class, Strings.class)) {
            Module m = c.getModule();
            System.out.println(c.getSimpleName() + " -> module " + m.getName() + ", automatique " + m.getDescriptor().isAutomatic()
                    + ", lit le module sans nom " + m.canRead(ClassLoader.getSystemClassLoader().getUnnamedModule()));
        }
    }
}
