package ch10_streams.drills.exercises;

import ch10_streams.ExerciseChecker;
import ch10_streams.drills.Library;
import ch10_streams.drills.Library.Book;

import java.util.Arrays;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.IntStream;

/**
 * DRILL 07 - IntStream / LongStream / DoubleStream (projet bibliotheque)
 * ======================================================================
 *
 * Mode d'emploi : voir Drill01_OptionalApi (chronometre, sans la carte,
 * puis recommencer plus tard selon drills/REVISION.md).
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : totalPages()            [mapToInt + sum] -> 2037.
 * TODO 2  : averagePages()          [average] -> OptionalDouble[254.625].
 * TODO 3  : pageStats()             [summaryStatistics] -> IntSummaryStatistics.
 * TODO 4  : totalPrice()            [mapToDouble + sum] -> 64.0.
 * TODO 5  : priceStats()            [DoubleStream.summaryStatistics].
 * TODO 6  : totalLateDays()         [mapToLong + sum] sur LOANS -> 19.
 * TODO 7  : sumUpTo(n)              [IntStream.rangeClosed + sum].
 * TODO 8  : averageYear()           [asDoubleStream + average + getAsDouble] -> 1953.0.
 * TODO 9  : pagesDesc()             [boxed + sorted(Comparator)] pages decroissantes.
 * TODO 10 : distinctDecades()       [map + distinct + sorted + toArray] -> [1930, 1940, 1950, 1960, 1980].
 * TODO 11 : pageLabels()            [mapToObj] "412p", "255p", ...
 * TODO 12 : maxYearViaReduce()      [IntStream.reduce(identity, IntBinaryOperator)] -> 1984.
 * TODO 13 : roundedPriceSum()       [DoubleStream.mapToLong(Math::round) + sum] -> 66.
 * TODO 14 : anyBookOver(pages)      [IntStream.anyMatch]
 * TODO 15 : isbnNumbersSum()        [mapToInt + Integer.parseInt] "B1".."B8" -> 1+..+8 = 36.
 * TODO 16 : sumOfSquaredPages()     [asLongStream + map + sum] -> 597663.
 * TODO 17 : sumFirstOdds(n)         [IntStream.iterate + limit + sum] 1 + 3 + 5 + ... (n termes).
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *   Stream<T> -> mapToInt / mapToLong / mapToDouble -> XxxStream
 *   IntStream -> boxed() -> Stream<Integer> ; mapToObj(IntFunction) -> Stream<R>
 *   IntStream -> asLongStream() / asDoubleStream() ; mapToLong / mapToDouble
 *   DoubleStream -> mapToInt / mapToLong (ex : Math::round rend un long)
 *   sum() -> int/long/double (0 si vide)       average() -> OptionalDouble
 *   max()/min() -> OptionalInt/Long/Double     count() -> long
 *   summaryStatistics() -> Int/Long/DoubleSummaryStatistics
 *        getCount getSum getMin getMax getAverage
 *   reduce(int identity, IntBinaryOperator)    reduce(IntBinaryOperator) -> OptionalInt
 *   sorted() (PAS de Comparator sur IntStream !)
 * ---------------------------------------------------------------------
 */
public class Drill07_PrimitiveStreamApi {

    public static int totalPages() {
        throw new UnsupportedOperationException("TODO 1 : implementer totalPages()");
    }

    public static OptionalDouble averagePages() {
        throw new UnsupportedOperationException("TODO 2 : implementer averagePages()");
    }

    public static IntSummaryStatistics pageStats() {
        throw new UnsupportedOperationException("TODO 3 : implementer pageStats()");
    }

    public static double totalPrice() {
        throw new UnsupportedOperationException("TODO 4 : implementer totalPrice()");
    }

    public static DoubleSummaryStatistics priceStats() {
        throw new UnsupportedOperationException("TODO 5 : implementer priceStats()");
    }

    public static long totalLateDays() {
        throw new UnsupportedOperationException("TODO 6 : implementer totalLateDays()");
    }

    public static int sumUpTo(int n) {
        throw new UnsupportedOperationException("TODO 7 : implementer sumUpTo()");
    }

    public static double averageYear() {
        throw new UnsupportedOperationException("TODO 8 : implementer averageYear()");
    }

    public static List<Integer> pagesDesc() {
        throw new UnsupportedOperationException("TODO 9 : implementer pagesDesc()");
    }

    public static int[] distinctDecades() {
        throw new UnsupportedOperationException("TODO 10 : implementer distinctDecades()");
    }

    public static List<String> pageLabels() {
        throw new UnsupportedOperationException("TODO 11 : implementer pageLabels()");
    }

    public static int maxYearViaReduce() {
        throw new UnsupportedOperationException("TODO 12 : implementer maxYearViaReduce()");
    }

    public static long roundedPriceSum() {
        throw new UnsupportedOperationException("TODO 13 : implementer roundedPriceSum()");
    }

    public static boolean anyBookOver(int pages) {
        throw new UnsupportedOperationException("TODO 14 : implementer anyBookOver()");
    }

    public static int isbnNumbersSum() {
        throw new UnsupportedOperationException("TODO 15 : implementer isbnNumbersSum()");
    }

    public static long sumOfSquaredPages() {
        throw new UnsupportedOperationException("TODO 16 : implementer sumOfSquaredPages()");
    }

    public static int sumFirstOdds(int n) {
        throw new UnsupportedOperationException("TODO 17 : implementer sumFirstOdds()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  totalPages == 2037", totalPages() == 2037);
        ExerciseChecker.check("2  averagePages == 254.625", averagePages().equals(OptionalDouble.of(254.625)));
        IntSummaryStatistics ps = pageStats();
        ExerciseChecker.check("3  pageStats : count 8, min 96, max 412, sum 2037",
                ps.getCount() == 8 && ps.getMin() == 96 && ps.getMax() == 412 && ps.getSum() == 2037);
        ExerciseChecker.check("4  totalPrice == 64.0", totalPrice() == 64.0);
        DoubleSummaryStatistics prs = priceStats();
        ExerciseChecker.check("5  priceStats : min 5.5, max 10.0, moyenne 8.0",
                prs.getMin() == 5.5 && prs.getMax() == 10.0 && prs.getAverage() == 8.0);
        ExerciseChecker.check("6  totalLateDays == 19", totalLateDays() == 19L);
        ExerciseChecker.check("7  sumUpTo(10) == 55, sumUpTo(0) == 0", sumUpTo(10) == 55 && sumUpTo(0) == 0);
        ExerciseChecker.check("8  averageYear == 1953.0", averageYear() == 1953.0);
        ExerciseChecker.check("9  pagesDesc", pagesDesc().equals(List.of(412, 328, 310, 271, 255, 253, 112, 96)));
        ExerciseChecker.check("10 distinctDecades", Arrays.equals(distinctDecades(), new int[]{1930, 1940, 1950, 1960, 1980}));
        List<String> labels = pageLabels();
        ExerciseChecker.check("11 pageLabels : 412p en premier, 310p en dernier",
                labels.size() == 8 && labels.get(0).equals("412p") && labels.get(7).equals("310p"));
        ExerciseChecker.check("12 maxYearViaReduce == 1984", maxYearViaReduce() == 1984);
        ExerciseChecker.check("13 roundedPriceSum == 66", roundedPriceSum() == 66L);
        ExerciseChecker.check("14 anyBookOver(400) && !anyBookOver(412)", anyBookOver(400) && !anyBookOver(412));
        ExerciseChecker.check("15 isbnNumbersSum == 36", isbnNumbersSum() == 36);
        ExerciseChecker.check("16 sumOfSquaredPages == 597663", sumOfSquaredPages() == 597663L);
        ExerciseChecker.check("17 sumFirstOdds(5) == 25, sumFirstOdds(0) == 0", sumFirstOdds(5) == 25 && sumFirstOdds(0) == 0);

        ExerciseChecker.summary();
    }
}
