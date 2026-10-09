package ch17_algorithms.projects.p04_hashing.solution;

import java.util.Objects;

/**
 * Une table de hachage a chainage, ecrite a la main : c'est ainsi que fonctionne HashMap.
 * Un tableau de "seaux" (buckets) ; chaque seau est une petite liste chainee des entrees dont le hash tombe la.
 */
public final class SimpleHashMap<K, V> {

    private static final class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    private Node<K, V>[] buckets;
    private int size;

    public SimpleHashMap() {
        buckets = newBuckets(8);
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Node<K, V>[] newBuckets(int n) {
        return (Node<K, V>[]) new Node[n];
    }

    // Pourquoi floorMod : hashCode() peut etre NEGATIF, et -7 % 8 vaut -7 (un indice impossible).
    private int indexOf(Object key, int length) {
        return Math.floorMod(key.hashCode(), length);
    }

    // Rend l'ancienne valeur, ou null (comme Map.put).
    public V put(K key, V value) {
        Objects.requireNonNull(key, "cle absente");
        int i = indexOf(key, buckets.length);
        for (Node<K, V> n = buckets[i]; n != null; n = n.next) {
            // Piege : equals, pas == ; deux String egales peuvent etre deux objets differents.
            if (n.key.equals(key)) {
                V old = n.value;
                n.value = value;
                return old;
            }
        }
        buckets[i] = new Node<>(key, value, buckets[i]);
        size++;
        // Pourquoi agrandir : avec trop d'entrees par seau, chercher redevient lineaire. Facteur de charge 0,75.
        if (size > buckets.length * 3 / 4) {
            resize();
        }
        return null;
    }

    public V get(Object key) {
        Objects.requireNonNull(key, "cle absente");
        for (Node<K, V> n = buckets[indexOf(key, buckets.length)]; n != null; n = n.next) {
            if (n.key.equals(key)) {
                return n.value;
            }
        }
        return null;
    }

    public V remove(Object key) {
        Objects.requireNonNull(key, "cle absente");
        int i = indexOf(key, buckets.length);
        Node<K, V> previous = null;
        for (Node<K, V> n = buckets[i]; n != null; previous = n, n = n.next) {
            if (n.key.equals(key)) {
                if (previous == null) {
                    buckets[i] = n.next;
                } else {
                    previous.next = n.next;
                }
                size--;
                return n.value;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return buckets.length;
    }

    // Chaque entree change de seau : son indice depend de la taille du tableau.
    private void resize() {
        Node<K, V>[] old = buckets;
        buckets = newBuckets(old.length * 2);
        for (Node<K, V> head : old) {
            for (Node<K, V> n = head; n != null; n = n.next) {
                int i = indexOf(n.key, buckets.length);
                buckets[i] = new Node<>(n.key, n.value, buckets[i]);
            }
        }
    }
}
