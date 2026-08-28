package ch11_exceptions.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans exceptions.exercises.Exercise04_TryWithResourcesOrder.
 */
public class Solution04_TryWithResourcesOrder {

    public static class TrackedResource implements AutoCloseable {
        private final String name;
        private final List<String> trace;

        public TrackedResource(String name, List<String> trace) {
            this.name = name;
            this.trace = trace;
        }

        @Override
        public void close() {
            trace.add("close:" + name);
        }
    }

    public static void useResourcesInOrder(List<String> trace) {
        try (TrackedResource a = new TrackedResource("A", trace);
             TrackedResource b = new TrackedResource("B", trace);
             TrackedResource c = new TrackedResource("C", trace)) {
            trace.add("use");
        }
    }
}
