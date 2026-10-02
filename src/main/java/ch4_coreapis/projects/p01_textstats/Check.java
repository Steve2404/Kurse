package ch4_coreapis.projects.p01_textstats;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON TextStats, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "LIGNES : 3, MOTS : 39, CARACTERES : 200, VOYELLES : 67",
            "PLUS LONG : palindromes (11), PLUS COURT : a",
            "FREQUENTS : le x4, un x4",
            "PALINDROMES : radar kayak rotor anna bob elle",
            "POSITIONS de \"radar\" : 3 83 139",
            "CENSURE : Un *****, un kayak, un rotor ; Bob a tout note dans son carnet.",
            "TITRE : Le Radar Du Kayak Detecte Un Rotor : Anna Et Bob Notent Le Niveau.",
            "COMMENCENT par \"Le \" : 1, FINISSENT par \".\" : 3, contient \"Bob\" : true, egal sans casse : true",
            "SALE : [   Total\\tfinal :   42 points   ] -> strip [Total\\tfinal :   42 points] -> stripLeading [Total\\tfinal :   42 points   ] -> stripTrailing [   Total\\tfinal :   42 points]",
            "ECHAPPEMENTS : [Total	final :   42 points], vide true, blanc true, trim [x]",
            "INDENTE :",
            "  a",
            "  b",
            "DESINDENTE :",
            "x",
            "  y",
            "12",
            "FORMATE : a     |  39|true [   ok]",
            "CHAINAGE : w0rld!");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".split(", ".charAt(", ".length()", ".indexOf(",
            ".substring(", ".toUpperCase()", ".toLowerCase()", ".equals(",
            ".equalsIgnoreCase(", ".startsWith(", ".endsWith(", ".contains(",
            ".replace(", ".strip()", ".stripLeading()", ".stripTrailing()",
            ".trim()", ".isEmpty()", ".isBlank()", ".indent(",
            ".stripIndent()", ".translateEscapes()", ".formatted(", "String.format(",
            ".repeat(", ".concat(", "Arrays.sort(", "re:indexOf\\([^,()]+, \\w+\\)##indexOf(texte, depart)",
            // Crescendo : notions des chapitres 5 a 15, interdites au chapitre 4.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!static {##bloc static (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!LocalDate.now()##date du jour (sortie non reproductible)", "!LocalDateTime.now()",
            "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "TextStats", args, EXPECTED, API);
    }
}
