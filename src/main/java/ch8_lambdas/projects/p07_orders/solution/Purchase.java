package ch8_lambdas.projects.p07_orders.solution;

/**
 * SOLUTION - une commande : des lignes (reference, quantite).
 */
public record Purchase(String id, String customer, String tier, String[] skus, int[] quantities) {

    public Purchase {
        skus = skus.clone();
        quantities = quantities.clone();
    }

    public static Purchase parse(String line) {
        String[] p = line.split(" ");
        String[] skus = new String[p.length - 3];
        int[] qty = new int[p.length - 3];
        for (int i = 3; i < p.length; i++) {
            String[] item = p[i].split(":");
            skus[i - 3] = item[0];
            qty[i - 3] = Integer.parseInt(item[1]);
        }
        return new Purchase(p[0], p[1], p[2], skus, qty);
    }

    public int lineCount() {
        return skus.length;
    }

    public String sku(int i) {
        return skus[i];
    }

    public int quantity(int i) {
        return quantities[i];
    }
}
