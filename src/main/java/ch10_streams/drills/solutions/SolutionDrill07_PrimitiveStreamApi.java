package ch10_streams.drills.solutions;

import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

/**
 * Corrige du drill 7. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.drills.exercises.Drill07_PrimitiveStreamApi.
 */
public class SolutionDrill07_PrimitiveStreamApi {

    public static int totalPages() {
        // sum() d'un IntStream rend un int (0 si vide).
        return Library.BOOKS.stream().mapToInt(Book::pages).sum();
    }

    public static OptionalDouble averagePages() {
        // average() rend une OptionalDouble.
        return Library.BOOKS.stream().mapToInt(Book::pages).average();
    }

    public static IntSummaryStatistics pageStats() {
        // summaryStatistics : count, sum, min, max, moyenne en un seul passage.
        return Library.BOOKS.stream().mapToInt(Book::pages).summaryStatistics();
    }

    public static double totalPrice() {
        // mapToDouble -> DoubleStream, dont sum() rend un double.
        return Library.BOOKS.stream().mapToDouble(Book::price).sum();
    }

    public static DoubleSummaryStatistics priceStats() {
        // DoubleSummaryStatistics : meme idee pour les double.
        return Library.BOOKS.stream().mapToDouble(Book::price).summaryStatistics();
    }

    public static long totalLateDays() {
        // mapToLong -> LongStream, dont sum() rend un long.
        return Library.LOANS.stream().mapToLong(Library.Loan::daysLate).sum();
    }

    public static int sumUpTo(int n) {
        // rangeClosed(1, 0) est vide : la somme vaut alors 0.
        return IntStream.rangeClosed(1, n).sum();
    }

    public static double averageYear() {
        // asDoubleStream convertit IntStream -> DoubleStream (elargissement).
        return Library.BOOKS.stream().mapToInt(Book::year).asDoubleStream().average().getAsDouble();
    }

    public static List<Integer> pagesDesc() {
        // IntStream.sorted() n'accepte pas de Comparator : boxed() d'abord.
        return Library.BOOKS.stream().mapToInt(Book::pages).boxed().sorted(Comparator.reverseOrder()).toList();
    }

    public static int[] distinctDecades() {
        // y / 10 * 10 : division entiere, 1965 -> 1960.
        return Library.BOOKS.stream().mapToInt(Book::year).map(y -> y / 10 * 10).distinct().sorted().toArray();
    }

    public static List<String> pageLabels() {
        // mapToObj : int -> String.
        return Library.BOOKS.stream().mapToInt(Book::pages).mapToObj(p -> p + "p").toList();
    }

    public static int maxYearViaReduce() {
        // reduce(identite, IntBinaryOperator) : MIN_VALUE est neutre pour max.
        return Library.BOOKS.stream().mapToInt(Book::year).reduce(Integer.MIN_VALUE, Math::max);
    }

    public static long roundedPriceSum() {
        // Math.round(double) rend un long : c'est un DoubleToLongFunction valide pour mapToLong.
        return Library.BOOKS.stream().mapToDouble(Book::price).mapToLong(Math::round).sum();
    }

    public static boolean anyBookOver(int pages) {
        // anyMatch existe aussi sur IntStream (avec un IntPredicate).
        return Library.BOOKS.stream().mapToInt(Book::pages).anyMatch(p -> p > pages);
    }

    public static int isbnNumbersSum() {
        // substring(1) retire le "B" avant de convertir en int.
        return Library.BOOKS.stream().mapToInt(b -> Integer.parseInt(b.isbn().substring(1))).sum();
    }

    public static long sumOfSquaredPages() {
        // asLongStream avant de mettre au carre : le calcul se fait en long.
        return Library.BOOKS.stream().mapToInt(Book::pages).asLongStream().map(p -> p * p).sum();
    }

    public static int sumFirstOdds(int n) {
        // iterate infini + limit ; limit(0) donne un stream vide, donc une somme de 0.
        return IntStream.iterate(1, x -> x + 2).limit(n).sum();
    }
}
