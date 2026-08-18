package streams.solutions;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 12. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise12_TeeingCollector.
 */
public class Solution12_TeeingCollector {

    public static String minMaxSummary(List<Integer> values) {
        Collector<Integer, ?, Optional<Integer>> minCollector = Collectors.minBy(Comparator.naturalOrder());
        Collector<Integer, ?, Optional<Integer>> maxCollector = Collectors.maxBy(Comparator.naturalOrder());
        return values.stream().collect(Collectors.teeing(minCollector, maxCollector,
                (min, max) -> "min=" + min.get() + ", max=" + max.get()));
    }
}
