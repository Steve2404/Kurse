package ch15_jdbc.projects.p05_procedures;

/**
 * Les donnees du projet 5 (DONNEES, ne pas modifier).
 */
public final class Data {

    public static final String URL = "jdbc:h2:mem:p05_procedures";

    public static final String[] SCHEMA = {
            "CREATE TABLE customers (id INT PRIMARY KEY, name VARCHAR(20) NOT NULL, tier VARCHAR(10) NOT NULL, points INT NOT NULL)",
            "CREATE TABLE purchases (id INT AUTO_INCREMENT PRIMARY KEY, customer_id INT NOT NULL REFERENCES customers(id), amount INT NOT NULL, card VARCHAR(25) NOT NULL)"};

    /** "id|nom|niveau" ; tout le monde demarre a 0 point. Niveaux : BRONZE < SILVER < GOLD. */
    public static final String[] CUSTOMERS = {"1|Ana|GOLD", "2|Ben|SILVER", "3|Cleo|BRONZE", "4|Dan|BRONZE"};

    /** "id client|montant|carte" : des numeros de carte, valides ou non selon l'algorithme de Luhn. */
    public static final String[] PURCHASES = {"1|120|4539 1488 0343 6467", "2|80|4539 1488 0343 6468", "1|45|4111 1111 1111 1111",
            "3|300|5500 0000 0000 0004", "4|60|6011 1111 1111 1117", "2|200|3782 822463 10005", "4|90|1234 5678 9012 3456", "3|15|7992 7398 713"};

    private Data() {
    }
}
