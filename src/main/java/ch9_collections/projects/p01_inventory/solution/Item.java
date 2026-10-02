package ch9_collections.projects.p01_inventory.solution;

/**
 * SOLUTION - un article : un record, donc equals/hashCode sur tous les composants (utile pour contains, indexOf, remove(Object)).
 */
public record Item(String sku, String name, String category, int stock, long price) {

    public static Item parse(String line) {
        String[] p = line.split(" ");
        return new Item(p[0], p[1], p[2], Integer.parseInt(p[3]), Long.parseLong(p[4]));
    }

    public long value() {
        return stock * price;
    }

    public Item withStock(int newStock) {
        return new Item(sku, name, category, newStock, price);
    }

    public Item withPrice(long newPrice) {
        return new Item(sku, name, category, stock, newPrice);
    }

    @Override
    public String toString() {
        return name;
    }
}
