package ch10_streams.drills.r08_collectors.solution;

import ch10_streams.drills.Data;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * SOLUTION du drill de rappel 8 - les collecteurs simples.
 */
public class Recall08 {

    record Book(String title, String author, String genre, int year, int pages, double price) {
        static Book parse(String line) {
            String[] p = line.split(";");
            return new Book(p[0], p[1], p[2], Integer.parseInt(p[3]), Integer.parseInt(p[4]), Double.parseDouble(p[5]));
        }
    }

    static final List<Book> BOOKS = Data.BOOKS.stream().map(Book::parse).toList();

    public static void main(String[] args) {
        Set<String> authors = BOOKS.stream().map(Book::author).collect(Collectors.toSet());
        TreeSet<String> sortedAuthors = BOOKS.stream().map(Book::author).collect(Collectors.toCollection(TreeSet::new));
        System.out.println("D01 : " + BOOKS.stream().map(Book::author).collect(Collectors.toList()).size() + " " + authors.size()
                + " " + sortedAuthors.first() + ".." + sortedAuthors.last());

        // toUnmodifiableXxx refuse null (NullPointerException) ; toUnmodifiableSet elimine les doublons.
        // toUnmodifiableSet elimine les doublons ; les toUnmodifiableXxx refusent null (NullPointerException).
        System.out.println("D02 : " + Data.WORDS.stream().collect(Collectors.toUnmodifiableSet()).size()
                + " " + Data.WORDS.stream().collect(Collectors.toUnmodifiableList()).size());

        System.out.println("D03 : " + Stream.of("a", "b").collect(Collectors.joining()) + " "
                + Stream.of("a", "b").collect(Collectors.joining("-")) + " "
                + Stream.of("a", "b").collect(Collectors.joining(", ", "[", "]")) + " "
                + Stream.<String>empty().collect(Collectors.joining(", ", "[", "]")));

        // counting -> Long ; averagingInt -> Double ; summingInt -> Integer.
        Long count = BOOKS.stream().collect(Collectors.counting());
        Double avgPages = BOOKS.stream().collect(Collectors.averagingInt(Book::pages));
        Integer totalPages = BOOKS.stream().collect(Collectors.summingInt(Book::pages));
        Double totalPrice = BOOKS.stream().collect(Collectors.summingDouble(Book::price));
        System.out.println("D04 : " + count + " " + avgPages + " " + totalPages + " " + Math.round(totalPrice * 100) / 100.0);

        IntSummaryStatistics years = BOOKS.stream().collect(Collectors.summarizingInt(Book::year));
        DoubleSummaryStatistics prices = BOOKS.stream().collect(Collectors.summarizingDouble(Book::price));
        LongSummaryStatistics pageStats = BOOKS.stream().collect(Collectors.summarizingLong(Book::pages));
        System.out.println("D05 : " + years.getMin() + "-" + years.getMax() + " " + prices.getMax() + " " + pageStats.getSum());

        System.out.println("D06 : " + BOOKS.stream().collect(Collectors.minBy(Comparator.comparingInt(Book::year))).map(Book::title).orElse("-")
                + " " + BOOKS.stream().collect(Collectors.maxBy(Comparator.comparingDouble(Book::price))).map(Book::title).orElse("-"));

        // toMap a 2 arguments sur une cle en double (auteur) lancerait IllegalStateException -> fonction de fusion.
        Map<String, String> byAuthor = BOOKS.stream()
                .collect(Collectors.toMap(Book::author, Book::title, (a, b) -> a + "+" + b, TreeMap::new));
        Map<String, Integer> pages = BOOKS.stream().collect(Collectors.toMap(Book::title, Book::pages));
        System.out.println("D07 : " + byAuthor.get("Zola") + " " + pages.get("Dune") + " " + byAuthor.keySet());

        System.out.println("D08 : " + Math.round(BOOKS.stream().collect(Collectors.averagingDouble(Book::price)) * 1000) / 1000.0
                + " " + BOOKS.stream().collect(Collectors.averagingLong(Book::year)));
    }
}
