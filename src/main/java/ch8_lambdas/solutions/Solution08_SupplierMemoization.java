package ch8_lambdas.solutions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise08_SupplierMemoization.
 */
public class Solution08_SupplierMemoization {

    static final class Lazy<T> {
        private final Supplier<T> supplier;
        private T cachedValue;
        private boolean computed;

        Lazy(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        T get() {
            // Le Supplier n'est appele qu'au premier get() ; ensuite la valeur gardee est rendue.
            if (!computed) {
                cachedValue = supplier.get();
                computed = true;
            }
            return cachedValue;
        }
    }

    public static <T, R> Function<T, R> memoize(Function<T, R> function) {
        // La Map capturee se souvient des resultats deja calcules (computeIfAbsent).
        Map<T, R> cache = new HashMap<>();
        return input -> cache.computeIfAbsent(input, function);
    }
}
