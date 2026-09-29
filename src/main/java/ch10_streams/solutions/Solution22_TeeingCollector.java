package ch10_streams.solutions;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 22. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise22_TeeingCollector.
 */
public class Solution22_TeeingCollector {

    public static String minMaxSummary(List<Integer> values) {
        // teeing envoie chaque element aux DEUX collecteurs en un seul parcours,
        // puis la fusion combine leurs resultats.
        Collector<Integer, ?, Optional<Integer>> minCollector = Collectors.minBy(Comparator.naturalOrder());
        Collector<Integer, ?, Optional<Integer>> maxCollector = Collectors.maxBy(Comparator.naturalOrder());
        return values.stream().collect(Collectors.teeing(minCollector, maxCollector,
                (min, max) -> "min=" + min.get() + ", max=" + max.get()));
    }
}
