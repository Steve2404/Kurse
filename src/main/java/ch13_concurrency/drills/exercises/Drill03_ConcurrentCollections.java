package ch13_concurrency.drills.exercises;

import ch13_concurrency.ExerciseChecker;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * DRILL 03 - Les collections concurrentes
 * =======================================
 *
 * Mode d'emploi : voir Drill01_ThreadsAndExecutors. Donnees : Loans.LOANS.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1 : countByTitle()        [ConcurrentHashMap.merge depuis plusieurs threads] -> {Dune=4, Fondation=2, Hyperion=3, Solaris=3}.
 * TODO 2 : members()             [ConcurrentHashMap.newKeySet()] les membres distincts, ajoutes depuis plusieurs threads -> 5.
 * TODO 3 : putIfAbsentTwice()    [putIfAbsent rend l'ancienne valeur] putIfAbsent("Dune", 14) puis putIfAbsent("Dune", 99) : "retour1 retour2" -> "null 14".
 * TODO 4 : snapshotLoop()        [CopyOnWriteArrayList] parcourir [Dune, Fondation, Hyperion] en ajoutant un titre a chaque tour : "tours taille" -> "3 6".
 * TODO 5 : queueOrder()          [ConcurrentLinkedQueue offer / poll / peek] offer Dune, Fondation, Hyperion ; poll ; puis rendre peek() -> Fondation.
 * TODO 6 : pollEmptyWithTimeout() [LinkedBlockingQueue.poll(delai, unite)] sur une file vide, avec 50 ms -> null (rendre String.valueOf).
 * TODO 7 : sortedTitles()        [ConcurrentSkipListSet] les titres de LOANS, tries et sans doublon -> [Dune, Fondation, Hyperion, Solaris].
 * TODO 8 : synchronizedAdds()    [Collections.synchronizedList] 4 threads ajoutent 1000 elements chacun ; rendre la taille -> 4000.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   ConcurrentHashMap (merge, compute, putIfAbsent... atomiques ; pas de cle ni de valeur null)
 *   ConcurrentHashMap.newKeySet()  -> un Set concurrent
 *   CopyOnWriteArrayList / CopyOnWriteArraySet : copie a chaque ecriture, iterateur = photo (jamais de CME)
 *   ConcurrentLinkedQueue / ConcurrentLinkedDeque : offer, poll, peek (sans blocage)
 *   BlockingQueue (LinkedBlockingQueue, ArrayBlockingQueue) : put / take bloquent ; offer(e, d, u) / poll(d, u) attendent au plus d
 *   ConcurrentSkipListMap / ConcurrentSkipListSet : versions triees (comme TreeMap / TreeSet)
 *   Collections.synchronizedList / Set / Map(...) : chaque appel est synchronise, MAIS l'iteration doit l'etre a la main
 * ---------------------------------------------------------------------
 */
public class Drill03_ConcurrentCollections {

    public static Map<String, Integer> countByTitle() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 1 : implementer countByTitle()");
    }

    public static Set<String> members() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 2 : implementer members()");
    }

    public static String putIfAbsentTwice() {
        throw new UnsupportedOperationException("TODO 3 : implementer putIfAbsentTwice()");
    }

    public static String snapshotLoop() {
        throw new UnsupportedOperationException("TODO 4 : implementer snapshotLoop()");
    }

    public static String queueOrder() {
        throw new UnsupportedOperationException("TODO 5 : implementer queueOrder()");
    }

    public static String pollEmptyWithTimeout() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 6 : implementer pollEmptyWithTimeout()");
    }

    public static Set<String> sortedTitles() {
        throw new UnsupportedOperationException("TODO 7 : implementer sortedTitles()");
    }

    public static int synchronizedAdds() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 8 : implementer synchronizedAdds()");
    }

    public static void main(String[] args) throws Exception {
        boolean exact = true;
        for (int run = 0; run < 3; run++) {
            exact &= Map.of("Dune", 4, "Fondation", 2, "Hyperion", 3, "Solaris", 3).equals(countByTitle());
        }
        ExerciseChecker.check("1  countByTitle exact, trois fois de suite", exact);
        ExerciseChecker.check("2  members == [ana, bob, cid, dan, eve]", Set.of("ana", "bob", "cid", "dan", "eve").equals(members()));
        ExerciseChecker.check("3  putIfAbsentTwice == null 14", "null 14".equals(putIfAbsentTwice()));
        ExerciseChecker.check("4  snapshotLoop == 3 6", "3 6".equals(snapshotLoop()));
        ExerciseChecker.check("5  queueOrder == Fondation", "Fondation".equals(queueOrder()));
        ExerciseChecker.check("6  pollEmptyWithTimeout == null", "null".equals(pollEmptyWithTimeout()));
        ExerciseChecker.check("7  sortedTitles == [Dune, Fondation, Hyperion, Solaris]",
                List.of("Dune", "Fondation", "Hyperion", "Solaris").toString().equals(sortedTitles().toString()));
        ExerciseChecker.check("8  synchronizedAdds == 4000", synchronizedAdds() == 4000);

        ExerciseChecker.summary();
    }
}
