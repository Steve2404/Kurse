package ch18_design.drills.r02_strategies.solution;

import java.time.LocalDate;
import java.util.List;

/** amount de remise des que le total atteint threshold (le seuil compris). */
public final class Threshold implements Promotion {

    private final long threshold;
    private final long amount;

    public Threshold(long threshold, long amount) {
        this.threshold = threshold;
        this.amount = amount;
    }

    @Override
    public long discount(List<Long> prices, LocalDate today) {
        long total = prices.stream().mapToLong(Long::longValue).sum();
        return total >= threshold ? amount : 0;
    }
}
