package ch3_makingdecisions.projects.p05_robot;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 5 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Robot, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"10", "8", "25", "E", "3", "N", "2", "X", "1", "E", "4", "S", "6", "O", "2", "N", "9", "E", "1", "N", "7"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "1. E3 : arrive en (3,0), batterie 22",
            "2. N2 : arrive en (3,2), batterie 20",
            "3. X1 : direction inconnue, ignoree",
            "4. E4 : obstacle en (4,2), arret en (3,2) apres 0 pas",
            "5. S6 : mur atteint en (3,0) apres 2 pas",
            "6. O2 : arrive en (1,0), batterie 16",
            "7. N9 : obstacle en (1,6), arret en (1,5) apres 5 pas",
            "8. E1 : arrive en (2,5), batterie 10",
            "9. N7 : mur atteint en (2,7) apres 2 pas",
            "BILAN : 17 pas parcourus, position (2,7), distance au depart 9, batterie 8",
            "7 ..R#......",
            "6 .#........",
            "5 ..........",
            "4 ........#.",
            "3 ......#...",
            "2 ....#.....",
            "1 ..#.......",
            "0 S.........");
            // EXPECTED-END

    static final List<String> API = List.of(
            "re:(?m)^\\s*\\w+:\\s*$##etiquette de boucle", "re:continue \\w+;##continue etiquete", "re:break \\w+;##break etiquete", "continue;",
            "i += 2", "else if", "||", "while (",
            "2xswitch (",
            // Crescendo : notions des chapitres 4 a 15, interdites au chapitre 3.
            "!StringBuilder", "!String.format", "!String.valueOf", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!.split(", "!.compareTo(", "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ",
            "!interface ", "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)",
            "!re:case [A-Z]\\w* \\w+ ->##pattern dans un case (preview en Java 17)", "!case null##case null (preview en Java 17)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Robot", args, ARGS, EXPECTED, API);
    }
}
