package ch14_io.drills.r06_kata;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall06, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 108 6",
            "D02 : capture",
            "D03 : true",
            "D04 : ligne 2 23 23");
            // EXPECTED-END

    static final List<String> API = List.of(
            "new Scanner(", ".hasNextInt()", "Reader r", "System.setOut(",
            "new PrintStream(", "System.console()", "Console", "Files.lines(",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall06", args, EXPECTED, API);
    }
}
