package ch13_concurrency.drills.solutions;

import ch13_concurrency.drills.Loans;
import ch13_concurrency.drills.Loans.Loan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Corrige du drill 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.drills.exercises.Drill03_ConcurrentCollections.
 */
public class SolutionDrill03_ConcurrentCollections {

    public static Map<String, Integer> countByTitle() throws InterruptedException {
        // merge est atomique cle par cle : le compte est exact meme avec 4 threads.
        Map<String, Integer> counts = new ConcurrentHashMap<>();
        onEveryLoan(loan -> counts.merge(loan.title(), 1, Integer::sum));
        return counts;
    }

    public static Set<String> members() throws InterruptedException {
        // newKeySet : le "ConcurrentHashSet" qui n'existe pas sous ce nom.
        Set<String> members = ConcurrentHashMap.newKeySet();
        onEveryLoan(loan -> members.add(loan.member()));
        return members;
    }

    public static String putIfAbsentTwice() {
        // putIfAbsent rend l'ANCIENNE valeur : null la 1re fois, 14 la 2e (et 99 n'est pas range).
        Map<String, Integer> map = new ConcurrentHashMap<>();
        Integer first = map.putIfAbsent("Dune", 14);
        Integer second = map.putIfAbsent("Dune", 99);
        return first + " " + second;
    }

    public static String snapshotLoop() {
        // L'iterateur est une PHOTO de la liste au debut : 3 tours, meme si on ajoute pendant le parcours.
        List<String> titles = new CopyOnWriteArrayList<>(List.of("Dune", "Fondation", "Hyperion"));
        int turns = 0;
        for (String t : titles) {
            titles.add(t + " (copie)");
            turns++;
        }
        return turns + " " + titles.size();
    }

    public static String queueOrder() {
        // Une file : poll retire la tete (Dune), peek regarde la nouvelle tete sans la retirer.
        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
        queue.offer("Dune");
        queue.offer("Fondation");
        queue.offer("Hyperion");
        queue.poll();
        return queue.peek();
    }

    public static String pollEmptyWithTimeout() throws InterruptedException {
        // poll(delai, unite) attend au plus le delai, puis rend null (take() attendrait pour toujours).
        return String.valueOf(new LinkedBlockingQueue<String>().poll(50, TimeUnit.MILLISECONDS));
    }

    public static Set<String> sortedTitles() {
        // La version concurrente de TreeSet : triee, sans doublon.
        Set<String> titles = new ConcurrentSkipListSet<>();
        for (Loan loan : Loans.LOANS) {
            titles.add(loan.title());
        }
        return titles;
    }

    public static int synchronizedAdds() throws InterruptedException {
        // Chaque add est synchronise : aucune perte ; seule l'ITERATION devrait etre protegee a la main.
        List<Integer> list = Collections.synchronizedList(new ArrayList<>());
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (int t = 0; t < 4; t++) {
            pool.submit(() -> {
                for (int i = 0; i < 1000; i++) {
                    list.add(i);
                }
            });
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
        return list.size();
    }

    private static void onEveryLoan(Consumer<Loan> action) throws InterruptedException {
        // Boite magique : un emprunt par tache, sur 4 threads, puis attendre la fin.
        ExecutorService pool = Executors.newFixedThreadPool(4);
        for (Loan loan : Loans.LOANS) {
            pool.submit(() -> action.accept(loan));
        }
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }
}
