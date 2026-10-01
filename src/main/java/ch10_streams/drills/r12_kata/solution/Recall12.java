package ch10_streams.drills.r12_kata.solution;

import ch10_streams.drills.Data;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * SOLUTION du drill de rappel 12 - kata mixte chronometre.
 */
public class Recall12 {

    record Book(String title, String author, String genre, int year, int pages, double price) {
        static Book parse(String line) {
            String[] p = line.split(";");
            return new Book(p[0], p[1], p[2], Integer.parseInt(p[3]), Integer.parseInt(p[4]), Double.parseDouble(p[5]));
        }
    }

    static final List<Book> BOOKS = Data.BOOKS.stream().map(Book::parse).toList();

    public static void main(String[] args) {
        System.out.println("D01 : " + BOOKS.stream()
                .collect(Collectors.groupingBy(Book::genre, Collectors.averagingDouble(Book::price)))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(e -> e.getKey() + String.format(Locale.US, " %.2f", e.getValue())).orElse("-"));

        System.out.println("D02 : " + BOOKS.stream()
                .collect(Collectors.toMap(Book::author, Book::pages, Integer::sum))
                .entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("-"));

        System.out.println("D03 : " + BOOKS.stream().collect(Collectors.groupingBy(b -> b.year() / 10 * 10, TreeMap::new,
                Collectors.mapping(Book::title, Collectors.joining("|")))).headMap(1960));

        System.out.println("D04 : " + (BOOKS.stream().map(Book::author).distinct().count() < BOOKS.size()));

        System.out.println("D05 : " + BOOKS.stream().sorted(Comparator.comparingDouble(Book::price).reversed()).skip(1).findFirst()
                .map(Book::title).orElse("-"));

        IntSummaryStatistics lengths = BOOKS.stream().mapToInt(b -> b.title().length()).summaryStatistics();
        System.out.println("D06 : " + lengths.getMin() + " " + lengths.getMax() + " " + lengths.getSum());

        Map<Boolean, Double> sf = BOOKS.stream().collect(Collectors.partitioningBy(b -> b.genre().equals("SF"), Collectors.averagingInt(Book::pages)));
        System.out.println("D07 : " + String.format(Locale.US, "SF %.1f / autres %.1f", sf.get(true), sf.get(false)));

        System.out.println("D08 : " + Data.WORDS.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
                .limit(2).map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining(", ")));

        System.out.println("D09 : " + BOOKS.stream().filter(b -> b.genre().equals("SF")).min(Comparator.comparingInt(Book::year))
                .map(Book::author).map(a -> a.charAt(0)).orElse('?'));

        // Somme cumulee par indices : chaque position somme son prefixe.
        int[] n = Data.NUMBERS;
        System.out.println("D10 : " + IntStream.range(0, n.length).map(i -> IntStream.rangeClosed(0, i).map(j -> n[j]).sum())
                .boxed().toList());
    }
}
