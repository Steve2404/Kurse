package ch13_concurrency.drills.exercises;

import ch13_concurrency.ExerciseChecker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * DRILL 05 - Kata melange : tout le chapitre 13 sans indice de forme
 * ==================================================================
 *
 * Mode d'emploi : voir Drill01_ThreadsAndExecutors. Ici, PAS de crochet :
 * a toi de choisir l'outil (thread, executor, atomique, verrou, collection
 * concurrente, stream parallele). Tout doit etre JUSTE a chaque execution.
 *
 *
 * -- Les TODO --
 *
 * TODO 1 : totalFees(executor)        les frais = 10 x jours, calcules par une tache par emprunt ; rendre la somme -> 1340.
 * TODO 2 : busiestMember()            le membre au plus grand total de jours, calcule en parallele -> bob (47).
 * TODO 3 : lateCount()                le nombre d'emprunts de plus de 14 jours, compte depuis plusieurs threads -> 2.
 * TODO 4 : titleRanking()             les titres du plus emprunte au moins emprunte, egalites par nom -> [Dune, Hyperion, Solaris, Fondation].
 * TODO 5 : allProcessed()             12 taches (une par emprunt) sur un pool de 3 ; attendre qu'elles aient TOUTES signale leur fin, au plus 5 s ; rendre true si c'est le cas.
 * TODO 6 : averageDays()              la moyenne des jours, en parallele -> 134 / 12.
 * TODO 7 : firstTitlePerMember()      membre -> titre de son PREMIER emprunt (ordre de LOANS), en parallele, cles triees.
 * TODO 8 : cancelPending()            un executor a 1 thread recoit une tache qui bloque, puis 11 autres ; une fois la 1re demarree, tout arreter ; rendre le nombre de taches jamais demarrees -> 11.
 */
public class Drill05_MixedKata {

    public static int totalFees(ExecutorService executor) throws Exception {
        throw new UnsupportedOperationException("TODO 1 : implementer totalFees()");
    }

    public static String busiestMember() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 2 : implementer busiestMember()");
    }

    public static int lateCount() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 3 : implementer lateCount()");
    }

    public static List<String> titleRanking() {
        throw new UnsupportedOperationException("TODO 4 : implementer titleRanking()");
    }

    public static boolean allProcessed() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 5 : implementer allProcessed()");
    }

    public static double averageDays() {
        throw new UnsupportedOperationException("TODO 6 : implementer averageDays()");
    }

    public static Map<String, String> firstTitlePerMember() {
        throw new UnsupportedOperationException("TODO 7 : implementer firstTitlePerMember()");
    }

    public static int cancelPending() throws InterruptedException {
        throw new UnsupportedOperationException("TODO 8 : implementer cancelPending()");
    }

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(4);
        try {
            ExerciseChecker.check("1  totalFees == 1340", totalFees(pool) == 1340);
        } finally {
            pool.shutdownNow();
        }
        ExerciseChecker.check("2  busiestMember == bob", "bob".equals(busiestMember()));
        ExerciseChecker.check("3  lateCount == 2", lateCount() == 2);
        ExerciseChecker.check("4  titleRanking == [Dune, Hyperion, Solaris, Fondation]",
                List.of("Dune", "Hyperion", "Solaris", "Fondation").equals(titleRanking()));
        ExerciseChecker.check("5  allProcessed == true", allProcessed());
        ExerciseChecker.check("6  averageDays == 134 / 12", Math.abs(averageDays() - 134.0 / 12) < 1e-9);
        ExerciseChecker.check("7  firstTitlePerMember == {ana=Dune, bob=Fondation, cid=Dune, dan=Dune, eve=Hyperion}",
                "{ana=Dune, bob=Fondation, cid=Dune, dan=Dune, eve=Hyperion}".equals(String.valueOf(firstTitlePerMember())));
        ExerciseChecker.check("8  cancelPending == 11", cancelPending() == 11);

        ExerciseChecker.summary();
    }
}
