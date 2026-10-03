package ch13_concurrency.drills.r07_bonus;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 7 (bonus) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall07, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 42 12",
            "D02 : profil de id-7",
            "D03 : -1 | erreur NumberFormatException | [exception] CompletionException",
            "D04 : 55 true",
            "D05 : 500000500000 true",
            "D06 : main autre true",
            "D07 : true false 1 3 | 1 0");
            // EXPECTED-END

    static final List<String> API = List.of(
            "CompletableFuture.supplyAsync(", ".thenApply(", ".thenCombine(", ".thenCompose(",
            ".exceptionally(", ".handle(", ".whenComplete(", "catch (CompletionException",
            "CompletableFuture.allOf(", "ForkJoinPool.commonPool()", "extends RecursiveTask<Long>", ".fork()",
            "ThreadLocal.withInitial(", "new Semaphore(3)", ".tryAcquire(2)", ".availablePermits()",
            "new CountDownLatch(2)", ".getCount()",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall07", args, EXPECTED, API);
    }
}
