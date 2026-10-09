package ch19_final.drills.r05_resilience.solution;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/** Le repli : la source si elle repond (et on retient la valeur), sinon la derniere valeur connue, sinon l'erreur. */
public final class LastKnown<K, V> {

    private final Map<K, V> known = new ConcurrentHashMap<>();

    public V get(K key, Function<K, V> source) {
        try {
            V value = source.apply(key);
            known.put(key, value);
            return value;
        } catch (RuntimeException e) {
            V previous = known.get(key);
            if (previous == null) {
                throw e;
            }
            return previous;
        }
    }
}
