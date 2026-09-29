package ch10_streams.drills.exercises;

import ch10_streams.ExerciseChecker;
import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * DRILL 09 - Les Collectors "simples" (sans Map) (projet bibliotheque)
 * ====================================================================
 *
 * Mode d'emploi : voir Drill01_OptionalApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 * Chaque TODO = UN appel stream().collect(Collectors.xxx(...)).
 * (Astuce : import static java.util.stream.Collectors.*; raccourcit
 * l'ecriture - a toi de voir.)
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : titlesList()           [toList]
 * TODO 2  : genresSet()            [toSet]
 * TODO 3  : authorsSorted()        [toCollection(TreeSet::new)]
 * TODO 4  : lockedTitles()         [toUnmodifiableList]
 * TODO 5  : lockedGenres()         [toUnmodifiableSet]
 * TODO 6  : titlesGlued()          [joining()] tous les titres colles sans separateur.
 * TODO 7  : titlesCsv()            [joining(", ")]
 * TODO 8  : sfTitlesBracketed()    [joining(", ", "[", "]")] titres SF seulement.
 * TODO 9  : countBooks()           [counting] -> Long 8.
 * TODO 10 : totalPages()           [summingInt]
 * TODO 11 : totalPrice()           [summingDouble]
 * TODO 12 : averagePages()         [averagingInt] -> 254.625.
 * TODO 13 : averagePrice()         [averagingDouble] -> 8.0.
 * TODO 14 : oldestBook()           [minBy] -> Le Hobbit.
 * TODO 15 : newestBook()           [maxBy] -> Neuromancien.
 * TODO 16 : pageSummary()          [summarizingInt]
 * TODO 17 : priceSummary()         [summarizingDouble]
 * TODO 18 : lateSummary()          [summarizingLong] sur LOANS.daysLate.
 * TODO 19 : titlesLinkedList()     [toCollection(LinkedList::new)]
 * TODO 20 : averageLateDays()      [averagingLong] sur LOANS -> 2.375.
 * TODO 21 : totalLateDays()        [summingLong] sur LOANS -> 19.
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   toList() toSet() toCollection(Supplier) toUnmodifiableList() toUnmodifiableSet()
 *   joining()  joining(sep)  joining(sep, prefixe, suffixe)
 *   counting() -> Long
 *   summingInt / summingLong / summingDouble(ToXxxFunction) -> Integer / Long / Double
 *   averagingInt / averagingLong / averagingDouble(...) -> Double (0.0 si vide !)
 *   minBy(Comparator) / maxBy(Comparator) -> Optional<T>
 *   summarizingInt / summarizingLong / summarizingDouble -> XxxSummaryStatistics
 * ---------------------------------------------------------------------
 */
public class Drill09_CollectorsBasicsApi {

    public static List<String> titlesList() {
        throw new UnsupportedOperationException("TODO 1 : implementer titlesList()");
    }

    public static Set<String> genresSet() {
        throw new UnsupportedOperationException("TODO 2 : implementer genresSet()");
    }

    public static TreeSet<String> authorsSorted() {
        throw new UnsupportedOperationException("TODO 3 : implementer authorsSorted()");
    }

    public static List<String> lockedTitles() {
        throw new UnsupportedOperationException("TODO 4 : implementer lockedTitles()");
    }

    public static Set<String> lockedGenres() {
        throw new UnsupportedOperationException("TODO 5 : implementer lockedGenres()");
    }

    public static String titlesGlued() {
        throw new UnsupportedOperationException("TODO 6 : implementer titlesGlued()");
    }

    public static String titlesCsv() {
        throw new UnsupportedOperationException("TODO 7 : implementer titlesCsv()");
    }

    public static String sfTitlesBracketed() {
        throw new UnsupportedOperationException("TODO 8 : implementer sfTitlesBracketed()");
    }

    public static Long countBooks() {
        throw new UnsupportedOperationException("TODO 9 : implementer countBooks()");
    }

    public static Integer totalPages() {
        throw new UnsupportedOperationException("TODO 10 : implementer totalPages()");
    }

    public static Double totalPrice() {
        throw new UnsupportedOperationException("TODO 11 : implementer totalPrice()");
    }

    public static Double averagePages() {
        throw new UnsupportedOperationException("TODO 12 : implementer averagePages()");
    }

    public static Double averagePrice() {
        throw new UnsupportedOperationException("TODO 13 : implementer averagePrice()");
    }

    public static Optional<Book> oldestBook() {
        throw new UnsupportedOperationException("TODO 14 : implementer oldestBook()");
    }

    public static Optional<Book> newestBook() {
        throw new UnsupportedOperationException("TODO 15 : implementer newestBook()");
    }

    public static IntSummaryStatistics pageSummary() {
        throw new UnsupportedOperationException("TODO 16 : implementer pageSummary()");
    }

    public static DoubleSummaryStatistics priceSummary() {
        throw new UnsupportedOperationException("TODO 17 : implementer priceSummary()");
    }

    public static LongSummaryStatistics lateSummary() {
        throw new UnsupportedOperationException("TODO 18 : implementer lateSummary()");
    }

    public static LinkedList<String> titlesLinkedList() {
        throw new UnsupportedOperationException("TODO 19 : implementer titlesLinkedList()");
    }

    public static Double averageLateDays() {
        throw new UnsupportedOperationException("TODO 20 : implementer averageLateDays()");
    }

    public static Long totalLateDays() {
        throw new UnsupportedOperationException("TODO 21 : implementer totalLateDays()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  titlesList : 8 titres", titlesList().size() == 8 && titlesList().get(4).equals("1984"));
        ExerciseChecker.check("2  genresSet == {SF, Conte, Dystopie, Fantasy}",
                genresSet().equals(Set.of("SF", "Conte", "Dystopie", "Fantasy")));
        ExerciseChecker.check("3  authorsSorted",
                authorsSorted().toString().equals("[Asimov, Gibson, Herbert, Orwell, Saint-Exupery, Tolkien]"));
        ExerciseChecker.check("4  lockedTitles refuse add", refusesAdd(lockedTitles()));
        ExerciseChecker.check("5  lockedGenres refuse add et a 4 elements", refusesAdd(lockedGenres()) && lockedGenres().size() == 4);
        ExerciseChecker.check("6  titlesGlued commence par DuneFondation et fait 83 caracteres",
                titlesGlued().startsWith("DuneFondationLes Robots") && titlesGlued().length() == 83);
        ExerciseChecker.check("7  titlesCsv commence par 'Dune, Fondation, ' et finit par ', Le Hobbit'",
                titlesCsv().startsWith("Dune, Fondation, ") && titlesCsv().endsWith(", Le Hobbit"));
        ExerciseChecker.check("8  sfTitlesBracketed == [Dune, Fondation, Les Robots, Neuromancien]",
                sfTitlesBracketed().equals("[Dune, Fondation, Les Robots, Neuromancien]"));
        ExerciseChecker.check("9  countBooks == 8L", countBooks() == 8L);
        ExerciseChecker.check("10 totalPages == 2037", totalPages() == 2037);
        ExerciseChecker.check("11 totalPrice == 64.0", totalPrice() == 64.0);
        ExerciseChecker.check("12 averagePages == 254.625", averagePages() == 254.625);
        ExerciseChecker.check("13 averagePrice == 8.0", averagePrice() == 8.0);
        ExerciseChecker.check("14 oldestBook == Le Hobbit", oldestBook().map(Book::title).equals(Optional.of("Le Hobbit")));
        ExerciseChecker.check("15 newestBook == Neuromancien", newestBook().map(Book::title).equals(Optional.of("Neuromancien")));
        ExerciseChecker.check("16 pageSummary : min 96, max 412", pageSummary().getMin() == 96 && pageSummary().getMax() == 412);
        ExerciseChecker.check("17 priceSummary : somme 64.0, min 5.5", priceSummary().getSum() == 64.0 && priceSummary().getMin() == 5.5);
        ExerciseChecker.check("18 lateSummary : somme 19, max 10, count 8",
                lateSummary().getSum() == 19 && lateSummary().getMax() == 10 && lateSummary().getCount() == 8);
        ExerciseChecker.check("19 titlesLinkedList : 8 titres, getLast == Le Hobbit",
                titlesLinkedList().size() == 8 && titlesLinkedList().getLast().equals("Le Hobbit"));
        ExerciseChecker.check("20 averageLateDays == 2.375", averageLateDays() == 2.375);
        ExerciseChecker.check("21 totalLateDays == 19L", totalLateDays() == 19L);

        ExerciseChecker.summary();
    }

    private static boolean refusesAdd(java.util.Collection<String> c) {
        try {
            c.add("X");
            return false;
        } catch (UnsupportedOperationException e) {
            return true;
        }
    }
}
