package ch10_streams.drills.solutions;

import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;
import ch10_streams.drills.Library.Loan;

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
 * Corrige du drill 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.drills.exercises.Drill09_CollectorsBasicsApi.
 */
public class SolutionDrill09_CollectorsBasicsApi {

    public static List<String> titlesList() {
        // Collectors.toList() : la forme d'avant Java 16, liste modifiable en pratique.
        return Library.BOOKS.stream().map(Book::title).collect(Collectors.toList());
    }

    public static Set<String> genresSet() {
        // toSet retire les doublons, sans ordre garanti.
        return Library.BOOKS.stream().map(Book::genre).collect(Collectors.toSet());
    }

    public static TreeSet<String> authorsSorted() {
        // toCollection permet de choisir la collection : TreeSet = trie et sans doublon.
        return Library.BOOKS.stream().map(Book::author).collect(Collectors.toCollection(TreeSet::new));
    }

    public static List<String> lockedTitles() {
        // toUnmodifiableList (Java 10) : add lance UnsupportedOperationException.
        return Library.BOOKS.stream().map(Book::title).collect(Collectors.toUnmodifiableList());
    }

    public static Set<String> lockedGenres() {
        // toUnmodifiableSet (Java 10) : doublons retires, ensemble non modifiable.
        return Library.BOOKS.stream().map(Book::genre).collect(Collectors.toUnmodifiableSet());
    }

    public static String titlesGlued() {
        // joining() sans argument : aucun separateur.
        return Library.BOOKS.stream().map(Book::title).collect(Collectors.joining());
    }

    public static String titlesCsv() {
        // joining(sep) : le separateur est mis ENTRE les elements, pas a la fin.
        return Library.BOOKS.stream().map(Book::title).collect(Collectors.joining(", "));
    }

    public static String sfTitlesBracketed() {
        // joining(sep, prefixe, suffixe).
        return Library.BOOKS.stream()
                .filter(b -> b.genre().equals("SF"))
                .map(Book::title)
                .collect(Collectors.joining(", ", "[", "]"));
    }

    public static Long countBooks() {
        // counting() rend un Long (objet), pas un long.
        return Library.BOOKS.stream().collect(Collectors.counting());
    }

    public static Integer totalPages() {
        // summingInt rend un Integer.
        return Library.BOOKS.stream().collect(Collectors.summingInt(Book::pages));
    }

    public static Double totalPrice() {
        // summingDouble rend un Double.
        return Library.BOOKS.stream().collect(Collectors.summingDouble(Book::price));
    }

    public static Double averagePages() {
        // averagingInt rend un Double (0.0 si vide, pas d'Optional).
        return Library.BOOKS.stream().collect(Collectors.averagingInt(Book::pages));
    }

    public static Double averagePrice() {
        // averagingDouble rend un Double.
        return Library.BOOKS.stream().collect(Collectors.averagingDouble(Book::price));
    }

    public static Optional<Book> oldestBook() {
        // minBy rend un Optional (le stream peut etre vide).
        return Library.BOOKS.stream().collect(Collectors.minBy(Comparator.comparingInt(Book::year)));
    }

    public static Optional<Book> newestBook() {
        // maxBy rend un Optional.
        return Library.BOOKS.stream().collect(Collectors.maxBy(Comparator.comparingInt(Book::year)));
    }

    public static IntSummaryStatistics pageSummary() {
        // summarizingInt = summaryStatistics() version Collector.
        return Library.BOOKS.stream().collect(Collectors.summarizingInt(Book::pages));
    }

    public static DoubleSummaryStatistics priceSummary() {
        // summarizingDouble pour des double.
        return Library.BOOKS.stream().collect(Collectors.summarizingDouble(Book::price));
    }

    public static LongSummaryStatistics lateSummary() {
        // summarizingLong pour des long (ici daysLate est un int, elargi en long).
        return Library.LOANS.stream().collect(Collectors.summarizingLong(Loan::daysLate));
    }

    public static LinkedList<String> titlesLinkedList() {
        // toCollection(LinkedList::new) : le Supplier fabrique la collection.
        return Library.BOOKS.stream().map(Book::title).collect(Collectors.toCollection(LinkedList::new));
    }

    public static Double averageLateDays() {
        // averagingLong : moyenne de valeurs long, rendue en Double.
        return Library.LOANS.stream().collect(Collectors.averagingLong(Loan::daysLate));
    }

    public static Long totalLateDays() {
        // summingLong rend un Long.
        return Library.LOANS.stream().collect(Collectors.summingLong(Loan::daysLate));
    }
}
