package ch13_concurrency.projects.p07_crawler.solution;

import ch13_concurrency.projects.p07_crawler.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * SOLUTION du projet 7 (capstone) - le robot d'indexation, puis le planificateur.
 */
public class CrawlerApp {

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(Data.THREADS);
        Crawler crawler = new Crawler(pool);
        List<Integer> perLevel;
        try {
            perLevel = crawler.crawl(Data.START, Data.MAX_DEPTH);
        } finally {
            pool.shutdown();
        }
        System.out.println("termine " + pool.awaitTermination(10, TimeUnit.SECONDS) + " ; pages par niveau " + perLevel + ", decouvertes " + crawler.visited()
                + ", telechargees " + crawler.fetched());
        System.out.println("pages cassees " + crawler.broken());
        Map<String, Set<String>> index = new TreeMap<>();
        crawler.index().forEach((w, pages) -> index.put(w, new TreeSet<>(pages)));
        StringBuilder sizes = new StringBuilder();
        index.forEach((w, pages) -> sizes.append(' ').append(w).append('=').append(pages.size()));
        System.out.println("index (mot=pages) :" + sizes);
        List<String> top = new ArrayList<>(index.keySet());
        top.sort((a, b) -> index.get(b).size() != index.get(a).size() ? index.get(b).size() - index.get(a).size() : a.compareTo(b));
        System.out.println("mot le plus present : " + top.get(0) + " -> " + new ArrayList<>(index.get(top.get(0))).subList(0, 5) + "...");

        // Le planificateur : une tache differee (avec resultat), une tache periodique a cadence fixe, une a delai fixe.
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        try {
            ScheduledFuture<String> report = scheduler.schedule(() -> "rapport : " + crawler.visited() + " pages", 20, TimeUnit.MILLISECONDS);
            AtomicInteger beats = new AtomicInteger();
            CountDownLatch fiveBeats = new CountDownLatch(5);
            ScheduledFuture<?> heartbeat = scheduler.scheduleAtFixedRate(() -> {
                beats.incrementAndGet();
                fiveBeats.countDown();
            }, 0, 5, TimeUnit.MILLISECONDS);
            AtomicInteger polls = new AtomicInteger();
            CountDownLatch threePolls = new CountDownLatch(3);
            ScheduledFuture<?> poller = scheduler.scheduleWithFixedDelay(() -> {
                polls.incrementAndGet();
                threePolls.countDown();
            }, 0, 5, TimeUnit.MILLISECONDS);
            System.out.println(report.get());
            fiveBeats.await();
            threePolls.await();
            heartbeat.cancel(false);                                 // une tache periodique ne s'arrete que si on l'annule
            poller.cancel(false);
            System.out.println("periodiques : au moins 5 battements " + (beats.get() >= 5) + ", au moins 3 sondages " + (polls.get() >= 3) + ", annulees "
                    + heartbeat.isCancelled() + " " + poller.isCancelled());
        } finally {
            scheduler.shutdown();
        }
        System.out.println("planificateur arrete " + scheduler.awaitTermination(5, TimeUnit.SECONDS));
    }
}
