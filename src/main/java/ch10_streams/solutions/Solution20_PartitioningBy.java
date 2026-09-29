package ch10_streams.solutions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 20. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise20_PartitioningBy.
 */
public class Solution20_PartitioningBy {

    public static Map<Boolean, List<Integer>> partitionEvenOdd(List<Integer> values) {
        // partitioningBy : exactement 2 cles (false et true), toujours presentes.
        return values.stream().collect(Collectors.partitioningBy(n -> n % 2 == 0));
    }
}
