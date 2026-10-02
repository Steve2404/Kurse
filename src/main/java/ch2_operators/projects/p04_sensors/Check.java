package ch2_operators.projects.p04_sensors;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Sensors, ou avec l'argument "solution".
 */
public class Check {

    static final String[] ARGS = {"22", "55", "80", "true"};

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "trame #1 (22 C, 55 %, batterie 80 %, signee) : valide | && 4 controle(s), & 4 | meme verdict true | un seul defaut climat false | alerte non",
            "trame #2 (70 C, 40 %, batterie 90 %, signee) : rejetee | && 1 controle(s), & 4 | meme verdict true | un seul defaut climat true | alerte oui",
            "trame #3 (25 C, 120 %, batterie 5 %, non signee) : rejetee | && 2 controle(s), & 4 | meme verdict true | un seul defaut climat true | alerte oui",
            "trame #4 (-30 C, 150 %, batterie 50 %, signee) : rejetee | && 1 controle(s), & 4 | meme verdict true | un seul defaut climat false | alerte non",
            "trame #5 (10 C, 10 %, batterie 11 %, non signee) : rejetee | && 4 controle(s), & 4 | meme verdict true | un seul defaut climat false | alerte oui",
            "increments : a = 12, b = 2, id = 5",
            "affectations : x = 6, y = 4, z = 36, affectation (vraie)",
            "references : f1 == f2 false, f1 == f3 true, f1 != f2 true",
            "instanceof : Integer true, Number true, texte Integer false, null Object false",
            "trames analysees : 5");
            // EXPECTED-END

    static final List<String> API = List.of(
            "3x&&", "||", "re:\\) & \\w+\\(##& (non court-circuit) entre deux appels", " ^ ",
            "instanceof", "++", "--", "re:\\w+ = \\w+ = ##affectation en chaine (x = y = 4)",
            "re:\\(\\w+ \\+= \\d+\\)##affectation dans une expression (x += 2)", "re:\\(\\w+ = (true|false)\\)##affectation dans une condition (flag = true)", " != ", " == ",
            // Crescendo : notions des chapitres 3 a 15, interdites au chapitre 2.
            "!if (", "!if(", "!else", "!for (", "!for(", "!while", "!switch", "!do {",
            "!->", "!StringBuilder", "!String.format", "!.formatted(", "!Math.", "!.length()", "!.substring(", "!.charAt(",
            "!.toUpperCase(", "!.toLowerCase(", "!.equals(", "!.repeat(", "!.strip", "!.trim(", "!.replace(", "!.indexOf(",
            "!re:new \\w+\\[##tableau cree par toi (chapitre 4)", "!List", "!Map", "!Set<", "!record ", "!enum ", "!interface ", "!extends ",
            "!implements ", "!catch", "!throw ", "!Locale", "!this(##appel this(...) (chapitre 6)", "!static {##bloc static (chapitre 6)", "!re:instanceof \\w+ \\w+##instanceof avec variable : pattern matching (chapitre 3)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Sensors", args, ARGS, EXPECTED, API);
    }
}
