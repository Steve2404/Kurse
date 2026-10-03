package ch15_jdbc.projects.p04_import;

/**
 * Les donnees du projet 4 (DONNEES, ne pas modifier).
 */
public final class Data {

    public static final String URL = "jdbc:h2:mem:p04_import";

    public static final String[] SCHEMA = {
            "CREATE TABLE customers (id INT AUTO_INCREMENT PRIMARY KEY, email VARCHAR(40) NOT NULL UNIQUE, name VARCHAR(30) NOT NULL, city VARCHAR(20) NOT NULL)",
            "CREATE TABLE import_log (id INT AUTO_INCREMENT PRIMARY KEY, file VARCHAR(20) NOT NULL, inserted INT NOT NULL, rejected INT NOT NULL)"};

    /** La taille d'un lot : on envoie les INSERT a la base par paquets de CHUNK. */
    public static final int CHUNK = 4;

    /** Le 1er fichier, "email;nom;ville" : des espaces en trop, des lignes invalides, un doublon (meme email, autre casse). */
    public static final String[] CLIENTS = {
            "ana@mail.fr;Ana;Lyon",
            "  ben@mail.fr ; Ben ;Paris",
            "cleo@mail.fr;Cleo;Lyon",
            "dan.mail.fr;Dan;Nice",
            "eve@mail.fr;Eve;Paris",
            "ANA@MAIL.FR;Ana Bis;Lille",
            "fay@mail.fr;Fay;Nice",
            "gus@mail.fr;Gus;Lyon",
            "hal@mail.fr; ;Paris",
            "ivy@mail.fr;Ivy;Lille",
            "jon@mail.fr;Jon;Paris;VIP",
            "kim@mail.fr;Kim;Nice",
            "leo@mail.fr;Leo;Lyon"};

    /** Le 2e fichier : deux clients y sont DEJA en base (la contrainte UNIQUE les refusera). */
    public static final String[] DELTA = {"mia@mail.fr;Mia;Paris", "ben@mail.fr;Ben;Paris", "noe@mail.fr;Noe;Lyon", "Gus@mail.fr;Gus;Lyon", "oli@mail.fr;Oli;Nice"};

    private Data() {
    }
}
