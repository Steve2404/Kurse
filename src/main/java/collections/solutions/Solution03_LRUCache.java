package collections.solutions;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Corrige de l'exercice 3.
 */
public class Solution03_LRUCache {

    static class LRUCache<K, V> extends LinkedHashMap<K, V> {

        private final int capacity;

        LRUCache(int capacity) {
            super(16, 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }
}