package ch3_makingdecisions.projects.p01_vending;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Vending, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"PIECE", "200", "PIECE", "30", "CHOIX", "D9", "CHOIX", "A1", "CHOIX", "B2", "PIECE", "100", "CHOIX", "B2", "HAPPY", "CHOIX", "C3", "PIECE", "50", "PIECE", "50", "PIECE", "20", "CHOIX", "C3", "CHOIX", "C3", "STOCK", "RENDU", "PIECE", "200", "PIECE", "100", "PIECE", "50", "PIECE", "20", "PIECE", "10", "RENDU", "TICKET"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "PIECE 2.00 -> credit 2.00",
            "PIECE 30 refusee",
            "CHOIX D9 -> produit inconnu",
            "CHOIX A1 -> Eau servi (1.20), reste 0.80",
            "CHOIX B2 -> credit insuffisant (manque 0.70)",
            "PIECE 1.00 -> credit 1.80",
            "CHOIX B2 -> Cafe servi (1.50), reste 0.30",
            "HAPPY HOUR on",
            "CHOIX C3 -> credit insuffisant (manque 1.20)",
            "PIECE 0.50 -> credit 0.80",
            "PIECE 0.50 -> credit 1.30",
            "PIECE 0.20 -> credit 1.50",
            "CHOIX C3 -> Chips servi (1.50), reste 0.00",
            "CHOIX C3 -> Chips epuise",
            "STOCK A1=1 B2=0 C3=0",
            "RENDU -> rien a rendre",
            "PIECE 2.00 -> credit 2.00",
            "PIECE 1.00 -> credit 3.00",
            "PIECE 0.50 -> credit 3.50",
            "PIECE 0.20 -> credit 3.70",
            "PIECE 0.10 -> credit 3.80",
            "RENDU -> 5 piece(s) : 200 100 50 20 10",
            "Commande inconnue : TICKET");
            // EXPECTED-END

    static final List<String> API = List.of(
            "args[++i]", "re:switch \\(\\w+\\) \\{\\s*case \"##switch sur un String", "yield ", "re:case \\d+, \\d+##case a plusieurs valeurs",
            "break;", "default:", "else if", "while (",
            "re:= switch \\(##switch expression affectee",
            // Crescendo : notions des chapitres 4 a 15, interdites au chapitre 3.
            "!StringBuilder", "!String.format", "!String.valueOf", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!.split(", "!.compareTo(", "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ",
            "!interface ", "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)",
            "!re:case [A-Z]\\w* \\w+ ->##pattern dans un case (preview en Java 17)", "!case null##case null (preview en Java 17)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Vending", args, ARGS, EXPECTED, API);
    }
}
