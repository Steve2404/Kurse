package ch10_streams.solutions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise02_PipelineLaziness.
 */
public class Solution02_PipelineLaziness {

    public static Stream<Integer> filterEvenThenTimesTen(List<Integer> values, int[] filterCalls, int[] mapCalls) {
        return values.stream()
                .filter(n -> {
                    filterCalls[0]++;
                    return n % 2 == 0;
                })
                .map(n -> {
                    mapCalls[0]++;
                    return n * 10;
                });
    }

    public static Optional<Integer> firstEvenTimesTen(List<Integer> values, int[] filterCalls) {
        return values.stream()
                .filter(n -> {
                    filterCalls[0]++;
                    return n % 2 == 0;
                })
                .map(n -> n * 10)
                .findFirst();
    }
}
