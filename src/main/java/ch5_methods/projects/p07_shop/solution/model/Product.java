package ch5_methods.projects.p07_shop.solution.model;

/**
 * SOLUTION - un produit : etat prive, lecture publique, modification reservee au paquet model.
 */
public class Product {

    public static final int MAX_STOCK = 99;
    private static int created;

    private String sku;
    private String name;
    private long price;
    private int stock;
    private int sold;

    public static Product of(String sku, String name, long price, int stock) {
        Product p = new Product();
        p.sku = sku;
        p.name = name;
        p.price = price;
        p.stock = Math.min(stock, MAX_STOCK);
        created++;
        return p;
    }

    public static int created() {
        return created;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public int getSold() {
        return sold;
    }

    // Package-private : seul Catalog (meme paquet) change le stock et le prix.
    int adjust(int delta) {
        int before = stock;
        stock = Math.max(0, Math.min(MAX_STOCK, stock + delta));
        return stock - before;
    }

    void sell(int qty) {
        stock -= qty;
        sold += qty;
    }

    void setPrice(long price) {
        this.price = price;
    }
}
