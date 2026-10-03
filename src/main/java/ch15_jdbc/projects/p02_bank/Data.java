package ch15_jdbc.projects.p02_bank;

/**
 * Les donnees du projet 2 (DONNEES, ne pas modifier).
 */
public final class Data {

    public static final String URL = "jdbc:h2:mem:p02_bank";

    /** Les tables : le CHECK interdit un solde negatif, la base refusera donc un decouvert. */
    public static final String[] SCHEMA = {
            "CREATE TABLE accounts (id VARCHAR(10) PRIMARY KEY, owner VARCHAR(20) NOT NULL, balance INT NOT NULL CHECK (balance >= 0))",
            "CREATE TABLE journal (seq INT AUTO_INCREMENT PRIMARY KEY, from_id VARCHAR(10) NOT NULL, to_id VARCHAR(10) NOT NULL, amount INT NOT NULL)"};

    /** "id|titulaire|solde" (en euros entiers). */
    public static final String[] ACCOUNTS = {"A1|Ana|500", "B2|Ben|200", "C3|Cleo|0", "D4|Dan|1000"};

    /** Les virements a traiter, dans l'ordre : "source>cible:montant". */
    public static final String[] TRANSFERS = {"A1>B2:150", "B2>C3:400", "C3>A1:0", "D4>Z9:100", "D4>C3:250", "Z9>A1:5", "C3>B2:250", "A1>A1:10", "B2>D4:350"};

    /** Deux paies groupees de D4 : "tout ou rien". La 2e echoue sur son 3e virement. */
    public static final String[][] PAYROLLS = {{"A1:100", "B2:100", "C3:100"}, {"A1:200", "B2:200", "C3:500"}};

    private Data() {
    }
}
