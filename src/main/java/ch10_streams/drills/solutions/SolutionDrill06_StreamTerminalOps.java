package ch10_streams.drills.solutions;

import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Corrige du drill 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.drills.exercises.Drill06_StreamTerminalOps.
 */
public class SolutionDrill06_StreamTerminalOps {

    public static long countOfGenre(String genre) {
        // count() rend un long.
        return Library.BOOKS.stream().filter(b -> b.genre().equals(genre)).count();
    }

    public static Optional<String> cheapestTitle() {
        // Sur un Stream<T>, min et max EXIGENT un Comparator (contrairement a IntStream).
        return Library.BOOKS.stream().min(Comparator.comparingDouble(Book::price)).map(Book::title);
    }

    public static Optional<String> thickestTitle() {
        // max rend un Optional : le stream pourrait etre vide.
        return Library.BOOKS.stream().max(Comparator.comparingInt(Book::pages)).map(Book::title);
    }

    public static Optional<String> firstAfterYear(int year) {
        // findFirst respecte l'ordre de la source.
        return Library.BOOKS.stream().filter(b -> b.year() > year).findFirst().map(Book::title);
    }

    public static Optional<Book> anyAfterYear(int year) {
        // findAny peut rendre n'importe quel element qui correspond (utile en parallele).
        return Library.BOOKS.stream().filter(b -> b.year() > year).findAny();
    }

    public static boolean hasAuthor(String author) {
        // anyMatch s'arrete au 1er element qui correspond.
        return Library.BOOKS.stream().anyMatch(b -> b.author().equals(author));
    }

    public static boolean allHavePagesOver(int n) {
        // allMatch s'arrete au 1er element qui NE correspond PAS.
        return Library.BOOKS.stream().allMatch(b -> b.pages() > n);
    }

    public static boolean noneCostsMoreThan(double price) {
        // noneMatch s'arrete au 1er element qui correspond.
        return Library.BOOKS.stream().noneMatch(b -> b.price() > price);
    }

    public static boolean allPoesieAreCheap() {
        // Piege : allMatch sur un stream VIDE rend true (aucun contre-exemple).
        return Library.BOOKS.stream().filter(b -> b.genre().equals("Poesie")).allMatch(b -> b.price() < 1);
    }

    public static void collectTitles(List<String> out) {
        // forEach : operation terminale sans resultat, pour un effet de bord.
        Library.BOOKS.stream().map(Book::title).forEach(out::add);
    }

    public static void collectSortedTitles(List<String> out) {
        // forEachOrdered garantit l'ordre du stream, meme en parallele.
        Library.BOOKS.stream().map(Book::title).sorted().forEachOrdered(out::add);
    }

    public static Object[] isbnArray() {
        // toArray() sans argument rend un Object[].
        return Library.BOOKS.stream().map(Book::isbn).toArray();
    }

    public static String[] titleArray() {
        // toArray(String[]::new) rend un tableau du bon type.
        return Library.BOOKS.stream().map(Book::title).toArray(String[]::new);
    }

    public static List<String> immutableTitles() {
        // toList() (Java 16) rend une liste NON modifiable.
        return Library.BOOKS.stream().map(Book::title).toList();
    }

    public static List<String> mutableTitles() {
        // Collectors.toList() rend en pratique une ArrayList modifiable (non garanti par la doc).
        return Library.BOOKS.stream().map(Book::title).collect(Collectors.toList());
    }

    public static List<String> firstTwoTitles() {
        // iterator() est une operation terminale : on parcourt ensuite a la main avec next().
        Iterator<String> it = Library.BOOKS.stream().map(Book::title).iterator();
        List<String> out = new ArrayList<>();
        out.add(it.next());
        out.add(it.next());
        return out;
    }

    public static long lateLoansCount() {
        // filter puis count : le nombre d'elements qui passent le filtre.
        return Library.LOANS.stream().filter(l -> l.daysLate() > 0).count();
    }
}
