package ch17_algorithms.projects.p04_hashing.solution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Un cache LRU (Least Recently Used) : plein, il oublie l'entree utilisee il y a le plus longtemps.
 * get et put en O(1) : une HashMap trouve le maillon, une liste DOUBLEMENT chainee le deplace ou le retire en O(1).
 */
public final class LruCache<K, V> {

    private final class Node {
        final K key;
        V value;
        Node prev;
        Node next;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final int capacity;
    private final Map<K, Node> index = new HashMap<>();
    // Pourquoi deux sentinelles : plus de cas particulier pour le premier ou le dernier maillon.
    private final Node oldest = new Node(null, null);
    private final Node newest = new Node(null, null);

    public LruCache(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("capacite invalide : " + capacity);
        }
        this.capacity = capacity;
        oldest.next = newest;
        newest.prev = oldest;
    }

    // Piege : un get est une UTILISATION ; l'entree redevient la plus recente.
    public V get(K key) {
        Node n = index.get(key);
        if (n == null) {
            return null;
        }
        unlink(n);
        appendNewest(n);
        return n.value;
    }

    public void put(K key, V value) {
        Node n = index.get(key);
        if (n != null) {
            n.value = value;
            unlink(n);
            appendNewest(n);
            return;
        }
        if (index.size() == capacity) {
            Node victim = oldest.next;
            unlink(victim);
            index.remove(victim.key);
        }
        n = new Node(key, value);
        index.put(key, n);
        appendNewest(n);
    }

    public int size() {
        return index.size();
    }

    public List<K> keysFromOldest() {
        List<K> keys = new ArrayList<>();
        for (Node n = oldest.next; n != newest; n = n.next) {
            keys.add(n.key);
        }
        return keys;
    }

    private void unlink(Node n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
    }

    private void appendNewest(Node n) {
        n.prev = newest.prev;
        n.next = newest;
        newest.prev.next = n;
        newest.prev = n;
    }
}
