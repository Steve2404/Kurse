package ch15_jdbc.projects.p07_bikes;

/**
 * Les donnees du projet 7 (DONNEES, ne pas modifier).
 */
public final class Data {

    public static final String URL = "jdbc:h2:mem:p07_bikes";

    public static final String[] SCHEMA = {
            "CREATE TABLE stations (id VARCHAR(5) PRIMARY KEY, name VARCHAR(20) NOT NULL, capacity INT NOT NULL)",
            "CREATE TABLE bikes (id VARCHAR(5) PRIMARY KEY, station_id VARCHAR(5) REFERENCES stations(id), km INT NOT NULL, state VARCHAR(10) NOT NULL)",
            "CREATE TABLE members (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(20) NOT NULL UNIQUE, credit INT NOT NULL CHECK (credit >= 0))",
            "CREATE TABLE rides (id INT AUTO_INCREMENT PRIMARY KEY, member_id INT NOT NULL REFERENCES members(id), bike_id VARCHAR(5) NOT NULL REFERENCES bikes(id), "
                    + "from_station VARCHAR(5) NOT NULL, to_station VARCHAR(5), minutes INT, cost INT)",
            "CREATE TABLE debts (member_id INT PRIMARY KEY REFERENCES members(id), amount INT NOT NULL)"};

    /** "id|nom|capacite". */
    public static final String[] STATIONS = {"S1|Gare|3", "S2|Port|2", "S3|Parc|4"};

    /** "id|station|km" : tous les velos demarrent en etat OK. */
    public static final String[] BIKES = {"B1|S1|120", "B2|S1|80", "B3|S2|300", "B4|S3|15", "B5|S3|990", "B6|S3|40"};

    /** Le tarif : les FREE premieres minutes sont gratuites, puis STEP_PRICE centimes par tranche ENTAMEE de STEP minutes. */
    public static final int FREE = 30;
    public static final int STEP = 15;
    public static final int STEP_PRICE = 50;

    /** Au-dela de ce kilometrage, un velo part en revision. */
    public static final int SERVICE_KM = 300;

    /** Le scenario de la journee, une commande par ligne. */
    public static final String[] SCRIPT = {
            "membre Ana 300", "membre Ben 100", "membre Ana 50",
            "louer Ana B1", "louer Ben B1", "louer Ben B3", "louer Ana B2", "louer Zoe B2",
            "rendre Ana S2 45 12", "rendre Ben S2 130 30",
            "louer Ana B4", "rendre Ana S2 20 3", "rendre Ana S1 20 3",
            "recharger Ben 400", "maintenance", "louer Ben B5", "louer Ana B6", "rendre Ana S1 10 2", "rendre Ben S1 5 1"};

    private Data() {
    }
}
