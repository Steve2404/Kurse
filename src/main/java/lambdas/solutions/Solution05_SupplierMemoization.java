package lambdas.solutions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans lambdas.exercises.Exercise05_SupplierMemoization.
 */
public class Solution05_SupplierMemoization {

    static final class Lazy<T> {
        private final Supplier<T> supplier;
        private T cachedValue;
        private boolean computed;

        Lazy(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        T get() {
            if (!computed) {
                cachedValue = supplier.get();
                computed = true;
            }
            return cachedValue;
        }
    }

    public static <T, R> Function<T, R> memoize(Function<T, R> function) {
        Map<T, R> cache = new HashMap<>();
        return input -> cache.computeIfAbsent(input, function);
    }
}
