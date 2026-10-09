package ch19_final.drills.r04_concurrency.solution;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/** Chaque cle est calculee UNE fois, meme demandee par 16 fils en meme temps (computeIfAbsent). */
public final class OnceCache<K, V> {

    private final Map<K, V> values = new ConcurrentHashMap<>();
    private final AtomicInteger computations = new AtomicInteger();

    public V get(K key, Function<K, V> compute) {
        return values.computeIfAbsent(key, k -> {
            computations.incrementAndGet();
            return compute.apply(k);
        });
    }

    public int computations() {
        return computations.get();
    }
}
