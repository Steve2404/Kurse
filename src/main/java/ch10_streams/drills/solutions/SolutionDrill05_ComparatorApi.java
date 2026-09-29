package ch10_streams.drills.solutions;

import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Corrige du drill 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.drills.exercises.Drill05_ComparatorApi.
 */
public class SolutionDrill05_ComparatorApi {

    public static Comparator<Book> byTitle() {
        // comparing(extracteur) : compare selon l'ordre naturel de la cle (ici le titre).
        return Comparator.comparing(Book::title);
    }

    public static Comparator<Book> byTitleDesc() {
        // La version a 2 arguments donne le comparateur a utiliser POUR la cle.
        return Comparator.comparing(Book::title, Comparator.reverseOrder());
    }

    public static Comparator<Book> byPages() {
        // comparingInt evite le boxing des int.
        return Comparator.comparingInt(Book::pages);
    }

    public static Comparator<Book> byPriceDesc() {
        // reversed() inverse tout le comparateur (ici il n'y a qu'un critere).
        return Comparator.comparingDouble(Book::price).reversed();
    }

    public static Comparator<Book> byGenreThenTitle() {
        // thenComparing departage les egalites du 1er critere.
        return Comparator.comparing(Book::genre).thenComparing(Book::title);
    }

    public static Comparator<Book> byAuthorThenYear() {
        // thenComparingInt : version int de thenComparing.
        return Comparator.comparing(Book::author).thenComparingInt(Book::year);
    }

    public static Comparator<Book> byGenreThenPriceDesc() {
        // thenComparing(cle, comparateur) inverse SEULEMENT le prix.
        // Un reversed() final inverserait aussi les genres.
        return Comparator.comparing(Book::genre).thenComparing(Book::price, Comparator.reverseOrder());
    }

    public static Comparator<Book> byTagCountThenTitle() {
        // Avec une lambda, il faut parfois typer le parametre (Book b) pour aider l'inference.
        return Comparator.comparingInt((Book b) -> b.tags().size()).thenComparing(Book::title);
    }

    public static Comparator<Book> byYearLambda() {
        // Un Comparator est une interface fonctionnelle : une lambda (a, b) -> int suffit.
        // Integer.compare evite les debordements d'une soustraction a - b.
        return (a, b) -> Integer.compare(a.year(), b.year());
    }

    public static Comparator<String> caseInsensitive() {
        // Comparateur tout fait de la classe String.
        return String.CASE_INSENSITIVE_ORDER;
    }

    public static Comparator<String> natural() {
        // naturalOrder : utilise compareTo des elements (qui doivent etre Comparable).
        return Comparator.naturalOrder();
    }

    public static Comparator<String> reverse() {
        // reverseOrder : l'inverse de naturalOrder.
        return Comparator.reverseOrder();
    }

    public static Comparator<String> nullsFirstNatural() {
        // nullsFirst emballe un comparateur : les null d'abord, le reste selon lui.
        return Comparator.nullsFirst(Comparator.naturalOrder());
    }

    public static Comparator<Integer> nullsLastReverse() {
        // nullsLast(reverseOrder()) : grands d'abord, null a la fin.
        return Comparator.nullsLast(Comparator.reverseOrder());
    }

    public static Comparator<Map.Entry<String, Long>> entryByValueDescThenKey() {
        // Le type explicite <String, Long> est necessaire pour enchainer thenComparing ensuite.
        return Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                .thenComparing(Map.Entry.comparingByKey());
    }
}
