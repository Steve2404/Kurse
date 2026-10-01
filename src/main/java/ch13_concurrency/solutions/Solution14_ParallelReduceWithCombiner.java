package ch13_concurrency.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 14. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.exercises.Exercise14_ParallelReduceWithCombiner.
 */
public class Solution14_ParallelReduceWithCombiner {

    public static int totalLength(List<String> words) {
        // Types differents (String -> int) : l'accumulateur ajoute un mot, le combinateur additionne deux sommes partielles.
        return words.parallelStream().reduce(0, (partial, word) -> partial + word.length(), Integer::sum);
    }
}
