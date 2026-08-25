package concurrency.solutions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans concurrency.exercises.Exercise10_ParallelStreamOrdering.
 */
public class Solution10_ParallelStreamOrdering {

    public static long sumWithParallelStream(List<Integer> values) {
        return values.parallelStream().mapToLong(Integer::longValue).sum();
    }

    public static List<Integer> collectOrderedWithParallel(List<Integer> values) {
        return values.parallelStream().map(v -> v * 2).collect(Collectors.toList());
    }

    public static void traceUnorderedForEach(List<Integer> values, List<Integer> trace) {
        values.parallelStream().forEach(trace::add);
    }
}
