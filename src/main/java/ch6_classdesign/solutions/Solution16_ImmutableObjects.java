package ch6_classdesign.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise16_ImmutableObjects.
 */
public class Solution16_ImmutableObjects {

    static final class ImmutablePoint {
        private final int x;
        private final int y;
        private final List<String> tags;

        private ImmutablePoint(int x, int y, List<String> tags) {
            // Copie a l'ENTREE : modifier la liste de l'appelant ne change plus ce point.
            this.x = x;
            this.y = y;
            this.tags = new ArrayList<>(tags);
        }

        static ImmutablePoint of(int x, int y, List<String> tags) {
            // Fabrique static : le constructeur private reste la seule porte.
            return new ImmutablePoint(x, y, tags);
        }

        int x() {
            return x;
        }

        int y() {
            return y;
        }

        List<String> tags() {
            // Copie a la SORTIE : l'appelant recoit une autre liste.
            return new ArrayList<>(tags);
        }
    }
}
