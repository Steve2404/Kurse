package ch10_streams.drills.r08_collectors.solution;

import ch10_streams.drills.Data;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
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
        String npe;
        try {
            Stream.of("a", null).collect(Collectors.toUnmodifiableList());
            npe = "rien";
        } catch (NullPointerException e) {
            npe = e.getClass().getSimpleName();
        }
        System.out.println("D02 : " + Data.WORDS.stream().collect(Collectors.toUnmodifiableSet()).size() + " " + npe);

        System.out.println("D03 : " + Stream.of("a", "b").collect(Collectors.joining()) + " "
                + Stream.of("a", "b").collect(Collectors.joining("-")) + " "
                + Stream.of("a", "b").collect(Collectors.joining(", ", "[", "]")) + " "
                + Stream.<String>empty().collect(Collectors.joining(", ", "[", "]")));

        // counting -> Long ; averagingInt -> Double ; summingInt -> Integer.
        Long count = BOOKS.stream().collect(Collectors.counting());
        Double avgPages = BOOKS.stream().collect(Collectors.averagingInt(Book::pages));
        Integer totalPages = BOOKS.stream().collect(Collectors.summingInt(Book::pages));
        Double totalPrice = BOOKS.stream().collect(Collectors.summingDouble(Book::price));
        System.out.println("D04 : " + count + " " + avgPages + " " + totalPages + " " + String.format(Locale.US, "%.2f", totalPrice));

        IntSummaryStatistics years = BOOKS.stream().collect(Collectors.summarizingInt(Book::year));
        DoubleSummaryStatistics prices = BOOKS.stream().collect(Collectors.summarizingDouble(Book::price));
        LongSummaryStatistics pageStats = BOOKS.stream().collect(Collectors.summarizingLong(Book::pages));
        System.out.println("D05 : " + years.getMin() + "-" + years.getMax() + " " + prices.getMax() + " " + pageStats.getSum());

        System.out.println("D06 : " + BOOKS.stream().collect(Collectors.minBy(Comparator.comparingInt(Book::year))).map(Book::title).orElse("-")
                + " " + BOOKS.stream().collect(Collectors.maxBy(Comparator.comparingDouble(Book::price))).map(Book::title).orElse("-"));

        // toMap a 2 arguments : une cle en double -> IllegalStateException.
        String duplicate;
        try {
            BOOKS.stream().collect(Collectors.toMap(Book::author, Book::title));
            duplicate = "rien";
        } catch (IllegalStateException e) {
            duplicate = e.getClass().getSimpleName();
        }
        Map<String, String> byAuthor = BOOKS.stream()
                .collect(Collectors.toMap(Book::author, Book::title, (a, b) -> a + "+" + b, TreeMap::new));
        Map<String, Integer> pages = BOOKS.stream().collect(Collectors.toMap(Book::title, Book::pages));
        System.out.println("D07 : " + duplicate + " " + byAuthor.get("Zola") + " " + pages.get("Dune") + " " + byAuthor.keySet());

        System.out.println("D08 : " + String.format(Locale.US, "%.3f", BOOKS.stream().collect(Collectors.averagingDouble(Book::price)))
                + " " + BOOKS.stream().collect(Collectors.averagingLong(Book::year)));
    }
}
