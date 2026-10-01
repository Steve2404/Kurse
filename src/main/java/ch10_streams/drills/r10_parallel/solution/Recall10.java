package ch10_streams.drills.r10_parallel.solution;

import ch10_streams.drills.Data;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * SOLUTION du drill de rappel 10 - streams paralleles.
 */
public class Recall10 {

    public static void main(String[] args) {
        // Le dernier parallel()/sequential() gagne pour TOUT le pipeline.
        System.out.println("D01 : " + Data.WORDS.stream().isParallel() + " " + Data.WORDS.parallelStream().isParallel() + " "
                + Data.WORDS.stream().parallel().isParallel() + " " + Data.WORDS.stream().parallel().map(String::length).sequential().isParallel());

        StringJoiner ordered = new StringJoiner(" ");
        Arrays.stream(Data.NUMBERS).parallel().forEachOrdered(n -> ordered.add(String.valueOf(n)));
        System.out.println("D02 : " + ordered);

        // Operation NON associative (soustraction) : le resultat parallele depend du decoupage.
        int seq = IntStream.rangeClosed(1, 100).reduce(0, (a, b) -> a - b);
        int par = IntStream.rangeClosed(1, 100).parallel().reduce(0, (a, b) -> a - b);
        System.out.println("D03 : sequentiel " + seq + ", parallele different : " + (seq != par));

        // findFirst reste deterministe en parallele sur un flux ordonne ; findAny non.
        System.out.println("D04 : " + Data.WORDS.parallelStream().filter(w -> w.length() == 4).findFirst().orElse("-")
                + " " + Data.WORDS.parallelStream().filter(w -> w.length() == 4).findAny().isPresent());

        System.out.println("D05 : " + Data.WORDS.parallelStream().unordered().distinct().count());

        // toList/collect respectent l'ordre de rencontre, meme en parallele.
        List<Integer> seqLengths = Data.WORDS.stream().map(String::length).toList();
        List<Integer> parLengths = Data.WORDS.parallelStream().map(String::length).collect(Collectors.toList());
        System.out.println("D06 : " + seqLengths.equals(parLengths));

        ConcurrentMap<Integer, Long> byLength = Data.WORDS.parallelStream()
                .collect(Collectors.groupingByConcurrent(String::length, Collectors.counting()));
        System.out.println("D07 : " + byLength.get(6) + " " + byLength.get(4));

        // reduce a 3 arguments correct : identite neutre, accumulateur et combiner coherents.
        int s = Data.WORDS.stream().reduce(0, (acc, w) -> acc + w.length(), Integer::sum);
        int p = Data.WORDS.parallelStream().reduce(0, (acc, w) -> acc + w.length(), Integer::sum);
        System.out.println("D08 : " + s + " " + (s == p));

        System.out.println("D09 : " + Data.WORDS.parallelStream().collect(Collectors.toConcurrentMap(Function.identity(), w -> 1, Integer::sum)).get("java"));
    }
}
