package ch13_concurrency.drills.solutions;

import ch13_concurrency.drills.Loans;
import ch13_concurrency.drills.Loans.Loan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch13_concurrency.drills.exercises.Drill04_ParallelStreams.
 */
public class SolutionDrill04_ParallelStreams {

    public static int parallelTotal() {
        // Une somme est associative : le resultat ne depend pas du decoupage.
        return Loans.LOANS.parallelStream().mapToInt(Loan::days).sum();
    }

    public static boolean becomesParallel() {
        // parallel() est une operation intermediaire : elle change le mode de TOUT le pipeline.
        return Loans.LOANS.stream().parallel().isParallel();
    }

    public static String firstLate() {
        // findFirst garde l'ordre de rencontre : toujours le 1er en retard de la liste.
        return Loans.LOANS.parallelStream().filter(l -> l.days() > 14).map(Loan::title).findFirst().orElse("aucun");
    }

    public static String anyLate() {
        // findAny peut rendre n'importe quel element qui passe le filtre (plus rapide en parallele).
        return Loans.LOANS.parallelStream().filter(l -> l.days() > 14).map(Loan::title).findAny().orElse("aucun");
    }

    public static List<String> titlesInOrder() {
        // forEachOrdered livre dans l'ordre et un par un : l'ArrayList n'est jamais touchee par deux threads a la fois.
        List<String> titles = new ArrayList<>();
        Loans.LOANS.parallelStream().map(Loan::title).forEachOrdered(titles::add);
        return titles;
    }

    public static int reduceThreeArgs() {
        // Types differents (Loan -> int) : l'accumulateur ajoute un emprunt, le combinateur additionne deux totaux.
        return Loans.LOANS.parallelStream().reduce(0, (acc, l) -> acc + l.days(), Integer::sum);
    }

    public static Map<String, Long> loansPerMember() {
        // groupingByConcurrent : une seule ConcurrentMap remplie par tous les threads.
        return Loans.LOANS.parallelStream().collect(Collectors.groupingByConcurrent(Loan::member, Collectors.counting()));
    }

    public static Map<String, Integer> daysPerTitle() {
        // toConcurrentMap a besoin d'une fonction de fusion des que deux elements ont la meme cle.
        return Loans.LOANS.parallelStream().collect(Collectors.toConcurrentMap(Loan::title, Loan::days, Integer::sum));
    }
}
