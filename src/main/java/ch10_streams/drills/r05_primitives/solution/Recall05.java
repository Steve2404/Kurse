package ch10_streams.drills.r05_primitives.solution;

import ch10_streams.drills.Data;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * SOLUTION du drill de rappel 5 - streams primitifs et conversions.
 */
public class Recall05 {

    public static void main(String[] args) {
        // range : fin EXCLUE ; rangeClosed : fin INCLUSE.
        System.out.println("D01 : " + IntStream.range(1, 5).sum() + " " + IntStream.rangeClosed(1, 5).sum() + " "
                + LongStream.rangeClosed(1, 20).reduce(1, (a, b) -> a * b));

        // sum() d'un IntStream est un int ; average() est TOUJOURS un OptionalDouble.
        OptionalDouble avg = IntStream.of(Data.NUMBERS).average();
        System.out.println("D02 : " + IntStream.of(Data.NUMBERS).sum() + " " + avg.getAsDouble() + " " + IntStream.empty().average());

        System.out.println("D03 : " + IntStream.of(Data.NUMBERS).max().getAsInt() + " " + LongStream.of(4L, 9L).min().getAsLong()
                + " " + DoubleStream.of(1.5, 2.5).max().getAsDouble());

        IntSummaryStatistics is = IntStream.of(Data.NUMBERS).summaryStatistics();
        LongSummaryStatistics ls = LongStream.of(10L, 20L).summaryStatistics();
        DoubleSummaryStatistics ds = DoubleStream.of(0.5, 1.5).summaryStatistics();
        System.out.println("D04 : " + is.getMin() + "-" + is.getMax() + " " + ls.getSum() + " " + ds.getAverage());

        // Stream<String> -> IntStream -> Stream<String> : mapToInt puis mapToObj.
        System.out.println("D05 : " + Data.WORDS.stream().mapToInt(String::length).mapToObj(n -> "#".repeat(n)).limit(2).toList());

        // boxed : IntStream -> Stream<Integer>, seul chemin vers une List<Integer>.
        List<Integer> evens = IntStream.rangeClosed(1, 10).filter(n -> n % 2 == 0).boxed().toList();
        System.out.println("D06 : " + evens);

        // asLongStream / asDoubleStream : elargissements ; mapToLong/mapToDouble : transformation.
        System.out.println("D07 : " + IntStream.of(Integer.MAX_VALUE, 1).asLongStream().sum() + " "
                + IntStream.of(Integer.MAX_VALUE, 1).sum() + " " + IntStream.of(1, 2).asDoubleStream().map(d -> d / 4).sum());

        System.out.println("D08 : " + Stream.of("a", "bb", "ccc").mapToLong(String::length).sum() + " "
                + Stream.of("1.5", "2.5").mapToDouble(Double::parseDouble).sum() + " "
                + DoubleStream.of(1.9, 2.9).mapToInt(d -> (int) d).sum());

        System.out.println("D09 : " + Stream.of(List.of(1, 2), List.of(3)).flatMapToInt(l -> l.stream().mapToInt(Integer::intValue)).sum());

        System.out.println("D10 : " + IntStream.iterate(1, i -> i <= 100, i -> i * 3).boxed().toList() + " "
                + IntStream.generate(() -> 7).limit(3).sum());

        System.out.println("D11 : " + "OCP".chars().map(c -> c + 1).mapToObj(c -> String.valueOf((char) c)).collect(Collectors.joining()));

        // Stream<Integer>.max veut un Comparator ; IntStream.max() n'en veut pas.
        System.out.println("D12 : " + Arrays.stream(Data.NUMBERS).boxed().max(Integer::compare).orElse(-1)
                + " " + Math.round(DoubleStream.of(1, 2, 2).average().orElse(0) * 1000) / 1000.0);
    }
}
