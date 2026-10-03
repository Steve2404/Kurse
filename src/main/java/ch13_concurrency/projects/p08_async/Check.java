package ch13_concurrency.projects.p08_async;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 8 (bonus) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON AsyncLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "devis : Rome 465 EUR = 439 CHF",
            "devis : Lisbonne 350 EUR = 330 CHF",
            "devis : Atlantis indisponible (aucun vol pour Atlantis)",
            "devis : Oslo 639 EUR = 603 CHF",
            "erreurs : handle erreur IllegalStateException ; join CompletionException <- IllegalStateException ; get ExecutionException",
            "delais : valeur par defaut, orTimeout TimeoutException ; anyOf cache ; chaine [recu 42, fini] ; complete a la main isDone true",
            "ThreadLocal : total des increments 800, valeur de main 42 puis apres remove 0",
            "Semaphore(2) : au plus 2 a la fois true, permis disponibles 2 ; CountDownLatch 6 -> 0, tryAcquire(3) false",
            "Fork/Join : sous-tableau maximal 5604, total 1703 ; Kadane sequentiel 5604 identique true",
            "RecursiveAction : min -50, max 50 ; parallelisme du pool 4");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.TRIPS", "Data.FLIGHTS", "Data.HOTELS", "Data.SIZE",
            "Data.delta(", "CompletableFuture.supplyAsync(", ".thenCombine(", ".thenApply(",
            ".thenCompose(", ".exceptionally(", "CompletableFuture.allOf(", ".handle(",
            "catch (CompletionException", "catch (ExecutionException", ".completeOnTimeout(", ".orTimeout(",
            "CompletableFuture.anyOf(", "CompletableFuture.completedFuture(", ".thenAccept(", ".thenRun(",
            ".complete(", "ThreadLocal.withInitial(", ".remove()", "new Semaphore(2)",
            ".acquire()", ".release()", ".availablePermits()", ".tryAcquire(",
            "new CountDownLatch(", ".countDown()", ".getCount()", "extends RecursiveTask<",
            "extends RecursiveAction", ".fork()", ".join()", "invokeAll(new ClampAction(",
            "new ForkJoinPool(4)", ".invoke(", ".getParallelism()",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "AsyncLab", args, EXPECTED, API);
    }
}
