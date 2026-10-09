package ch17_algorithms.projects.p09_graphs.solution;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Union-find (ensembles disjoints) : chaque ensemble est un arbre dont la racine est le "representant".
 * Deux astuces le rendent presque O(1) : la compression des chemins, et l'union par taille.
 */
public final class UnionFind {

    private final int[] parent;
    private final int[] size;
    private int count;

    public UnionFind(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        count = n;
    }

    // Compression : chaque sommet du chemin est rattache directement a la racine.
    public int find(int x) {
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    // Rend false si a et b etaient deja ensemble. Le petit arbre passe sous le grand : les arbres restent bas.
    public boolean union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return false;
        }
        if (size[ra] < size[rb]) {
            int t = ra;
            ra = rb;
            rb = t;
        }
        parent[rb] = ra;
        size[ra] += size[rb];
        count--;
        return true;
    }

    public boolean connected(int a, int b) {
        return find(a) == find(b);
    }

    public int count() {
        return count;
    }

    // Kruskal : les aretes de la moins chere a la plus chere ; on garde une arete si elle relie deux morceaux SEPARES.
    // edges[i] = {a, b, cout}. Le cout minimal pour relier tout le monde, ou une exception si c'est impossible.
    public static long minimumSpanningTreeCost(int n, int[][] edges) {
        int[][] sorted = edges.clone();
        Arrays.sort(sorted, Comparator.comparingInt(e -> e[2]));
        UnionFind uf = new UnionFind(n);
        long cost = 0;
        for (int[] e : sorted) {
            if (uf.union(e[0], e[1])) {
                cost += e[2];
            }
        }
        if (uf.count() > 1) {
            throw new IllegalStateException("graphe non connexe");
        }
        return cost;
    }
}
