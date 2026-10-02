package ch5_methods.projects.p07_shop;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 7 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON app.ShopApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "VENTE K01 2       | ok",
            "VENTE E03 3       | rupture (2 en stock)",
            "STOCK W06 2 3     | +5 (2 livraison(s))",
            "VENTE W06 4       | ok",
            "VENTE X99 1       | inconnu",
            "STOCK C04         | +0 (0 livraison(s))",
            "REMISE H05 25     | nouveau prix 59.92",
            "VENTE H05 3       | ok",
            "INFO 3            | E03 Ecran 189.90",
            "INFO 9            | aucun produit a cette position",
            "VENTE K01 3       | ok",
            "STOCK E03 1 1 1 1 | +4 (4 livraison(s))",
            "VENTE S02 10      | ok",
            "REMISE T07 90c    | nouveau prix 12.00",
            "REF      PRODUIT  PRIX     STOCK  VENDUS",
            "K01      Clavier  49.90        0      5  <- a commander",
            "S02      Souris   19.90        2     10  <- a commander",
            "E03      Ecran    189.90       6      0",
            "C04      Cable    7.90        30      0",
            "H05      Casque   59.92        1      3  <- a commander",
            "W06      Webcam   54.90        1      4  <- a commander",
            "T07      Tapis    12.00        7      0",
            "chiffre d'affaires 847.86, produits crees 7, copie defensive intacte true",
            "meilleur panier pour 150.00 : Souris Casque Webcam Tapis = 146.72 (65 noeuds explores)");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.PRODUCTS", "Data.ORDERS", "Data.BUDGET", "re:package [\\w.]+\\.model;##paquet model",
            "re:package [\\w.]+\\.app;##paquet app", "import static", "Product... ", "2xProduct find(",
            "2xlong discount(", "int restock(String", "re:int\\.\\.\\. ##varargs int...", "re:(?m)^\\s+(?:void|int|long) \\w+\\(##methode package-private",
            "Arrays.copyOf(", "static final int", "private static int",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "app.ShopApp", args, EXPECTED, API);
    }
}
