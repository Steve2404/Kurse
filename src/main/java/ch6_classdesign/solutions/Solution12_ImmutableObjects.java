package ch6_classdesign.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans classdesign.exercises.Exercise12_ImmutableObjects.
 */
public class Solution12_ImmutableObjects {

    static final class ImmutablePoint {
        private final int x;
        private final int y;
        private final List<String> tags;

        private ImmutablePoint(int x, int y, List<String> tags) {
            this.x = x;
            this.y = y;
            this.tags = new ArrayList<>(tags);
        }

        static ImmutablePoint of(int x, int y, List<String> tags) {
            return new ImmutablePoint(x, y, tags);
        }

        int x() {
            return x;
        }

        int y() {
            return y;
        }

        List<String> tags() {
            return new ArrayList<>(tags);
        }
    }
}
