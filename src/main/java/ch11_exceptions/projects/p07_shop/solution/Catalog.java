package ch11_exceptions.projects.p07_shop.solution;

import java.util.Map;
import java.util.TreeMap;

/**
 * SOLUTION - le catalogue et le calcul d'un panier "produit:quantite,produit:quantite".
 */
public class Catalog {

    private final Map<String, Long> prices = new TreeMap<>();

    public Catalog(String[] lines) {
        for (String line : lines) {
            String[] p = line.split("=");
            prices.put(p[0], Long.parseLong(p[1]));
        }
    }

    public long price(String product) throws UnknownProductException {
        Long price = prices.get(product);
        if (price == null) {
            throw new UnknownProductException(product);
        }
        return price;
    }

    // Rend {nombre d'articles, total en centimes}. Une quantite illisible -> NumberFormatException (non verifiee).
    public long[] total(String cart) throws UnknownProductException {
        long count = 0;
        long total = 0;
        for (String entry : cart.split(",")) {
            String[] p = entry.split(":");
            int quantity = Integer.parseInt(p[1]);
            total += quantity * price(p[0]);
            count += quantity;
        }
        return new long[] {count, total};
    }
}
