package ch15_jdbc.projects.p03_orders;

/**
 * Les donnees du projet 3 (DONNEES, ne pas modifier).
 */
public final class Data {

    public static final String URL = "jdbc:h2:mem:p03_orders";

    /** Le stock ne peut pas etre negatif (CHECK) ; une commande ne peut pas contenir deux fois le meme produit (cle primaire). */
    public static final String[] SCHEMA = {
            "CREATE TABLE products (sku VARCHAR(5) PRIMARY KEY, name VARCHAR(20) NOT NULL, stock INT NOT NULL CHECK (stock >= 0), price INT NOT NULL)",
            "CREATE TABLE orders (id INT AUTO_INCREMENT PRIMARY KEY, customer VARCHAR(20) NOT NULL, status VARCHAR(10) NOT NULL, total INT NOT NULL)",
            "CREATE TABLE order_lines (order_id INT NOT NULL, sku VARCHAR(5) NOT NULL, qty INT NOT NULL, PRIMARY KEY (order_id, sku))"};

    /** "sku|nom|stock|prix". */
    public static final String[] PRODUCTS = {"P1|Clavier|5|40", "P2|Souris|10|15", "P3|Ecran|2|180", "P4|Cable|20|5"};

    /** "client|politique|sku:qte,sku:qte,..." ; TOUT = tout ou rien, PARTIEL = on livre ce qui est possible. */
    public static final String[] ORDERS = {"Ana|PARTIEL|P1:2,P3:3,P4:5", "Ben|TOUT|P1:2,P2:3", "Cleo|TOUT|P3:2,P1:2",
            "Dan|PARTIEL|P9:1,P2:4,P2:1", "Eve|PARTIEL|P3:1,P4:2", "Fay|PARTIEL|P3:5"};

    private Data() {
    }
}
