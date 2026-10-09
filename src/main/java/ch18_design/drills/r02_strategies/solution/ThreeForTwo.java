package ch18_design.drills.r02_strategies.solution;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

/** Par tranche de 3 articles, le moins cher de la tranche est offert (les prix tries du plus cher au moins cher). */
public final class ThreeForTwo implements Promotion {

    @Override
    public long discount(List<Long> prices, LocalDate today) {
        List<Long> sorted = prices.stream().sorted(Comparator.reverseOrder()).toList();
        return IntStream.range(0, sorted.size()).filter(i -> i % 3 == 2).mapToLong(sorted::get).sum();
    }
}
