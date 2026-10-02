package ch1_buildingblocks.drills.r06_init;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 6 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall06, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : champ > bloc1 > bloc2 > constructeur()",
            "D02 : champ > bloc1 > bloc2 > constructeur(3)",
            "D03 : 8 0",
            "D04 : 3",
            "D05 : 1");
            // EXPECTED-END

    static final List<String> API = List.of(
            "2xre:(?m)^\\s*\\{\\s*$##bloc d'initialisation", "static int ", "re:int \\w+ = \\w+\\(\\);##champ initialise par une methode", "this.size",
            "2xEgg(",
            // Crescendo : notions des chapitres 2 a 15, interdites au chapitre 1.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!->", "!+=", "!++",
            "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(", "!.toUpperCase(", "!.toLowerCase(", "!.equals(",
            "!.repeat(", "!.strip", "!.trim(", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall06", args, EXPECTED, API);
    }
}
