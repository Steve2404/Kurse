package inv.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * SOLUTION - le reassort sous budget : un sac a dos 0/1 par programmation dynamique.
 */
public final class Restock {

    // Utilise java.logging : c'est ce qui rend le module java.logging NECESSAIRE (jdeps le verra).
    private static final Logger LOG = Logger.getLogger(Restock.class.getName());

    private Restock() {
    }

    public static String loggerName() {
        return LOG.getName();
    }

    // best[i][b] = meilleure valeur avec les i premiers articles et un budget b ; puis on remonte le choix.
    public static List<Item> choose(List<Item> items, int budget) {
        int n = items.size();
        int[][] best = new int[n + 1][budget + 1];
        for (int i = 1; i <= n; i++) {
            Item it = items.get(i - 1);
            for (int b = 0; b <= budget; b++) {
                best[i][b] = best[i - 1][b];
                if (it.cost() <= b) {
                    best[i][b] = Math.max(best[i][b], best[i - 1][b - it.cost()] + it.value());
                }
            }
        }
        List<Item> chosen = new ArrayList<>();
        for (int i = n, b = budget; i > 0; i--) {
            if (best[i][b] != best[i - 1][b]) {
                Item it = items.get(i - 1);
                chosen.add(it);
                b -= it.cost();
            }
        }
        Collections.reverse(chosen);
        return chosen;
    }
}
