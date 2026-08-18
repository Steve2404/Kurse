package lambdas.solutions;

import java.util.Map;
import java.util.function.Function;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans lambdas.exercises.Exercise10_RecursiveMemoizedFibonacci.
 */
public class Solution10_RecursiveMemoizedFibonacci {

    @SuppressWarnings("unchecked")
    public static Function<Integer, Long> buildMemoizedFibonacci(Map<Integer, Long> cache) {
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
