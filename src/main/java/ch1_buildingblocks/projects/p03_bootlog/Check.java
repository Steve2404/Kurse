package ch1_buildingblocks.projects.p03_bootlog;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON BootLog, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "--- 1er serveur ---",
            "1. Server : champ port = 8080",
            "2. Server : bloc A (port = 8080, name = null)",
            "3. Server : champ name = alpha",
            "4. Server : lecture anticipee de late = 0",
            "5. Server : champ late = 42",
            "6. Server : bloc B (early = 0, late = 42)",
            "7. Server : constructeur debut (this.maxUsers = 0, parametre maxUsers = 50)",
            "8. Server : constructeur fin (maxUsers = 50, serveur n 1)",
            "--- 2e serveur ---",
            "9. Server : champ port = 8080",
            "10. Server : bloc A (port = 8080, name = null)",
            "11. Server : champ name = alpha",
            "12. Server : lecture anticipee de late = 0",
            "13. Server : champ late = 42",
            "14. Server : bloc B (early = 0, late = 42)",
            "15. Server : constructeur debut (this.maxUsers = 0, parametre maxUsers = 10)",
            "16. Server : constructeur fin (maxUsers = 10, serveur n 2)",
            "--- portee ---",
            "17. portee : port local = 9090, champ this.port = 8080",
            "18. portee : dans le bloc, backup = 9091",
            "19. portee : name local = local, champ this.name = alpha",
            "--- bilan ---",
            "2 serveurs crees (attendu 2), 19 etapes journalisees",
            "first et second designent le serveur de 10 utilisateurs");
            // EXPECTED-END

    static final List<String> API = List.of(
            "2xre:(?m)^\\s*\\{\\s*$##bloc d'initialisation { ... } (au moins 2, chacun sur ses propres lignes)",
            "this.name", "this.port", "this.maxUsers", "var ", "final int ", "static int ",
            "re:(?m)^\\s*int \\w+ = \\w+\\(\\);##champ initialise par un appel de methode (int x = methode();)",
            // Crescendo : notions des chapitres 2 a 15, interdites au chapitre 1.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!->", "!+=", "!++",
            "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(",
            "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ", "!implements ",
            "!catch", "!throw ", "!Locale", "!this(##appel this(...) entre constructeurs (chapitre 6)",
            "!static {##bloc static (chapitre 6)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "BootLog", args, EXPECTED, API);
    }
}
