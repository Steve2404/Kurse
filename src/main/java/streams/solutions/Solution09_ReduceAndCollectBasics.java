package streams.solutions;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise09_ReduceAndCollectBasics.
 */
public class Solution09_ReduceAndCollectBasics {

    public static int sumWithReduce(List<Integer> values) {
        return values.stream().reduce(0, Integer::sum);
    }

    public static String joinWithCollect(List<String> words) {
        return words.stream().collect(Collectors.joining(", "));
    }

    public static Set<Integer> toSetCollect(List<Integer> values) {
        return values.stream().collect(Collectors.toSet());
    }
}
