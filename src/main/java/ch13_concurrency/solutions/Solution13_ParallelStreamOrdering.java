package ch13_concurrency.solutions;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise13_ParallelStreamOrdering.
 */
public class Solution13_ParallelStreamOrdering {

    public static long sumWithParallelStream(List<Integer> values) {
        // Une somme ne depend pas de l'ordre : le parallele donne toujours le meme resultat.
        return values.parallelStream().mapToLong(Integer::longValue).sum();
    }

    public static List<Integer> collectOrderedWithParallel(List<Integer> values) {
        // collect(toList()) recolle les morceaux dans l'ordre de rencontre, meme en parallele.
        return values.parallelStream().map(v -> v * 2).collect(Collectors.toList());
    }

    public static void traceUnorderedForEach(List<Integer> values, List<Integer> trace) {
        // forEach en parallele n'a AUCUN ordre garanti (la liste recue doit etre thread-safe) ; forEachOrdered le garderait.
        values.parallelStream().forEach(trace::add);
    }
}
