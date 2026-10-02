package ch3_makingdecisions.projects.p04_rpn;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Rpn, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"3", "4", "+", "2", "x", "DUP", "x", "7", "/", "2.5", "+", "CHS", "0", "/", "DROP", "SWAP", "9", "-", "CLR", "10", "4", "/"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "1. 3 -> T=0 Z=0 Y=0 X=3",
            "2. 4 -> T=0 Z=0 Y=3 X=4",
            "3. + -> T=0 Z=0 Y=0 X=7",
            "4. 2 -> T=0 Z=0 Y=7 X=2",
            "5. x -> T=0 Z=0 Y=0 X=14",
            "6. DUP -> T=0 Z=0 Y=14 X=14",
            "7. x -> T=0 Z=0 Y=0 X=196",
            "8. 7 -> T=0 Z=0 Y=196 X=7",
            "9. / -> T=0 Z=0 Y=0 X=28",
            "10. 2.5 -> T=0 Z=0 Y=28 X=2.5 (X decimal)",
            "11. + -> T=0 Z=0 Y=0 X=30.5 (X decimal)",
            "12. CHS -> T=0 Z=0 Y=0 X=-30.5 (X decimal)",
            "13. 0 -> T=0 Z=0 Y=-30.5 X=0",
            "14. / -> T=0 Z=0 Y=-30.5 X=0 (ERREUR division par zero, pile inchangee)",
            "15. DROP -> T=0 Z=0 Y=0 X=-30.5 (X decimal)",
            "16. SWAP -> T=0 Z=0 Y=-30.5 X=0",
            "17. 9 -> T=0 Z=-30.5 Y=0 X=9",
            "18. - -> T=0 Z=0 Y=-30.5 X=-9",
            "19. CLR -> T=0 Z=0 Y=0 X=0",
            "20. 10 -> T=0 Z=0 Y=0 X=10",
            "21. 4 -> T=0 Z=0 Y=10 X=4",
            "22. / -> T=0 Z=0 Y=0 X=2.5 (X decimal)",
            "piege du ternaire : 1.0 est un Double");
            // EXPECTED-END

    static final List<String> API = List.of(
            "re:instanceof Integer \\w+ &&##instanceof avec variable et &&", "re:!\\(\\w+ instanceof \\w+ \\w+\\)##portee de flux !(x instanceof T v)", "else if", "yield ",
            "for (String ", "re:case \"\\+\", \"-\"##case a plusieurs valeurs", "(double)", "Number",
            // Crescendo : notions des chapitres 4 a 15, interdites au chapitre 3.
            "!StringBuilder", "!String.format", "!String.valueOf", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!.split(", "!.compareTo(", "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ",
            "!interface ", "!extends ", "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)",
            "!re:case [A-Z]\\w* \\w+ ->##pattern dans un case (preview en Java 17)", "!case null##case null (preview en Java 17)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Rpn", args, ARGS, EXPECTED, API);
    }
}
