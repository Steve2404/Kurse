package ch9_collections.solutions;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Corrige de l'exercice 9.
 */
public class Solution09_LRUCache {

    static class LRUCache<K, V> extends LinkedHashMap<K, V> {

        private static final long serialVersionUID = 1L; // LinkedHashMap est Serializable : -Xlint:serial l'exige

        private final int capacity;

        LRUCache(int capacity) {
            // accessOrder = true : get() deplace l'entree en fin, donc la plus ancienne (en tete) est la moins recemment utilisee.
            super(16, 0.75f, true);
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            // Appele par put() apres chaque ajout : rendre true supprime l'entree la plus ancienne.
            return size() > capacity;
        }
    }
}