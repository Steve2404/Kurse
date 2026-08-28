package ch10_streams.solutions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise11_PartitioningBy.
 */
public class Solution11_PartitioningBy {

    public static Map<Boolean, List<Integer>> partitionEvenOdd(List<Integer> values) {
        return values.stream().collect(Collectors.partitioningBy(n -> n % 2 == 0));
    }
}
