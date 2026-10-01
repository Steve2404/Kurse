package ch13_concurrency.drills.exercises;

import ch13_concurrency.ExerciseChecker;
import ch13_concurrency.drills.Loans;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * DRILL 04 - Les streams paralleles
 * =================================
 *
 * Mode d'emploi : voir Drill01_ThreadsAndExecutors. Donnees : Loans.LOANS.
 *
 *
 * -- Les TODO (forme visee entre crochets) --
 *
 * TODO 1 : parallelTotal()       [parallelStream + mapToInt + sum] -> 134.
 * TODO 2 : becomesParallel()     [Stream.parallel() + isParallel()] partir de LOANS.stream() -> true.
 * TODO 3 : firstLate()           [findFirst en parallele] le titre du PREMIER emprunt de plus de 14 jours -> Hyperion.
 * TODO 4 : anyLate()             [findAny] le titre d'UN emprunt de plus de 14 jours -> Hyperion ou Dune.
 * TODO 5 : titlesInOrder()       [forEachOrdered] les titres, en parallele, ajoutes a une liste dans l'ordre de LOANS.
 * TODO 6 : reduceThreeArgs()     [reduce(identite, accumulateur, combinateur)] total des jours depuis les Loan -> 134.
 * TODO 7 : loansPerMember()      [groupingByConcurrent + counting] -> {ana=3, bob=3, cid=2, dan=2, eve=2}, une ConcurrentMap.
 * TODO 8 : daysPerTitle()        [toConcurrentMap avec fusion] titre -> total des jours -> {Dune=54, Fondation=21, Hyperion=32, Solaris=27}.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   collection.parallelStream() ; stream.parallel() / sequential() ; isParallel()
 *   findAny (n'importe lequel) / findFirst (ordre de rencontre) ; forEach (desordre) / forEachOrdered (ordre)
 *   reduce(identite, acc, comb) : identite NEUTRE, acc et comb associatifs, sans etat
 *   collect(Collectors.toList()) garde l'ordre ; groupingByConcurrent / toConcurrentMap -> ConcurrentMap
 *   unordered() peut accelerer ; une lambda qui modifie un etat partage = bug en parallele
 * ---------------------------------------------------------------------
 */
public class Drill04_ParallelStreams {

    public static int parallelTotal() {
        throw new UnsupportedOperationException("TODO 1 : implementer parallelTotal()");
    }

    public static boolean becomesParallel() {
        throw new UnsupportedOperationException("TODO 2 : implementer becomesParallel()");
    }

    public static String firstLate() {
        throw new UnsupportedOperationException("TODO 3 : implementer firstLate()");
    }

    public static String anyLate() {
        throw new UnsupportedOperationException("TODO 4 : implementer anyLate()");
    }

    public static List<String> titlesInOrder() {
        throw new UnsupportedOperationException("TODO 5 : implementer titlesInOrder()");
    }

    public static int reduceThreeArgs() {
        throw new UnsupportedOperationException("TODO 6 : implementer reduceThreeArgs()");
    }

    public static Map<String, Long> loansPerMember() {
        throw new UnsupportedOperationException("TODO 7 : implementer loansPerMember()");
    }

    public static Map<String, Integer> daysPerTitle() {
        throw new UnsupportedOperationException("TODO 8 : implementer daysPerTitle()");
    }

    public static void main(String[] args) {
        List<String> expectedTitles = Loans.LOANS.stream().map(Loans.Loan::title).collect(Collectors.toList());
        ExerciseChecker.check("1  parallelTotal == 134", parallelTotal() == 134);
        ExerciseChecker.check("2  becomesParallel == true", becomesParallel());
        boolean ordered = true;
        boolean first = true;
        for (int run = 0; run < 5; run++) {
            ordered &= expectedTitles.equals(titlesInOrder());
            first &= "Hyperion".equals(firstLate());
        }
        ExerciseChecker.check("3  firstLate == Hyperion, 5 fois de suite", first);
        ExerciseChecker.check("4  anyLate est Hyperion ou Dune", Set.of("Hyperion", "Dune").contains(anyLate()));
        ExerciseChecker.check("5  titlesInOrder dans l'ordre de LOANS, 5 fois de suite", ordered);
        ExerciseChecker.check("6  reduceThreeArgs == 134", reduceThreeArgs() == 134);
        Map<String, Long> perMember = loansPerMember();
        ExerciseChecker.check("7  loansPerMember == {ana=3, bob=3, cid=2, dan=2, eve=2}, ConcurrentMap",
                perMember instanceof ConcurrentMap && Map.of("ana", 3L, "bob", 3L, "cid", 2L, "dan", 2L, "eve", 2L).equals(perMember));
        Map<String, Integer> perTitle = daysPerTitle();
        ExerciseChecker.check("8  daysPerTitle == {Dune=54, Fondation=21, Hyperion=32, Solaris=27}, ConcurrentMap",
                perTitle instanceof ConcurrentMap && Map.of("Dune", 54, "Fondation", 21, "Hyperion", 32, "Solaris", 27).equals(perTitle));

        ExerciseChecker.summary();
    }
}
