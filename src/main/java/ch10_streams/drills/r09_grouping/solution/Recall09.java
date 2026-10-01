package ch10_streams.drills.r09_grouping.solution;

import ch10_streams.drills.Data;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.filtering;
import static java.util.stream.Collectors.flatMapping;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.groupingByConcurrent;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.maxBy;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.teeing;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

/**
 * SOLUTION du drill de rappel 9 - groupingBy, partitioningBy, collecteurs en aval, teeing.
 */
public class Recall09 {

    record Book(String title, String author, String genre, int year, int pages, double price) {
        static Book parse(String line) {
            String[] p = line.split(";");
            return new Book(p[0], p[1], p[2], Integer.parseInt(p[3]), Integer.parseInt(p[4]), Double.parseDouble(p[5]));
        }
    }

    static final List<Book> BOOKS = Data.BOOKS.stream().map(Book::parse).toList();

    public static void main(String[] args) {
        // groupingBy(f) seul : HashMap de List ; on recopie dans une TreeMap pour un affichage stable.
        Map<String, List<Book>> plain = BOOKS.stream().collect(groupingBy(Book::genre));
        System.out.println("D01 : " + plain.getClass().getSimpleName() + " " + plain.get("SF").size() + " " + new TreeMap<>(plain).keySet());

        System.out.println("D02 : " + BOOKS.stream().collect(groupingBy(Book::genre, TreeMap::new, counting())));

        Map<String, Set<String>> authorsByGenre = BOOKS.stream().collect(groupingBy(Book::genre, TreeMap::new, mapping(Book::author, toCollection(TreeSet::new))));
        System.out.println("D03 : " + authorsByGenre);

        // partitioningBy : toujours les cles false ET true, meme vides.
        Map<Boolean, Long> before1950 = BOOKS.stream().collect(partitioningBy(b -> b.year() < 1950, counting()));
        Map<Boolean, List<Book>> nothing = BOOKS.stream().collect(partitioningBy(b -> b.pages() > 10_000));
        System.out.println("D04 : " + before1950 + " " + nothing.keySet() + " " + nothing.get(true).size());

        // filtering en aval garde le groupe meme vide (Fantasy=0).
        System.out.println("D05 : " + BOOKS.stream().collect(groupingBy(Book::genre, TreeMap::new, filtering(b -> b.price() > 9, counting()))));

        System.out.println("D06 : " + BOOKS.stream().collect(groupingBy(Book::author, TreeMap::new,
                flatMapping(b -> Arrays.stream(b.title().split(" ")), toCollection(TreeSet::new)))).get("Tolkien"));

        Map<String, String> thickest = BOOKS.stream().collect(groupingBy(Book::genre, TreeMap::new,
                collectingAndThen(maxBy(Comparator.comparingInt(Book::pages)), o -> o.map(Book::title).orElse("-"))));
        System.out.println("D07 : " + thickest);

        Map<String, Integer> pages = BOOKS.stream().collect(groupingBy(Book::genre, TreeMap::new, reducing(0, Book::pages, Integer::sum)));
        Map<String, Optional<Book>> oldest = BOOKS.stream().collect(groupingBy(Book::genre, TreeMap::new,
                reducing((a, b) -> a.year() <= b.year() ? a : b)));
        System.out.println("D08 : " + pages + " " + oldest.get("SF").map(Book::title).orElse("-"));

        // Groupement a deux niveaux.
        System.out.println("D09 : " + BOOKS.stream().collect(groupingBy(Book::genre, TreeMap::new,
                groupingBy(b -> b.year() / 100 * 100, TreeMap::new, counting()))));

        System.out.println("D10 : " + BOOKS.stream().collect(teeing(summingInt(Book::pages), counting(), (p, n) -> p / n + " pages en moyenne sur " + n)));

        ConcurrentMap<String, Long> concurrent = BOOKS.parallelStream().collect(groupingByConcurrent(Book::author, counting()));
        System.out.println("D11 : " + new TreeMap<>(concurrent) + " " + BOOKS.stream().map(Book::genre).collect(toSet()).size());

        System.out.println("D12 : " + BOOKS.stream().collect(partitioningBy(b -> b.genre().equals("SF"),
                mapping(Book::title, joining("/")))).get(false));
    }
}
