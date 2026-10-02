package ch5_methods.projects.p02_bank;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON app.BankApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "ouvert 1 Alice courant 1200.00",
            "ouvert 2 Bob premium 500.00",
            "ouvert 3 Chloe courant 0.00",
            "ouvert 4 Dan premium 3000.00",
            "OK depot 250.00 sur 3 -> 250.00",
            "REFUS RETRAIT 1 150000 (decouvert autorise 0.00)",
            "OK retrait 600.00 sur 2 -> -100.00",
            "OK virement 750.50 de 4 vers 3",
            "REFUS RETRAIT 2 15000 (decouvert autorise 200.00)",
            "REFUS VIREMENT 1 9 100 (compte inconnu)",
            "OK retrait 10.00 sur 3 -> 990.50",
            "OK retrait 10.00 sur 3 -> 980.50",
            "OK retrait 10.00 sur 3 -> 970.50 ALERTE 3 retraits de suite",
            "REFUS DEPOT 1 -500 (compte inconnu ou montant <= 0)",
            "OK interets : 1:1.20 2:-1.50 3:0.97 4:5.62",
            "OK depot 500.00 sur 2 -> 398.50",
            "releve 2 Bob (premium, solde 398.50) : ouverture 500.00 | retrait 600.00 | interets -1.50 | depot 500.00",
            "releve 3 Chloe (courant, solde 971.47) : ouverture 0.00 | depot 250.00 | depot 750.50 | retrait 10.00 | retrait 10.00 | retrait 10.00 | interets 0.97",
            "classement : 1.Dan=2255.12 2.Alice=1201.20 3.Chloe=971.47 4.Bob=398.50",
            "total 4826.29, comptes ouverts 4, operations 14, max 8");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.ACCOUNTS", "Data.OPERATIONS", "re:package [\\w.]+\\.core;##paquet core", "re:package [\\w.]+\\.premium;##paquet premium",
            "re:package [\\w.]+\\.app;##paquet app", "private static int", "private long", "protected ",
            "extends Account", "import static", "re:(?m)^\\s+(?:void|boolean|int|long) \\w+\\(##methode package-private (sans modificateur)", "re:static \\w+ open\\w*\\(##fabrique static",
            "Account... ", "Arrays.copyOf(", "final ",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)", "!super##super (chapitre 6)",
            "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()",
            "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "app.BankApp", args, EXPECTED, API);
    }
}
