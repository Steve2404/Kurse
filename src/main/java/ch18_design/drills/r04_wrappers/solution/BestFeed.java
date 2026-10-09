package ch18_design.drills.r04_wrappers.solution;

import java.util.List;
import java.util.OptionalLong;

/** Le composite : demande a toutes les sources et rend le prix le plus bas parmi celles qui repondent. */
public final class BestFeed implements PriceFeed {

    private final List<PriceFeed> sources;

    public BestFeed(List<PriceFeed> sources) {
        this.sources = List.copyOf(sources);
    }

    @Override
    public long price(String symbol) {
        OptionalLong best = sources.stream().mapToLong(source -> answer(source, symbol)).filter(p -> p >= 0).min();
        return best.orElseThrow(() -> new IllegalStateException("aucune source pour " + symbol));
    }

    // -1 pour une source en panne (un prix n'est jamais negatif).
    private static long answer(PriceFeed source, String symbol) {
        try {
            return source.price(symbol);
        } catch (RuntimeException e) {
            return -1;
        }
    }
}
