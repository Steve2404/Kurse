package ch9_collections.projects.p04_generics.solution;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * SOLUTION - un cache LRU generique : on ETEND LinkedHashMap&lt;K, V&gt; en transmettant ses parametres de type.
 * accessOrder = true : chaque get/put deplace la cle en fin d'ordre ; la plus ancienne est en tete.
 */
public class Cache<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = 1L;
    private final int capacity;
    private int hits;
    private int misses;

    public Cache(int capacity) {
        super(16, 0.75f, true);
        this.capacity = capacity;
    }

    // Appelee par LinkedHashMap apres chaque put : true -> l'entree la moins recemment utilisee est retiree.
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }

    // Une methode qui recoit une fonction generique : java.util.function.Function<? super K, ? extends V>.
    public V load(K key, java.util.function.Function<? super K, ? extends V> loader) {
        if (containsKey(key)) {
            hits++;
            return get(key);
        }
        misses++;
        V v = loader.apply(key);
        put(key, v);
        return v;
    }

    public String stats() {
        return hits + " succes, " + misses + " echecs";
    }
}
