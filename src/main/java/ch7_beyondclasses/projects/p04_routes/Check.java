package ch7_beyondclasses.projects.p04_routes;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON RoutesApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "villes : Paris Lyon Marseille Toulouse Bordeaux Nantes Lille Strasbourg Nice Rennes",
            "record : City[name=Paris, lat=48.8566, lon=2.3522] | egal true | hash egal true | City[name=Nowhere, lat=0.0, lon=0.0] | creees 13",
            "distances : PAR-MAR 660 km, LIL-MAR 834 km, BOR-STR 758 km",
            "plus proche voisin : PAR-LIL-STR-LYO-MAR-NIC-TOU-BOR-NAN-REN-PAR = 2794 km",
            "apres 2-opt : PAR-LIL-STR-LYO-NIC-MAR-TOU-BOR-NAN-REN-PAR = 2666 km",
            "optimum (362880 tournees) : PAR-LIL-STR-LYO-NIC-MAR-TOU-BOR-NAN-REN-PAR = 2666 km",
            "ecart glouton 5 %, ecart 2-opt 0 % ; Stats[legs=10, longest=408.0, longestLeg=LIL-STR]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.CITIES", "interface Located", "default double distanceTo(", "record City(",
            "implements Located", "public City {", "this(name, 0, 0)", "private static int created",
            "record Route(", "record Stats(", "public Route {", "Math.asin(",
            // Crescendo : notions des chapitres 8 a 15, interdites au chapitre 7.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:\\(\\s*\\)\\s*->|\\(\\w+(?:\\s*,\\s*\\w+)*\\)\\s*->|[=(]\\s*\\w+\\s*->##lambda (chapitre 8)", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "RoutesApp", args, EXPECTED, API);
    }
}
