package ch1_buildingblocks.projects.p01_receipt;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Receipt, ou avec l'argument "solution".
 * Il lance ton main avec les arguments ARGS, comme le ferait :
 *   java ch1_buildingblocks.projects.p01_receipt.Receipt Dune 3 1250 Fondation 2 990 10 true
 */
public class Check {

    static final String[] ARGS = {"Dune", "3", "1250", "Fondation", "2", "990", "10", "true"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "+--------------------------------+",
            "|       LIBRAIRIE DU PORT        |",
            "|  12, quai des Brumes - Nantes  |",
            "+--------------------------------+",
            "Dune x 3 a 12.50 = 37.50",
            "Fondation x 2 a 9.90 = 19.80",
            "--------------------------------",
            "Articles   : 5",
            "Sous-total : 57.30",
            "Remise 10% : -5.73",
            "TOTAL      : 51.57",
            "Carte fidelite : true",
            "    Merci de votre visite !",
            "      \"Lire, c'est voyager.\"");
            // EXPECTED-END

    static final List<String> API = List.of(
            "String... ", "args[7]", "Integer.parseInt(", "Integer.valueOf(", ".intValue()", "Boolean.parseBoolean(",
            "2xclass ", "4x\"\"\"##\"\"\" (2 text blocks)", "re:\\\\\\r?\\n##\\ en fin de ligne d'un text block", "this.", "final int ",
            // Crescendo : notions des chapitres 2 a 15, interdites au chapitre 1.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!->", "!+=", "!++",
            "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(",
            "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ", "!implements ",
            "!catch", "!throw ", "!Locale");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Receipt", args, ARGS, EXPECTED, API);
    }
}
