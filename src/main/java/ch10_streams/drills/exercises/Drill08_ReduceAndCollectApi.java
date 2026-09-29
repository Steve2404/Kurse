package ch10_streams.drills.exercises;

import ch10_streams.ExerciseChecker;
import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * DRILL 08 - reduce (3 formes), collect a 3 arguments, Collectors.reducing (projet bibliotheque)
 * ==============================================================================================
 *
 * Mode d'emploi : voir Drill01_OptionalApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : totalPagesReduce()      [reduce(identity, BinaryOperator)] -> 2037.
 * TODO 2  : longestTitle()          [reduce(BinaryOperator) -> Optional] a egalite : le 1er.
 * TODO 3  : totalPriceReduce3()     [reduce(identity, BiFunction, BinaryOperator)] directement
 *           sur Stream<Book> (sans map) -> 64.0.
 * TODO 4  : initials()              [collect(Supplier, BiConsumer, BiConsumer) + StringBuilder]
 *           1re lettre de chaque titre -> "DFLL1LNL".
 * TODO 5  : titlesArrayList()       [collect(ArrayList::new, ArrayList::add, ArrayList::addAll)].
 * TODO 6  : totalPagesReducing()    [Collectors.reducing(identity, mapper, op)] -> 2037.
 * TODO 7  : latestYear()            [Collectors.reducing(op) -> Optional] -> Optional[1984].
 * TODO 8  : totalPriceReducing()    [Collectors.reducing(identity, op)] apres map -> 64.0.
 * TODO 9  : pagesByGenre()          [reducing EN AVAL de groupingBy] TreeMap genre -> pages.
 * TODO 10 : cheapestByReduce()      [reduce(BinaryOperator.minBy(cmp))] titre du moins cher.
 * TODO 11 : countViaReduce()        [map vers 1L + reduce] refaire count() a la main -> 8.
 * TODO 12 : tagCountsAsText()       [reduce 3 arguments, resultat String] concatene le
 *           nombre de tags de chaque livre -> "22122212".
 * TODO 13 : lateDaysByMember()      [collect 3 arguments dans une TreeMap + merge]
 *           -> {M1=0, M2=8, M3=11}.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Optional<T> reduce(BinaryOperator<T>)
 *   T reduce(T identity, BinaryOperator<T>)
 *   <U> U reduce(U identity, BiFunction<U, ? super T, U> acc, BinaryOperator<U> combiner)
 *   <R> R collect(Supplier<R>, BiConsumer<R, ? super T>, BiConsumer<R, R>)
 *   Collectors.reducing(BinaryOperator<T>)                   -> Optional<T>
 *   Collectors.reducing(T identity, BinaryOperator<T>)       -> T
 *   Collectors.reducing(U identity, Function<T,U>, BinaryOperator<U>) -> U
 *   BinaryOperator.minBy(cmp) / maxBy(cmp)
 *   map.merge(cle, valeur, Integer::sum)
 * ---------------------------------------------------------------------
 */
public class Drill08_ReduceAndCollectApi {

    public static int totalPagesReduce() {
        throw new UnsupportedOperationException("TODO 1 : implementer totalPagesReduce()");
    }

    public static Optional<String> longestTitle() {
        throw new UnsupportedOperationException("TODO 2 : implementer longestTitle()");
    }

    public static double totalPriceReduce3() {
        throw new UnsupportedOperationException("TODO 3 : implementer totalPriceReduce3()");
    }

    public static String initials() {
        throw new UnsupportedOperationException("TODO 4 : implementer initials()");
    }

    public static ArrayList<String> titlesArrayList() {
        throw new UnsupportedOperationException("TODO 5 : implementer titlesArrayList()");
    }

    public static int totalPagesReducing() {
        throw new UnsupportedOperationException("TODO 6 : implementer totalPagesReducing()");
    }

    public static Optional<Integer> latestYear() {
        throw new UnsupportedOperationException("TODO 7 : implementer latestYear()");
    }

    public static double totalPriceReducing() {
        throw new UnsupportedOperationException("TODO 8 : implementer totalPriceReducing()");
    }

    public static TreeMap<String, Integer> pagesByGenre() {
        throw new UnsupportedOperationException("TODO 9 : implementer pagesByGenre()");
    }

    public static Optional<String> cheapestByReduce() {
        throw new UnsupportedOperationException("TODO 10 : implementer cheapestByReduce()");
    }

    public static long countViaReduce() {
        throw new UnsupportedOperationException("TODO 11 : implementer countViaReduce()");
    }

    public static String tagCountsAsText() {
        throw new UnsupportedOperationException("TODO 12 : implementer tagCountsAsText()");
    }

    public static TreeMap<String, Integer> lateDaysByMember() {
        throw new UnsupportedOperationException("TODO 13 : implementer lateDaysByMember()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  totalPagesReduce == 2037", totalPagesReduce() == 2037);
        ExerciseChecker.check("2  longestTitle == La Ferme des animaux", longestTitle().equals(Optional.of("La Ferme des animaux")));
        ExerciseChecker.check("3  totalPriceReduce3 == 64.0", totalPriceReduce3() == 64.0);
        ExerciseChecker.check("4  initials == DFLL1LNL", initials().equals("DFLL1LNL"));
        ArrayList<String> list = titlesArrayList();
        ExerciseChecker.check("5  titlesArrayList : 8 titres, Dune en premier", list.size() == 8 && list.get(0).equals("Dune"));
        ExerciseChecker.check("6  totalPagesReducing == 2037", totalPagesReducing() == 2037);
        ExerciseChecker.check("7  latestYear == Optional[1984]", latestYear().equals(Optional.of(1984)));
        ExerciseChecker.check("8  totalPriceReducing == 64.0", totalPriceReducing() == 64.0);
        ExerciseChecker.check("9  pagesByGenre == {Conte=96, Dystopie=440, Fantasy=310, SF=1191}",
                pagesByGenre().toString().equals("{Conte=96, Dystopie=440, Fantasy=310, SF=1191}"));
        ExerciseChecker.check("10 cheapestByReduce == La Ferme des animaux",
                cheapestByReduce().equals(Optional.of("La Ferme des animaux")));
        ExerciseChecker.check("11 countViaReduce == 8", countViaReduce() == 8L);
        ExerciseChecker.check("12 tagCountsAsText == 22122212", tagCountsAsText().equals("22122212"));
        ExerciseChecker.check("13 lateDaysByMember == {M1=0, M2=8, M3=11}",
                lateDaysByMember().toString().equals("{M1=0, M2=8, M3=11}"));

        ExerciseChecker.summary();
    }
}
