package ch13_concurrency.drills.solutions;

import ch13_concurrency.drills.Loans;
import ch13_concurrency.drills.Loans.Loan;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Corrige du drill 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.drills.exercises.Drill05_MixedKata.
 */
public class SolutionDrill05_MixedKata {

    public static int totalFees(ExecutorService executor) throws Exception {
        // Une Callable par emprunt, invokeAll attend tout, puis on additionne les Future.
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (Loan loan : Loans.LOANS) {
            tasks.add(() -> loan.days() * 10);
        }
        int total = 0;
        for (Future<Integer> f : executor.invokeAll(tasks)) {
            total += f.get();
        }
        return total;
    }

    public static String busiestMember() {
        // groupingByConcurrent + summingInt en parallele, puis le max des totaux.
        Map<String, Integer> totals = Loans.LOANS.parallelStream()
                .collect(Collectors.groupingByConcurrent(Loan::member, Collectors.summingInt(Loan::days)));
        return totals.entrySet().stream().max(Map.Entry.comparingByValue()).orElseThrow().getKey();
    }

    public static int lateCount() throws InterruptedException {
        // Un compteur atomique partage entre les taches : aucune increment perdue.
        AtomicInteger late = new AtomicInteger();
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (Loan loan : Loans.LOANS) {
            pool.submit(() -> {
                if (loan.days() > 14) {
                    late.incrementAndGet();
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
        return late.get();
    }

    public static List<String> titleRanking() {
        // Compter en parallele (ConcurrentHashMap.merge), puis trier : nombre decroissant, puis nom.
        Map<String, Integer> counts = new ConcurrentHashMap<>();
        Loans.LOANS.parallelStream().forEach(l -> counts.merge(l.title(), 1, Integer::sum));
        List<String> titles = new ArrayList<>(counts.keySet());
        titles.sort(Comparator.comparing((String t) -> counts.get(t)).reversed().thenComparing(Comparator.naturalOrder()));
        return titles;
    }

    public static boolean allProcessed() throws InterruptedException {
        // Un CountDownLatch de 12 : chaque tache le decremente ; await(delai) rend true quand il atteint 0.
        CountDownLatch done = new CountDownLatch(Loans.LOANS.size());
        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            for (Loan loan : Loans.LOANS) {
                pool.submit(done::countDown);
            }
            return done.await(5, TimeUnit.SECONDS);
        } finally {
            pool.shutdown();
        }
    }

    public static double averageDays() {
        // average() rend un OptionalDouble (vide si aucun element).
        return Loans.LOANS.parallelStream().mapToInt(Loan::days).average().orElse(0);
    }

    public static Map<String, String> firstTitlePerMember() {
        // toMap avec fusion (a, b) -> a : en parallele, les morceaux sont recolles DANS L'ORDRE, donc "a" est bien le premier.
        return Loans.LOANS.parallelStream()
                .collect(Collectors.toMap(Loan::member, Loan::title, (a, b) -> a, TreeMap::new));
    }

    public static int cancelPending() throws InterruptedException {
        // shutdownNow interrompt la tache en cours et rend celles de la file jamais demarrees.
        ExecutorService single = Executors.newSingleThreadExecutor();
        CountDownLatch started = new CountDownLatch(1);
        single.submit(() -> {
            started.countDown();
            try {
                new CountDownLatch(1).await();
            } catch (InterruptedException e) {
                // fin demandee par shutdownNow
            }
        });
        for (int i = 0; i < 11; i++) {
            single.submit(() -> { });
        }
        started.await();
        int pending = single.shutdownNow().size();
        single.awaitTermination(5, TimeUnit.SECONDS);
        return pending;
    }
}
