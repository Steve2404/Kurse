package ch16_testing.projects.p07_debug.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * L'inventaire de Data, CORRIGE. La structure est volontairement gardee : on corrige un bug sans tout reecrire.
 * Chaque correction porte son numero (BUG 1 a 6) et le test de non-regression qui la protege.
 */
public final class Inventory {

    static final class Item {
        final String sku;
        Integer qty;
        int priceCents;

        Item(String sku, int qty, int priceCents) {
            this.sku = sku;
            this.qty = qty;
            this.priceCents = priceCents;
        }
    }

    private final List<Item> items = new ArrayList<>();

    // Piege : une ligne mal formee doit etre refusee AVANT de toucher au stock.
    public void receive(String line) {
        String[] parts = line.split(";");
        int qty;
        int price;
        try {
            if (parts.length != 3) {
                throw new NumberFormatException();
            }
            qty = Integer.parseInt(parts[1]);
            price = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("livraison invalide : " + line);
        }
        if (qty < 1 || price < 0) {
            throw new IllegalArgumentException("livraison invalide : " + line);
        }
        String sku = parts[0];
        for (Item item : items) {
            // BUG 1 : == comparait les REFERENCES ; deux "PEN" venus de split() sont deux objets differents.
            if (item.sku.equals(sku)) {
                item.qty += qty;
                item.priceCents = price;
                return;
            }
        }
        items.add(new Item(sku, qty, price));
    }

    public int quantity(String sku) {
        int total = 0;
        for (Item item : items) {
            if (item.sku.equals(sku)) {
                total += item.qty;
            }
        }
        return total;
    }

    public boolean ship(String sku, int qty) {
        if (qty < 1) {
            throw new IllegalArgumentException("quantite invalide : " + qty);
        }
        for (Item item : items) {
            if (item.sku.equals(sku)) {
                // BUG 2 : > refusait d'expedier exactement tout le stock.
                if (item.qty >= qty) {
                    item.qty -= qty;
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public long stockValueCents() {
        long total = 0;
        for (Item item : items) {
            // BUG 3 : int * int deborde AVANT d'etre range dans le long ; on convertit un des deux d'abord.
            total += (long) item.qty * item.priceCents;
        }
        return total;
    }

    public long averagePriceCents() {
        long sum = 0;
        int n = 0;
        for (Item item : items) {
            if (item.qty > 0) {
                sum += item.priceCents;
                n++;
            }
        }
        // BUG 4 : sum / n tronquait ; + n / 2 arrondit au centime le plus proche.
        return n == 0 ? 0 : (sum + n / 2) / n;
    }

    // BUG 5 : == sur deux Integer compare des OBJETS ; vrai jusqu'a 127 (le cache), faux pour 1000.
    public boolean sameQuantity(String a, String b) {
        return quantity(a) == quantity(b);
    }

    public List<String> lowStock(int threshold) {
        List<String> result = new ArrayList<>();
        // BUG 6 : la boucle commencait a 1 et oubliait le premier article.
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).qty < threshold) {
                result.add(items.get(i).sku);
            }
        }
        Collections.sort(result);
        return result;
    }
}
