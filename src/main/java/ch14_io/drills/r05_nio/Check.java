package ch14_io.drills.r05_nio;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [alpha, beta, gamma, delta] 4",
            "D02 : [ALPHA, BETA, GAMMA, DELTA]",
            "D03 : alpha beta",
            "D04 : [src/data.txt, src/main] 5 [src/main/App.java]",
            "D05 : [., src]",
            "D06 : true false true 2026-01-01T00:00:00Z false true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Files.write(", "StandardOpenOption.APPEND", "Files.newBufferedWriter(", "Files.readAllLines(",
            "Files.lines(", "Files.newBufferedReader(", "Files.list(", "Files.walk(",
            "Files.find(", "Files.walk(box, 1)", "Files.setLastModifiedTime(", "Files.readAttributes(",
            "BasicFileAttributes", "Files.getAttribute(", "Files.isReadable(",
            // Crescendo : notions du chapitre 15 (JDBC) ou System.exit / printStackTrace, interdites au chapitre 14.
            "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, EXPECTED, API);
    }
}
