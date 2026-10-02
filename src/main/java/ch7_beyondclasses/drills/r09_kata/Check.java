package ch7_beyondclasses.drills.r09_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 9 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall09, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : Item[name=LIVRE, price=1200, rate=REDUCED] 1266 60000",
            "D02 : 0 2 3 EUR",
            "D03 : 54000 500 panier(0)",
            "D04 : carte 1234 especes 500 true",
            "D05 : 3798 50 true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "interface Priced", "enum Tax", "record Item(", "implements Priced",
            "Item {", "new Discount() {", "sealed interface Payment permits", "class Line",
            "cart.new Line(", "record Totals(", "static String currency()",
            // Crescendo : notions des chapitres 8 a 15, interdites au chapitre 7.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:\\(\\s*\\)\\s*->|\\(\\w+(?:\\s*,\\s*\\w+)*\\)\\s*->|[=(]\\s*\\w+\\s*->##lambda (chapitre 8)", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall09", args, EXPECTED, API);
    }
}
