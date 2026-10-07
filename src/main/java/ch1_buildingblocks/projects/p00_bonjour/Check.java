package ch1_buildingblocks.projects.p00_bonjour;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du projet 0 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Bonjour, ou avec l'argument "solution".
 * Il lance ton main avec les arguments ARGS, comme le ferait :
 *   java ch1_buildingblocks.projects.p00_bonjour.Bonjour Marie Nantes
 */
public class Check {

    static final String[] ARGS = {"Marie", "Nantes"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "Bonjour, Marie !",
            "Tu habites a Nantes.",
            "Bienvenue dans le cours de Java.");
            // EXPECTED-END

    static final List<String> API = List.of(
            "System.out.println(", "args[0]", "args[1]",
            // Crescendo : notions des chapitres 2 a 15, interdites au chapitre 1.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!->", "!+=", "!++",
            "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length", "!.substring(", "!.charAt(",
            "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ", "!implements ",
            "!catch", "!throw ", "!Locale");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Bonjour", args, ARGS, EXPECTED, API);
    }
}
