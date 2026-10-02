package ch11_exceptions.drills.r04_resources;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [ouvre a, ouvre b, corps, ferme b, ferme a]",
            "D02 : [ouvre a, ouvre b, ferme b, ferme a, catch corps [close b, close a], finally]",
            "D03 : [ouvre a, corps, ferme a, catch close a 0]",
            "D04 : [ouvre partagee, corps true, ferme partagee]",
            "D05 : [corps, ferme lambda]",
            "D06 : 1 a la main",
            "D07 : [ouvre v, type Door, ferme v]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "implements AutoCloseable", "public void close()", "try (Door a = new Door(", "try (shared; Door nothing = null)",
            "try (AutoCloseable lambda", "catch (Exception", ".getSuppressed()", ".addSuppressed(",
            "try (var door = new Door(",
            // Crescendo : notions des chapitres 12 a 15 (threads, E/S, JDBC) ou System.exit / printStackTrace, interdits au chapitre 11.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!CompletableFuture", "!Files.", "!Path.of",
            "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter", "!InputStream", "!OutputStream",
            "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall04", args, EXPECTED, API);
    }
}
