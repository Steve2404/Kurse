package ch6_classdesign.projects.p03_payroll;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Payroll, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "#1 Alice [manager de 2] 6300.00",
            "  #2 Bruno [manager de 3] 4950.00",
            "    #4 David [ingenieur +10h] 4050.00",
            "    #5 Emma [ingenieur +0h] 4000.00",
            "    #6 Farid [stagiaire] 900.00",
            "  #3 Chloe [manager de 2] 5000.00",
            "    #7 Gina [ingenieur +5h] 4025.00",
            "    #8 Hugo [manager de 2] 4500.00",
            "      #9 Ines [ingenieur +20h] 4100.00",
            "      #10 Jules [stagiaire] 1000.00",
            "masse salariale 38825.00 ; equipe Bruno 13900.00 ; equipe Chloe 18625.00",
            "manager commun : David+Ines=Alice Ines+Jules=Hugo Gina+Hugo=Chloe Emma+Farid=Bruno",
            "plus longue chaine : Alice > Chloe > Hugo > Ines (4 niveaux)",
            "champ masque : manager / employe / manager/employe",
            "static masquee : encadrement / employe ; redefinie : 4500.00 = 4500.00",
            "prive redeclare : hausse de Hugo 126.00 (3 %, pas 10 %) ; badge final #8 Hugo");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.STAFF", "Data.PAIRS", "abstract class Employee", "3xextends Employee",
            "super.pay()", "2xstatic String category()", "2xString type = ", "super.type",
            "public final String badge()", "2xprivate int rate()", "instanceof Manager", "@Override",
            "public abstract String role()",
            // Crescendo : notions des chapitres 7 a 15, interdites au chapitre 6.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!sealed ", "!permits ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat",
            "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()", "!.now()", "!re:\\((?:[A-Z]\\w*)\\)\\s*[\\w(]##cast d objet (chapitre 7)", "!re:(?m)^[ \\t]+(?:(?:public|protected|private|static|final|abstract)\\s+)*class \\w+##classe imbriquee (chapitre 7)",
            "!re:new \\w+\\([^;]*\\)\\s*\\{##classe anonyme (chapitre 7)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Payroll", args, EXPECTED, API);
    }
}
