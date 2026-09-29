package ch10_streams.solutions;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise16_ReduceAndCollectBasics.
 */
public class Solution16_ReduceAndCollectBasics {

    public static int sumWithReduce(List<Integer> values) {
        // 0 est l'element neutre de l'addition : c'est aussi le resultat pour une liste vide.
        return values.stream().reduce(0, Integer::sum);
    }

    public static String joinWithCollect(List<String> words) {
        // collect(Collector) : joining colle les elements avec le separateur ENTRE eux.
        return words.stream().collect(Collectors.joining(", "));
    }

    public static Set<Integer> toSetCollect(List<Integer> values) {
        // toSet retire les doublons ; aucun ordre n'est garanti.
        return values.stream().collect(Collectors.toSet());
    }
}
