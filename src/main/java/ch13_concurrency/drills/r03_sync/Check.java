package ch13_concurrency.drills.r03_sync;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall03, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : 40000 40000",
            "D02 : 6 6 7 17 17 true false 50 50",
            "D03 : 16000 false true",
            "D04 : 20000 false false true 1",
            "D05 : 2 false true true",
            "D06 : 4 3 false");
            // EXPECTED-END

    static final List<String> API = List.of(
            "static synchronized void", "synchronized (lock)", "AtomicInteger", ".incrementAndGet()",
            ".getAndIncrement()", ".addAndGet(", ".getAndSet(", ".compareAndSet(",
            ".updateAndGet(", ".accumulateAndGet(", "AtomicLong", "AtomicBoolean",
            "new ReentrantLock()", ".tryLock()", ".tryLock(10", ".getHoldCount()",
            "ReentrantReadWriteLock", ".readLock()", ".writeLock()", ".getReadLockCount()",
            "new CyclicBarrier(3",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall03", args, EXPECTED, API);
    }
}
