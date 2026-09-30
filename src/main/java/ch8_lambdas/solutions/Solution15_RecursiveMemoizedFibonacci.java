package ch8_lambdas.solutions;

import java.util.Map;
import java.util.function.Function;

/**
 * Corrige de l'exercice 15. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise15_RecursiveMemoizedFibonacci.
 */
public class Solution15_RecursiveMemoizedFibonacci {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Function<Integer, Long> buildMemoizedFibonacci(Map<Integer, Long> cache) {
        // Une lambda ne peut pas se nommer elle-meme : on passe par un tableau d'une case (ou un champ) pour la recursion.
        Function<Integer, Long>[] fibHolder = new Function[1];
        fibHolder[0] = n -> {
            if (n <= 1) {
                return (long) n;
            }
            if (cache.containsKey(n)) {
                return cache.get(n);
            }
            long result = fibHolder[0].apply(n - 1) + fibHolder[0].apply(n - 2);
            cache.put(n, result);
            return result;
        };
        return fibHolder[0];
    }
}
