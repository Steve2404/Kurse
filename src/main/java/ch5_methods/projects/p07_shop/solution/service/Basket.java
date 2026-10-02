package ch5_methods.projects.p07_shop.solution.service;

import ch5_methods.projects.p07_shop.solution.model.Product;

/**
 * SOLUTION - le meilleur panier : depenser le plus possible sans depasser le budget (un exemplaire par produit).
 */
public class Basket {

    private static int explored;

    public static int explored() {
        return explored;
    }

    // Retour arriere sur "je prends / je ne prends pas". chosen[] est partage par tous les appels ;
    // best[] (taille n + 1, case n = total) garde la meilleure selection trouvee.
    public static boolean[] best(long budget, Product... candidates) {
        explored = 0;
        boolean[] chosen = new boolean[candidates.length];
        long[] best = new long[candidates.length + 1];
        explore(candidates, 0, budget, 0, chosen, best);
        boolean[] result = new boolean[candidates.length];
        for (int i = 0; i < candidates.length; i++) {
            result[i] = best[i] == 1;
        }
        return result;
    }

    private static void explore(Product[] p, int i, long budget, long spent, boolean[] chosen, long[] best) {
        explored++;
        if (spent > best[p.length]) {
            best[p.length] = spent;
            for (int k = 0; k < p.length; k++) {
                best[k] = chosen[k] ? 1 : 0;
            }
        }
        if (i == p.length) {
            return;
        }
        // Elagage : un produit trop cher ou en rupture n'ouvre pas la branche "je prends".
        if (p[i].getStock() > 0 && spent + p[i].getPrice() <= budget) {
            chosen[i] = true;
            explore(p, i + 1, budget, spent + p[i].getPrice(), chosen, best);
            chosen[i] = false;
        }
        explore(p, i + 1, budget, spent, chosen, best);
    }
}
