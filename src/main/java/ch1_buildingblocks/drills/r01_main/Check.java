package ch1_buildingblocks.drills.r01_main;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur du drill de rappel 1 (ne pas modifier). Consigne : TODO.md.
 * Lance-le tel quel pour verifier TON Recall01, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"alpha", "42", "3.5", "TRUE", "Bonjour le monde"};

    static final String SCRIPT = "commandes.sh";

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : alpha puis 42",
            "D02 : 50 et non 428",
            "D03 : 7.0",
            "D04 : true false",
            "D05 : [Bonjour le monde]");
            // EXPECTED-END

    static final List<String> SCRIPT_EXPECTED = List.of(
            // SCRIPT-BEGIN
            "D01 : un puis 1",
            "D02 : 9 et non 18",
            "D03 : 0.5",
            "D04 : true false",
            "D05 : [deux mots]",
            "D01 : trois puis 3",
            "D02 : 11 et non 38",
            "D03 : 3.0",
            "D04 : false false",
            "D05 : [  espaces  ]",
            "D01 : quatre puis 4",
            "D02 : 12 et non 48",
            "D03 : 4.0",
            "D04 : true false",
            "D05 : [seul]");
            // SCRIPT-END

    static final List<String> API = List.of(
            "final String... ", "Integer.parseInt(", "Double.parseDouble(", "Boolean.parseBoolean(",
            "args[4]",
            // Crescendo : notions des chapitres 2 a 15, interdites au chapitre 1.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!->", "!+=", "!++",
            "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(", "!.toUpperCase(", "!.toLowerCase(", "!.equals(",
            "!.repeat(", "!.strip", "!.trim(", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall01", args, ARGS, EXPECTED, API);
        System.out.println();
        ProjectChecker.checkScript(Check.class, SCRIPT, args, SCRIPT_EXPECTED);
    }
}
