package ch13_concurrency.projects.p02_primes;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON PrimeLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "premiers dans [1000000, 3000000) par segment : 17971 17686 17453 17325 17148 16991 16921 16823",
            "total 138318, premier 1000003, dernier 2999999, plus grand ecart 148",
            "invokeAll : 138318 (identique true)",
            "invokeAny : miroir C",
            "get(50 ms) : TimeoutException, cancel true, isCancelled true, isDone true",
            "tache en echec : ExecutionException <- ArithmeticException: / by zero",
            "submit(Runnable).get() = null",
            "pool : isShutdown true, awaitTermination true, isTerminated true",
            "soumission apres shutdown : RejectedExecutionException",
            "shutdownNow : 3 taches jamais lancees, journal [bloquante, bloquante interrompue]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.LOW", "Data.HIGH", "Data.SEGMENTS", "Data.THREADS",
            "Data.MIRRORS", "implements Callable<Segment>", "record Segment(", "Executors.newFixedThreadPool(",
            "Executors.newSingleThreadExecutor()", ".submit(", "Future<Segment>", ".get()",
            ".invokeAll(", ".invokeAny(", ".get(50, TimeUnit.MILLISECONDS)", "catch (TimeoutException",
            ".cancel(true)", ".isCancelled()", ".isDone()", "catch (ExecutionException",
            ".shutdown()", ".isShutdown()", ".awaitTermination(", ".isTerminated()",
            "catch (RejectedExecutionException", ".execute(", ".shutdownNow()", "CountDownLatch",
            "finally",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "PrimeLab", args, EXPECTED, API);
    }
}
