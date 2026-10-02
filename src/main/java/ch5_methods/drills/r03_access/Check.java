package ch5_methods.drills.r03_access;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall03, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : stylo 7",
            "D02 : 42 6 article",
            "D03 : 8 8",
            "D04 : article special",
            "D05 : crayon 42 7 article",
            "D06 : stylo 7 42 9 article");
            // EXPECTED-END

    static final List<String> API = List.of(
            "public String name", "protected int stock", "re:(?m)^\\s+int code##champ package-private", "private int secret",
            "protected static String", "extends Item", "re:package [\\w.]+\\.shop;##paquet shop", "re:package [\\w.]+\\.club;##paquet club",
            // Crescendo : notions des chapitres 6 a 15, interdites au chapitre 5.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!record ", "!enum ", "!interface ",
            "!implements ", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!this(##appel this(...) (chapitre 6)", "!super##super (chapitre 6)",
            "!abstract ##abstract (chapitre 6)", "!@Override##@Override (chapitre 6)", "!re:(?m)^\\s*(?:(?:public|protected|private)\\s+)?[A-Z]\\w*\\s*\\([^;{)]*\\)\\s*\\{##constructeur ecrit par toi (chapitre 6)", "!.stream(", "!.lines()", "!Optional", "!Comparator", "!.chars()",
            "!LocalDate.now()", "!LocalDateTime.now()", "!Instant.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall03", args, EXPECTED, API);
    }
}
