package ch11_exceptions.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch11_exceptions.exercises.Exercise07_TryWithResourcesOrder.
 */
public class Solution07_TryWithResourcesOrder {

    public static class TrackedResource implements AutoCloseable {
        private final String name;
        private final List<String> trace;

        public TrackedResource(String name, List<String> trace) {
            this.name = name;
            this.trace = trace;
        }

        @Override
        public void close() {
            // Chaque fermeture laisse une trace : on voit l'ordre INVERSE de la declaration (C, B, A).
            trace.add("close:" + name);
        }
    }

    @SuppressWarnings("try") // a, b, c ne servent qu'a tracer l'ouverture et la fermeture : -Xlint:try le signalerait
    public static void useResourcesInOrder(List<String> trace) {
        // Ouverture dans l'ordre A, B, C ; fermeture automatique a l'envers, APRES le corps du bloc.
        try (TrackedResource a = new TrackedResource("A", trace);
             TrackedResource b = new TrackedResource("B", trace);
             TrackedResource c = new TrackedResource("C", trace)) {
            trace.add("use");
        }
    }
}
