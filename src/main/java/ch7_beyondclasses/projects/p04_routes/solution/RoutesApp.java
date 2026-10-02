package ch7_beyondclasses.projects.p04_routes.solution;

import ch7_beyondclasses.projects.p04_routes.Data;

/**
 * SOLUTION du projet 4 - la tournee de livraison.
 */
public class RoutesApp {

    private static int[] bestOrder;
    private static double bestKm;
    private static int evaluated;

    // Plus proche voisin : depuis le depot, aller toujours a la ville non visitee la plus proche.
    static int[] nearestNeighbour(double[][] dist) {
        int n = dist.length;
        int[] order = new int[n];
        boolean[] seen = new boolean[n];
        seen[0] = true;
        for (int i = 1; i < n; i++) {
            int from = order[i - 1];
            int next = -1;
            for (int j = 0; j < n; j++) {
                if (!seen[j] && (next < 0 || dist[from][j] < dist[from][next])) {
                    next = j;
                }
            }
            order[i] = next;
            seen[next] = true;
        }
        return order;
    }

    // 2-opt : inverser un troncon si cela raccourcit la tournee ; recommencer tant qu'on gagne.
    static int[] twoOpt(int[] start, double[][] dist) {
        int[] order = start.clone();
        int n = order.length;
        boolean improved = true;
        while (improved) {
            improved = false;
            for (int i = 1; i < n - 1; i++) {
                for (int k = i + 1; k < n; k++) {
                    int a = order[i - 1];
                    int b = order[i];
                    int c = order[k];
                    int d = order[(k + 1) % n];
                    if (dist[a][c] + dist[b][d] < dist[a][b] + dist[c][d] - 1e-9) {
                        for (int l = i, r = k; l < r; l++, r--) {
                            int t = order[l];
                            order[l] = order[r];
                            order[r] = t;
                        }
                        improved = true;
                    }
                }
            }
        }
        return order;
    }

    // Force brute : toutes les permutations des villes 1..n-1 (le depot reste en tete).
    static void permute(int[] order, int k, double[][] dist) {
        if (k == order.length) {
            evaluated++;
            double km = Route.length(order, dist);
            if (km < bestKm - 1e-9) {
                bestKm = km;
                bestOrder = order.clone();
            }
            return;
        }
        for (int i = k; i < order.length; i++) {
            int t = order[k];
            order[k] = order[i];
            order[i] = t;
            permute(order, k + 1, dist);
            order[i] = order[k];
            order[k] = t;
        }
    }

    public static void main(String[] args) {
        City[] cities = new City[Data.CITIES.length];
        StringBuilder names = new StringBuilder("villes :");
        for (int i = 0; i < cities.length; i++) {
            cities[i] = City.parse(Data.CITIES[i]);
            names.append(' ').append(cities[i].name());
        }
        System.out.println(names);
        City paris = cities[0];
        System.out.println("record : " + paris + " | egal " + paris.equals(new City("PARIS ", 48.8566, 2.3522)) + " | hash egal "
                + (paris.hashCode() == new City(" paris", 48.8566, 2.3522).hashCode()) + " | " + new City("nowhere") + " | creees " + City.created());

        int n = cities.length;
        double[][] dist = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = cities[i].distanceTo(cities[j]);   // methode default de l'interface Located
            }
        }
        System.out.println("distances : PAR-MAR " + Math.round(dist[0][2]) + " km, LIL-MAR " + Math.round(dist[6][2]) + " km, BOR-STR " + Math.round(dist[4][7]) + " km");

        int[] nn = nearestNeighbour(dist);
        Route greedy = new Route(nn, Route.length(nn, dist));
        int[] opt = twoOpt(nn, dist);
        Route improved = new Route(opt, Route.length(opt, dist));
        int[] all = new int[n];
        for (int i = 0; i < n; i++) {
            all[i] = i;
        }
        bestKm = Double.MAX_VALUE;
        permute(all, 1, dist);
        Route best = new Route(bestOrder, bestKm);
        System.out.println("plus proche voisin : " + greedy.describe(cities));
        System.out.println("apres 2-opt : " + improved.describe(cities));
        System.out.println("optimum (" + evaluated + " tournees) : " + best.describe(cities));
        System.out.println("ecart glouton " + Math.round(100 * (greedy.km() - best.km()) / best.km()) + " %, ecart 2-opt " + Math.round(100 * (improved.km() - best.km()) / best.km())
                + " % ; " + best.stats(cities, dist));
    }
}
