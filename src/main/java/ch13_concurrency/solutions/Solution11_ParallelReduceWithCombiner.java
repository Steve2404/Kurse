package ch13_concurrency.solutions;

import java.util.List;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans concurrency.exercises.Exercise11_ParallelReduceWithCombiner.
 */
public class Solution11_ParallelReduceWithCombiner {

    public static int totalLength(List<String> words) {
        return words.parallelStream().reduce(0, (partial, word) -> partial + word.length(), Integer::sum);
    }
}
