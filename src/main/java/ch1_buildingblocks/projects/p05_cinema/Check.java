package ch1_buildingblocks.projects.p05_cinema;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 5 - capstone (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON app.Cinema, ou avec l'argument "solution".
 * Il lance ton main comme : java ...app.Cinema "Dune 2" VO 2 1150 3 850 0x0F true
 */
public class Check {

    static final String[] ARGS = {"Dune 2", "VO", "2", "1150", "3", "850", "0x0F", "true"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "[1] Seance : champ salle = 7",
            "[2] Seance : bloc d'initialisation (capacite = 0)",
            "[3] Seance : champ capacite = 120",
            "[4] Seance : constructeur (film = Dune 2)",
            "+==============================+",
            "|        CINEMA LE PHARE       |",
            "+==============================+",
            "Film    : Dune 2 (VO), salle 7",
            "Plein   : 2 x 11.50 = 23.00",
            "Reduit  : 3 x 8.50 = 25.50",
            "Sous-total       : 48.50",
            "Code promo 0x0F : -15% = -7.27",
            "A PAYER          : 41.23",
            "Places restantes : 115",
            "Fidelite : true, code en binaire 1111, en octal 17",
            "     Bonne seance !");
            // EXPECTED-END

    static final List<String> API = List.of(
            "re:(?m)^package [\\w.]+\\.app;##paquet ...app", "re:(?m)^package [\\w.]+\\.model;##paquet ...model",
            "re:(?m)^import [\\w.]+\\.model\\.\\w+;##import explicite d'une classe de model",
            "2xpublic class ", "re:(?m)^\\s*\\{\\s*$##bloc d'initialisation", "this.", "var ", "final int ", "static int ",
            "Integer.parseInt(", "Integer.valueOf(", ".intValue()", "Integer.decode(", "Boolean.parseBoolean(",
            "Integer.toBinaryString(", "Integer.toOctalString(", "4x\"\"\"##\"\"\" (2 text blocks)",
            // Crescendo : notions des chapitres 2 a 15, interdites au chapitre 1.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!->", "!+=", "!++",
            "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(",
            "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ", "!implements ",
            "!catch", "!throw ", "!Locale", "!this(##appel this(...) entre constructeurs (chapitre 6)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "app.Cinema", args, ARGS, EXPECTED, API);
    }
}
