package ch18_design.drills.r02_strategies.solution;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

/** La caisse : elle RECOIT ses promotions et son horloge (injection) ; elle ne nomme aucune promotion. */
public final class Checkout {

    private final List<Promotion> promotions;
    private final Clock clock;

    public Checkout(List<Promotion> promotions, Clock clock) {
        this.promotions = List.copyOf(promotions);
        this.clock = clock;
    }

    // Chaque promotion se calcule sur les prix d'origine ; le total ne descend jamais sous 0.
    public long total(List<Long> prices) {
        LocalDate today = LocalDate.now(clock);
        long sum = prices.stream().mapToLong(Long::longValue).sum();
        long discounts = promotions.stream().mapToLong(p -> p.discount(prices, today)).sum();
        return Math.max(0, sum - discounts);
    }
}
