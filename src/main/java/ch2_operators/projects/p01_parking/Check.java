package ch2_operators.projects.p01_parking;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Parking, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"2", "135", "true", "false", "3"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "#1 VOITURE | 135 min | nuit | 2 h | 7.50",
            "#2 MOTO | 10 min | jour | 0 h | gratuit (moins de 15 min)",
            "#3 CAMION | 600 min | jour | abonne | 10 h | 28.00",
            "#4 VOITURE | 61 min | nuit | abonne | 1 h | offert (10e visite)",
            "#5 MOTO | 16 min | jour | 1 h | 1.50",
            "#6 CAMION | 1440 min | nuit | 24 h | offert (10e visite)",
            "Tickets emis : 6, prochain numero : 7");
            // EXPECTED-END

    static final List<String> API = List.of(
            "2xre:\\?[^;:]*:[^;]*\\?##ternaires imbriques (a ? b : c ? d : e)", "+=", "-=", "++",
            "% 10 == 0", "re:![a-z(]##operateur ! (negation)", "Boolean.parseBoolean(", "re:\\(\\w+ \\+ 59\\) / 60##arrondi a l'heure superieure (n + 59) / 60",
            // Crescendo : notions des chapitres 3 a 15, interdites au chapitre 2.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!do {",
            "!->", "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)", "!re:instanceof \\w+ \\w+##instanceof avec variable : pattern matching (chapitre 3)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Parking", args, ARGS, EXPECTED, API);
    }
}
