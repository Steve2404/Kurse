package ch9_collections.projects.p07_social.solution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * SOLUTION - Union-Find GENERIQUE (ensembles disjoints) : chaque element pointe vers un parent ; la racine represente le groupe.
 * T extends Comparable : pour rendre les groupes tries.
 */
public class UnionFind<T extends Comparable<T>> {

    private final Map<T, T> parent = new HashMap<>();
    private int unions;

    public void add(T x) {
        parent.putIfAbsent(x, x);
    }

    // Avec compression de chemin : chaque noeud visite pointe ensuite directement vers la racine.
    public T find(T x) {
        T p = parent.get(x);
        if (!p.equals(x)) {
            p = find(p);
            parent.put(x, p);
        }
        return p;
    }

    public boolean union(T a, T b) {
        T ra = find(a);
        T rb = find(b);
        if (ra.equals(rb)) {
            return false;
        }
        parent.put(ra, rb);
        unions++;
        return true;
    }

    public int unions() {
        return unions;
    }

    public Collection<Set<T>> groups() {
        Map<T, Set<T>> byRoot = new HashMap<>();
        for (T x : parent.keySet()) {
            byRoot.computeIfAbsent(find(x), k -> new TreeSet<>()).add(x);
        }
        // Trier les groupes par leur plus petit element, pour une sortie stable.
        Map<T, Set<T>> sorted = new TreeMap<>();
        for (Set<T> g : byRoot.values()) {
            sorted.put(((TreeSet<T>) g).first(), g);
        }
        List<Set<T>> out = new ArrayList<>(sorted.values());
        return out;
    }
}
