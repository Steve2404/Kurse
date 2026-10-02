package ch2_operators.projects.p05_reportcard;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 5 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON ReportCard, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"14.5", "9.5", "17", "3", "2", "1", "4", "101"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "points 79.5 / coefficients 6 = 13.25",
            "piege : (int) points = 79 ; 79 / 6 = 13 ; (double) (79 / 6) = 13.0 ; (double) 79 / 6 = 13.166666666666666",
            "absences 4 -> penalite 0.25 ; options 101 : delegue latin",
            "moyenne finale : 13.7 (Assez bien)",
            "ecart a la classe : +2.2",
            "lettre : B, admis : true, felicitations : false");
            // EXPECTED-END

    static final List<String> API = List.of(
            "(int)", "(char)", "+=", "-=",
            "re:Integer\\.parseInt\\([^,()]+(\\[\\d\\])?, 2\\)##parseInt en base 2", "<<", " & ", "&&",
            "||", "2xre:\\?[^;:]*:[^;]*\\?##ternaires imbriques (a ? b : c ? d : e)", "Double.parseDouble(", "Integer.toBinaryString(",
            // Crescendo : notions des chapitres 3 a 15, interdites au chapitre 2.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!do {",
            "!->", "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)", "!re:instanceof \\w+ \\w+##instanceof avec variable : pattern matching (chapitre 3)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "ReportCard", args, ARGS, EXPECTED, API);
    }
}
