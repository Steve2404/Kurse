package ch13_concurrency.projects.p07_crawler;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 7 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON CrawlerApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "termine true ; pages par niveau [1, 3, 9, 22, 28], decouvertes 80, telechargees 63",
            "pages cassees [p13 (404 p13), p26 (404 p26), p52 (404 p52), p65 (404 p65), p78 (404 p78)]",
            "index (mot=pages) : atome=13 carte=10 file=13 flux=12 java=13 module=12 pool=15 tache=13 thread=12 verrou=13",
            "mot le plus present : pool -> [p15, p18, p25, p28, p35]...",
            "rapport : 80 pages",
            "periodiques : au moins 5 battements true, au moins 3 sondages true, annulees true true",
            "planificateur arrete true");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.MAX_DEPTH", "Data.THREADS", "Data.START", "Data.links(",
            "Data.words(", "ConcurrentHashMap.newKeySet()", ".computeIfAbsent(", "AtomicInteger",
            ".invokeAll(", "catch (ExecutionException", "Callable<List<String>>", "Executors.newScheduledThreadPool(",
            ".schedule(", ".scheduleAtFixedRate(", ".scheduleWithFixedDelay(", "ScheduledFuture<",
            ".cancel(false)", "CountDownLatch", ".shutdown()", ".awaitTermination(",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "CrawlerApp", args, EXPECTED, API);
    }
}
