package ch13_concurrency.drills.r02_executors;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall02, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [a, b, c]",
            "D02 : 42 true",
            "D03 : 6 seule reussite",
            "D04 : TimeoutException true true",
            "D05 : UnsupportedOperationException non",
            "D06 : null",
            "D07 : true true true RejectedExecutionException",
            "D08 : vite true true true",
            "D09 : plus tard true true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Executors.newSingleThreadExecutor()", "Executors.newFixedThreadPool(", ".execute(", ".submit(",
            ".invokeAll(", ".invokeAny(", "catch (TimeoutException", ".cancel(true)",
            "catch (ExecutionException", ".awaitTermination(", "catch (RejectedExecutionException", "Executors.newSingleThreadScheduledExecutor()",
            ".schedule(", ".scheduleAtFixedRate(", "CountDownLatch", "Executors.newCachedThreadPool()",
            "100, TimeUnit.MILLISECONDS)",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall02", args, EXPECTED, API);
    }
}
