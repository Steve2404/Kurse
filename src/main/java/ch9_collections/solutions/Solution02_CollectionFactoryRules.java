package ch9_collections.solutions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Corrige de l'exercice 2.
 */
public class Solution02_CollectionFactoryRules {

    static final List<String> FACTORIES = List.of("ArrayList", "Arrays.asList", "List.of", "List.copyOf", "unmodifiableList");

    public static String modifyBehavior(String factory, String operation) {
        // Arrays.asList est de taille fixe mais ecrivable : set et sort remplacent des cases, add/remove/clear changeraient la taille.
        switch (factory) {
            case "ArrayList":
                return "OK";
            case "Arrays.asList":
                return operation.equals("set") || operation.equals("sort") ? "OK" : "UnsupportedOperationException";
            default:
                return "UnsupportedOperationException";
        }
    }

    public static String nullBehavior(String factory, String operation) {
        // Piege : List.of / List.copyOf refusent null partout, meme dans contains(null) qui ne fait que chercher.
        return factory.equals("List.of") || factory.equals("List.copyOf") ? "NullPointerException" : "OK";
    }

    public static boolean seesLaterChanges(String factory) {
        // Vue (fenetre) contre copie (photo) : unmodifiableList interdit d'ecrire, mais montre toujours la source.
        return factory.equals("Arrays.asList") || factory.equals("unmodifiableList");
    }

    public static List<String> freeze(List<String> list) {
        // Copie privee (plus de lien avec l'original) + vitrine (lecture seule) ; contrairement a List.copyOf, null passe.
        return Collections.unmodifiableList(new ArrayList<>(list));
    }
}
