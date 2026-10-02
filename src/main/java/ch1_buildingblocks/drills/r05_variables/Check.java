package ch1_buildingblocks.drills.r05_variables;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 5 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 5 2 3 13",
            "D02 : 1 8 7",
            "D03 : var 10 3.14 5000000000",
            "D04 : 3 affecte une fois",
            "D05 : 0 1",
            "D06 : 9 1 6",
            "D07 : 10",
            "D08 : 100");
            // EXPECTED-END

    static final List<String> API = List.of(
            "var ", "final int ", "final String ", "re:int \\w+, \\w+, \\w+ = ##plusieurs declarations sur une ligne",
            "int $", "re:int _\\w+##identificateur commencant par _", "this.", "re:(?m)^\\s*\\{\\s*$##bloc { } dans une methode",
            // Crescendo : notions des chapitres 2 a 15, interdites au chapitre 1.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!->", "!+=", "!++",
            "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(", "!.toUpperCase(", "!.toLowerCase(", "!.equals(",
            "!.repeat(", "!.strip", "!.trim(", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, EXPECTED, API);
    }
}
