package ch10_streams.drills.solutions;

import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;
import ch10_streams.drills.Library.Loan;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Corrige du drill 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.drills.exercises.Drill04_StreamIntermediateOps.
 */
public class SolutionDrill04_StreamIntermediateOps {

    public static List<String> titlesOfGenre(String genre) {
        // filter garde les livres du genre, map les transforme en titres.
        return Library.BOOKS.stream().filter(b -> b.genre().equals(genre)).map(Book::title).toList();
    }

    public static List<String> authorsInOrder() {
        // distinct garde la 1re apparition et respecte l'ordre de la source.
        return Library.BOOKS.stream().map(Book::author).distinct().toList();
    }

    public static List<String> titlesSorted() {
        // Ordre naturel des String : chiffres < majuscules < minuscules, et "Le " < "Les".
        return Library.BOOKS.stream().map(Book::title).sorted().toList();
    }

    public static List<String> titlesByYearDesc() {
        // sorted(Comparator) : reversed() inverse l'ordre croissant des annees.
        return Library.BOOKS.stream()
                .sorted(Comparator.comparingInt(Book::year).reversed())
                .map(Book::title)
                .toList();
    }

    public static List<String> allTags() {
        // flatMap aplatit les listes de tags de chaque livre en un seul flux.
        return Library.BOOKS.stream().flatMap(b -> b.tags().stream()).distinct().sorted().toList();
    }

    public static List<String> firstTitles(int n) {
        // limit(n) : les n premiers, puis le pipeline s'arrete (court-circuit).
        return Library.BOOKS.stream().limit(n).map(Book::title).toList();
    }

    public static List<String> titlesAfter(int n) {
        // skip(n) : jette les n premiers elements.
        return Library.BOOKS.stream().skip(n).map(Book::title).toList();
    }

    public static List<String> cheapTitles(double max) {
        // On TRIE d'abord : takeWhile s'arrete au 1er livre trop cher, tous les suivants le sont aussi.
        return Library.BOOKS.stream()
                .sorted(Comparator.comparingDouble(Book::price))
                .takeWhile(b -> b.price() <= max)
                .map(Book::title)
                .toList();
    }

    public static List<String> expensiveTitles(double min) {
        // dropWhile jette tant que c'est trop bon marche, puis garde tout le reste.
        return Library.BOOKS.stream()
                .sorted(Comparator.comparingDouble(Book::price))
                .dropWhile(b -> b.price() < min)
                .map(Book::title)
                .toList();
    }

    public static long countSfWithTrace(List<String> seen) {
        // peek AVANT filter voit les 8 livres. (Sans le filter, count() pourrait sauter le peek.)
        return Library.BOOKS.stream()
                .peek(b -> seen.add(b.isbn()))
                .filter(b -> b.genre().equals("SF"))
                .count();
    }

    public static int[] pagesOf(String genre) {
        // mapToInt puis toArray() donne directement un int[].
        return Library.BOOKS.stream().filter(b -> b.genre().equals(genre)).mapToInt(Book::pages).toArray();
    }

    public static List<String> numberedTitles() {
        // On parcourt des INDICES pour avoir le numero ; mapToObj fabrique la String.
        return IntStream.range(0, Library.BOOKS.size())
                .mapToObj(i -> (i + 1) + ". " + Library.BOOKS.get(i).title())
                .toList();
    }

    public static List<Double> pricesWithTax(double rate) {
        // mapToDouble -> DoubleStream ; boxed() pour pouvoir faire toList().
        return Library.BOOKS.stream().mapToDouble(b -> b.price() * (1 + rate)).boxed().toList();
    }

    public static int totalTagLetters() {
        // flatMapToInt : chaque livre fournit un IntStream des longueurs de ses tags.
        return Library.BOOKS.stream().flatMapToInt(b -> b.tags().stream().mapToInt(String::length)).sum();
    }

    public static long totalPagesAsLong() {
        // mapToLong -> LongStream, donc sum() rend un long.
        return Library.BOOKS.stream().mapToLong(Book::pages).sum();
    }

    public static List<String> titlesBorrowedBy(String memberId) {
        // Jointure : emprunt -> isbn -> le livre correspondant (0 ou 1) via flatMap -> titre.
        return Library.LOANS.stream()
                .filter(l -> l.memberId().equals(memberId))
                .map(Loan::isbn)
                .flatMap(isbn -> Library.BOOKS.stream().filter(b -> b.isbn().equals(isbn)))
                .map(Book::title)
                .toList();
    }

    public static List<String> upperTitlesOfAuthor(String author) {
        // Une reference de methode (String::toUpperCase) remplace s -> s.toUpperCase().
        return Library.BOOKS.stream()
                .filter(b -> b.author().equals(author))
                .map(Book::title)
                .map(String::toUpperCase)
                .sorted()
                .toList();
    }
}
