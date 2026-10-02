package ch5_methods.projects.p07_shop.solution.app;

import ch5_methods.projects.p07_shop.Data;
import ch5_methods.projects.p07_shop.solution.model.Catalog;
import ch5_methods.projects.p07_shop.solution.model.Product;
import ch5_methods.projects.p07_shop.solution.service.Basket;

import static ch5_methods.projects.p07_shop.solution.util.Format.money;
import static ch5_methods.projects.p07_shop.solution.util.Format.pad;

/**
 * SOLUTION du projet 7 (capstone) - la boutique.
 */
public class ShopApp {

    public static void main(String[] args) {
        Catalog catalog = new Catalog();
        for (String line : Data.PRODUCTS) {
            String[] p = line.split(" ");
            catalog.add(Product.of(p[0], p[1], Long.parseLong(p[2]), Integer.parseInt(p[3])));
        }
        for (String order : Data.ORDERS) {
            System.out.println(pad(order, 18) + "| " + execute(catalog, order));
        }
        System.out.println(pad("REF") + pad("PRODUIT") + pad("PRIX", 9) + pad("STOCK", 7) + "VENDUS");
        for (Product p : catalog.all()) {
            String alert = p.getStock() < 3 ? "  <- a commander" : "";
            System.out.println(pad(p.getSku()) + pad(p.getName()) + pad(money(p.getPrice()), 9) + pad(p.getStock(), 5) + "  " + pad(p.getSold(), 5) + alert);
        }
        Product[] copy = catalog.all();
        copy[0] = null;   // ne touche que la copie
        System.out.println("chiffre d'affaires " + money(catalog.revenue()) + ", produits crees " + Product.created() + ", copie defensive intacte "
                + (catalog.find(1) != null));
        Product[] all = catalog.all();
        boolean[] pick = Basket.best(Data.BUDGET, all);
        StringBuilder basket = new StringBuilder();
        long total = 0;
        for (int i = 0; i < all.length; i++) {
            if (pick[i]) {
                basket.append(' ').append(all[i].getName());
                total += all[i].getPrice();
            }
        }
        System.out.println("meilleur panier pour " + money(Data.BUDGET) + " :" + basket + " = " + money(total) + " (" + Basket.explored() + " noeuds explores)");
    }

    private static String execute(Catalog catalog, String order) {
        String[] p = order.split(" ");
        switch (p[0]) {
            case "VENTE":
                return catalog.sell(p[1], Integer.parseInt(p[2]));
            case "STOCK": {
                // Les quantites sont lues dans un int[] puis passees au varargs.
                int[] deliveries = new int[p.length - 2];
                for (int i = 0; i < deliveries.length; i++) {
                    deliveries[i] = Integer.parseInt(p[i + 2]);
                }
                return "+" + catalog.restock(p[1], deliveries) + " (" + deliveries.length + " livraison(s))";
            }
            case "REMISE":
                // Le TYPE de l'argument choisit la surcharge : int -> pourcentage, long -> montant fixe.
                if (p[2].endsWith("c")) {
                    long cents = Long.parseLong(p[2].substring(0, p[2].length() - 1));
                    return "nouveau prix " + money(catalog.discount(p[1], cents));
                }
                return "nouveau prix " + money(catalog.discount(p[1], Integer.parseInt(p[2])));
            case "INFO": {
                Product found = catalog.find(Integer.parseInt(p[1]));
                return found == null ? "aucun produit a cette position" : found.getSku() + " " + found.getName() + " " + money(found.getPrice());
            }
            default:
                return "commande inconnue";
        }
    }
}
