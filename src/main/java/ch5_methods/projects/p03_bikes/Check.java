package ch5_methods.projects.p03_bikes;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON BikeApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "[charge] BikeApp",
            "main commence",
            "[charge] Network : champ NAMES",
            "[charge] Network : bloc static 1 (5 stations)",
            "[charge] Network : bloc static 2 (Floyd-Warshall, 12 raccourcis trouves)",
            "Gare -> Parc : 5 km, diametre 9 km, voisin le plus proche du Parc 3 km",
            "[objet] station #0",
            "[objet] station #1",
            "[objet] station #2",
            "[objet] station #3",
            "[objet] station #4",
            "depart : Gare=6/8 Centre=1/6 Parc=0/5 Port=3/6 Campus=8/10",
            "Parc->Gare : vide, marche vers Centre (3 km) 2 km",
            "Centre->Port : vide, marche vers Gare (2 km) 9 km",
            "Centre->Parc : vide, marche vers Gare (2 km) 5 km",
            "Campus->Port : 2 km",
            "Campus->Port : 2 km",
            "Campus->Port : plein, depot a Campus (+2 km a pied) 0 km",
            "Gare->Port : plein, depot a Campus (+2 km a pied) 7 km",
            "Gare->Port : plein, depot a Campus (+2 km a pied) 7 km",
            "arrivee : Gare=3/8 Centre=0/6 Parc=1/5 Port=6/6 Campus=8/10",
            "km a velo 34, km a pied 13",
            "camion : 1 Port->Parc 3 Campus->Centre 1 Port->Gare (18 km) => Gare=4/8 Centre=3/6 Parc=2/5 Port=4/6 Campus=5/10",
            "static via null : 5 stations, Campus (#4 sur 5)");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.ROADS", "Data.TRIPS", "2xstatic {", "static final int",
            "re:(?m)^\\s+\\{\\s*$##bloc { } de l objet", "re:final int \\w+;##champ final de l objet", "import static", "re:\\w+ \\w+ = null;##reference null (appel static)",
            "re:\\[i\\]\\[k\\] \\+ \\w+\\[k\\]\\[j\\]##Floyd-Warshall", "re:private static \\w+\\[\\] ##registre static",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "BikeApp", args, EXPECTED, API);
    }
}
