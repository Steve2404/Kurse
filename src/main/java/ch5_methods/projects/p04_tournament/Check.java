package ch5_methods.projects.p04_tournament;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Tournament, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "echange : primitifs a=1 b=2, tableau [2, 1]",
            "references : Lions! Ours, reassigne [1, 2, 3], modifie [10, 2, 3]",
            "immuables : Lions 5, resultat ignore 5, resultat garde 6",
            "cache Integer : 127 true, 128 false, equals true, Long.equals(5) false, 5L == 5 true, compare true",
            "unboxing : total 7, AB, Integer.valueOf(\"42\") + 1 = 43, Double 5.0",
            "permutations par echanges : ABC ACB BAC BCA CBA CAB (6)",
            "ordre lexicographique : ABCD ABDC ACBD ACDB ADBC ADCB ... DCBA (24 au total)",
            "rang de CADB : par enumeration 14, par calcul 14, le tableau est reste sur DCBA",
            "J1 : Lions 2-0 Requins, Ours 1-1 Tigres, Aigles 3-2 Loups",
            "J2 : Lions 3-0 Tigres, Requins 1-0 Loups, Ours 2-0 Aigles",
            "J3 : Lions 0-0 Loups, Tigres 1-1 Aigles, Requins 2-2 Ours",
            "J4 : Lions 1-0 Aigles, Loups 1-2 Ours, Tigres 2-2 Requins",
            "J5 : Lions 2-0 Ours, Aigles 3-2 Requins, Loups 2-0 Tigres",
            "classement : 1.Lions 13pts(+8) 2.Ours 8pts(+1) 3.Aigles 7pts(-1) 4.Requins 5pts(-2) 5.Loups 4pts(-1) 6.Tigres 3pts(-5)");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.TEAMS", "Data.WORD", "Data.TARGET", "Integer ",
            "Long ", "Character ", "re:static void \\w+\\(int \\w+, int \\w+\\)##echange de deux int (sans effet)", "re:static void \\w+\\(int\\[\\] \\w+, int \\w+, int \\w+\\)##echange dans un tableau",
            "re:static void \\w+\\(StringBuilder##methode qui recoit un StringBuilder", "re:static void \\w+\\(String \\w+, Integer##methode qui recoit String et Integer", "System.arraycopy(", ".compareTo(",
            "do {",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Tournament", args, EXPECTED, API);
    }
}
