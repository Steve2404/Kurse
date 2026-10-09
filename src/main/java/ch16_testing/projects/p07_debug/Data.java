package ch16_testing.projects.p07_debug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Les donnees du projet 7 (DONNEES, ne pas modifier) : un programme d'inventaire ECRIT PAR QUELQU'UN D'AUTRE,
 * qui contient SIX bugs. Lance Data (fleche verte) : sa sortie est fausse. Le TODO.md donne la sortie juste.
 * Tu ne corriges pas ce fichier : tu le copies dans ta propre classe Inventory, et c'est elle que tu corriges.
 */
public final class Data {

    /** Les livraisons du jour : "CODE;quantite;prix unitaire en centimes". */
    public static final String[] DELIVERIES = {
            "PEN;120;150",
            "INK;1000;899",
            "PAD;40;320",
            "PEN;80;150",
            "GLUE;5;275",
            "BOX;50000;99999",
            "CUP;1000;450"};

    private Data() {
    }

    public static void main(String[] args) {
        LegacyInventory inv = new LegacyInventory();
        for (String line : DELIVERIES) {
            inv.receive(line);
        }
        System.out.println("PEN en stock : " + inv.quantity("PEN"));
        System.out.println("prix moyen : " + inv.averagePriceCents());
        System.out.println("expedier 200 PEN : " + inv.ship("PEN", 200));
        System.out.println("PEN en stock : " + inv.quantity("PEN"));
        System.out.println("valeur du stock : " + inv.stockValueCents());
        System.out.println("INK et CUP au meme niveau : " + inv.sameQuantity("INK", "CUP"));
        System.out.println("stock bas (< 100) : " + inv.lowStock(100));
    }

    /** L'inventaire bogue. Lis-le, lance-le dans le debogueur, mais ne le modifie pas. */
    static final class LegacyInventory {

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

        void receive(String line) {
            String[] parts = line.split(";");
            String sku = parts[0];
            int qty = Integer.parseInt(parts[1]);
            int price = Integer.parseInt(parts[2]);
            for (Item item : items) {
                if (item.sku == sku) {
                    item.qty += qty;
                    item.priceCents = price;
                    return;
                }
            }
            items.add(new Item(sku, qty, price));
        }

        int quantity(String sku) {
            int total = 0;
            for (Item item : items) {
                if (item.sku.equals(sku)) {
                    total += item.qty;
                }
            }
            return total;
        }

        boolean ship(String sku, int qty) {
            for (Item item : items) {
                if (item.sku.equals(sku)) {
                    if (item.qty > qty) {
                        item.qty -= qty;
                        return true;
                    }
                    return false;
                }
            }
            return false;
        }

        long stockValueCents() {
            long total = 0;
            for (Item item : items) {
                total += item.qty * item.priceCents;
            }
            return total;
        }

        long averagePriceCents() {
            long sum = 0;
            int n = 0;
            for (Item item : items) {
                if (item.qty > 0) {
                    sum += item.priceCents;
                    n++;
                }
            }
            return n == 0 ? 0 : sum / n;
        }

        boolean sameQuantity(String a, String b) {
            return find(a).qty == find(b).qty;
        }

        List<String> lowStock(int threshold) {
            List<String> result = new ArrayList<>();
            for (int i = 1; i < items.size(); i++) {
                if (items.get(i).qty < threshold) {
                    result.add(items.get(i).sku);
                }
            }
            Collections.sort(result);
            return result;
        }

        private Item find(String sku) {
            for (Item item : items) {
                if (item.sku.equals(sku)) {
                    return item;
                }
            }
            return null;
        }
    }
}
