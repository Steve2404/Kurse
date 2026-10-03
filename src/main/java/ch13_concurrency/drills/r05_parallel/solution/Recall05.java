package ch13_concurrency.drills.r05_parallel.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * SOLUTION du drill de rappel 5 - les streams paralleles.
 */
public class Recall05 {

    public static void main(String[] args) {
        List<Integer> nums = IntStream.rangeClosed(1, 10).boxed().toList();
        Stream<Integer> st = nums.stream();
        System.out.println("D01 : " + st.isParallel() + " " + nums.parallelStream().isParallel() + " " + nums.stream().parallel().sequential().isParallel());

        System.out.println("D02 : " + nums.parallelStream().reduce(0, Integer::sum) + " " + (nums.parallelStream().reduce(5, Integer::sum) != 60) + " "
                + nums.parallelStream().reduce(0, (acc, n) -> acc + n * n, Integer::sum));

        List<Integer> seen = Collections.synchronizedList(new ArrayList<>());
        nums.parallelStream().forEachOrdered(seen::add);
        System.out.println("D03 : " + seen + " " + nums.parallelStream().map(n -> n * 10).toList());

        System.out.println("D04 : " + nums.parallelStream().filter(n -> n > 4).findFirst().orElseThrow() + " " + nums.parallelStream().filter(n -> n > 4).findAny().isPresent()
                + " " + nums.parallelStream().unordered().skip(2).count());

        ConcurrentMap<Boolean, List<Integer>> parity = nums.parallelStream().collect(Collectors.groupingByConcurrent(n -> n % 2 == 0));
        ConcurrentMap<Integer, String> squares = nums.parallelStream().filter(n -> n <= 4).collect(Collectors.toConcurrentMap(n -> n, n -> "n" + n * n));
        Map<Boolean, Integer> sizes = new TreeMap<>();
        parity.forEach((k, v) -> sizes.put(k, v.size()));
        System.out.println("D05 : " + parity.getClass().getSimpleName() + " " + sizes + " " + new TreeMap<>(squares));

        List<String> words = List.of("b", "a", "d", "c");
        String joined = words.parallelStream().collect(Collectors.joining(","));
        List<String> sortedPar = words.parallelStream().sorted().toList();
        System.out.println("D06 : " + joined + " " + sortedPar + " " + words.parallelStream().map(String::toUpperCase).collect(Collectors.toList()));
    }
}
