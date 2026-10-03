package ch13_concurrency.projects.p03_bank;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON BankLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "soldes [102097, 355497, -169497, 349086, -177365, 80182] ; identiques au sequentiel true",
            "conservation : total 600000 = 600000 true ; 20000 virements, frais 60000, plus gros virement 999",
            "interblocage evite : A abandonne, B reussit ; virement 2 -> 3 par tryTransfer true",
            "reentrance : getHoldCount 2, isHeldByCurrentThread true, isLocked apres 2 unlock false, unlock de trop IllegalMonitorStateException, equitable true",
            "volatile : boucle arretee apres 1000 tours ; getAndIncrement 1000 puis 1001, compareAndSet(1001, 0) true -> 0, updateAndGet 10");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.ACCOUNTS", "Data.TRANSFERS", "Data.transfer(", "Data.FEE",
            "new ReentrantLock()", ".lock()", ".unlock()", "public synchronized void",
            "synchronized (this)", "AtomicLong", ".addAndGet(", ".accumulateAndGet(",
            "Math.min(", ".tryLock(", "TimeUnit.MILLISECONDS", "CyclicBarrier",
            ".getHoldCount()", ".isHeldByCurrentThread()", ".isLocked()", "catch (IllegalMonitorStateException",
            "new ReentrantLock(true)", ".isFair()", "static volatile boolean", "AtomicInteger",
            ".incrementAndGet()", ".getAndIncrement()", ".compareAndSet(", ".updateAndGet(",
            "3xfinally",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "BankLab", args, EXPECTED, API);
    }
}
