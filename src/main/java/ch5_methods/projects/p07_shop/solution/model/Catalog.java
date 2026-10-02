package ch5_methods.projects.p07_shop.solution.model;

import java.util.Arrays;

/**
 * SOLUTION - le catalogue : la seule porte publique pour modifier les produits.
 */
public class Catalog {

    private Product[] products = new Product[4];
    private int size;
    private long revenue;

    public void add(Product... items) {
        for (Product p : items) {
            if (size == products.length) {
                products = Arrays.copyOf(products, size * 2);
            }
            products[size++] = p;
        }
    }

    // Surcharges de find : par reference (String) ou par position (int, a partir de 1).
    public Product find(String sku) {
        for (int i = 0; i < size; i++) {
            if (products[i].getSku().equals(sku)) {
                return products[i];
            }
        }
        return null;
    }

    public Product find(int position) {
        return position >= 1 && position <= size ? products[position - 1] : null;
    }

    // Copie defensive : rendre le tableau interne laisserait l'appelant le modifier (passage de reference).
    public Product[] all() {
        return Arrays.copyOf(products, size);
    }

    public long revenue() {
        return revenue;
    }

    public String sell(String sku, int qty) {
        Product p = find(sku);
        if (p == null) {
            return "inconnu";
        }
        if (qty > p.getStock()) {
            return "rupture (" + p.getStock() + " en stock)";
        }
        p.sell(qty);
        revenue += qty * p.getPrice();
        return "ok";
    }

    // Varargs : zero, une ou plusieurs livraisons. Rend la quantite reellement ajoutee (plafond MAX_STOCK).
    public int restock(String sku, int... deliveries) {
        Product p = find(sku);
        if (p == null || deliveries.length == 0) {
            return 0;
        }
        int total = 0;
        for (int d : deliveries) {
            total += p.adjust(d);
        }
        return total;
    }

    // Deux surcharges de remise : un pourcentage entier, ou un montant fixe en centimes (long).
    public long discount(String sku, int percent) {
        Product p = find(sku);
        long newPrice = p.getPrice() - Math.round(p.getPrice() * percent / 100.0);
        p.setPrice(newPrice);
        return newPrice;
    }

    public long discount(String sku, long cents) {
        Product p = find(sku);
        p.setPrice(Math.max(0, p.getPrice() - cents));
        return p.getPrice();
    }
}
