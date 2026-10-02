package ch1_buildingblocks.projects.p04_bookshop;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON projet, ou avec l'argument "solution".
 * Partie 1 : il lance app.Main avec ARGS. Partie 2 : il execute TON script commandes.sh avec bash,
 * depuis la racine du depot, et compare ce qu'il affiche.
 */
public class Check {

    static final String[] ARGS = {"Dune", "Frank", "Herbert", "1965"};

    static final String SCRIPT = "commandes.sh";

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "MODELE : Dune, de Frank Herbert (1965)",
            "EXPORT : \"Dune\";\"Herbert, Frank\";1965");
            // EXPECTED-END

    static final List<String> SCRIPT_EXPECTED = List.of(
            // SCRIPT-BEGIN
            "MODELE : Hyperion, de Dan Simmons (1989)",
            "EXPORT : \"Hyperion\";\"Simmons, Dan\";1989",
            "classes dans le jar : 4",
            "MODELE : Fondation, de Isaac Asimov (1951)",
            "EXPORT : \"Fondation\";\"Asimov, Isaac\";1951",
            "MODELE : Le Hobbit, de John Tolkien (1937)",
            "EXPORT : \"Le Hobbit\";\"Tolkien, John\";1937",
            "Bonjour Lea ! (lance sans javac, depuis un seul fichier source)");
            // SCRIPT-END

    static final List<String> API = List.of(
            "re:(?m)^package [\\w.]+\\.app;##paquet ...app", "re:(?m)^package [\\w.]+\\.model;##paquet ...model",
            "re:(?m)^package [\\w.]+\\.export;##paquet ...export", "re:(?m)^package [\\w.]+\\.tools;##paquet ...tools",
            "re:(?m)^import [\\w.]+\\.model\\.\\*;##import avec joker du paquet model",
            "2xre:[\\w.]+\\.model\\.Book\\b##nom pleinement qualifie de model.Book (dans export.Book)",
            "re:new [\\w.]+\\.export\\.Book\\(##nom pleinement qualifie de export.Book (dans Main)",
            "2xre:(?m)^public class Book##classe publique Book (dans model ET dans export)",
            "Integer.parseInt(",
            // Crescendo : notions des chapitres 2 a 15, interdites au chapitre 1.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!->", "!+=", "!++",
            "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(",
            "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ", "!implements ",
            "!catch", "!throw ", "!Locale", "!module ");

    public static void main(String[] args) throws Exception {
        boolean code = ProjectChecker.check(Check.class, "app.Main", args, ARGS, EXPECTED, API);
        System.out.println();
        boolean script = ProjectChecker.checkScript(Check.class, SCRIPT, args, SCRIPT_EXPECTED);
        System.out.println();
        System.out.println(code && script ? "=== PROJET 4 COMPLET : code et commandes ===" : "=== Projet 4 : pas encore complet ===");
    }
}
