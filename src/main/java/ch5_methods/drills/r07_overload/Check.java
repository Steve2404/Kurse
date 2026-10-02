package ch5_methods.drills.r07_overload;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 7 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall07, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : int int int long Integer int... int...",
            "D02 : double double double Object Object",
            "D03 : Long Number Long Number",
            "D04 : String Object String Object",
            "D05 : int[] int[] Object Object",
            "D06 : short char float short float");
            // EXPECTED-END

    static final List<String> API = List.of(
            "4xstatic String f(", "2xstatic String g(", "h(Long ", "h(Number ",
            "k(String ", "m(int[] ", "p(short ", "p(char ",
            "p(float ", "k(null)", "m(null)",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!extends ##extends / heritage (chapitre 6)", "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)",
            "!super##super (chapitre 6)", "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator",
            "!.chars()", "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall07", args, EXPECTED, API);
    }
}
