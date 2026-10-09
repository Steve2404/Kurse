package ch18_design.drills.r04_wrappers.solution;

import java.util.HashMap;
import java.util.Map;

/** Le proxy : garde chaque prix (pour toute la seance), compte les vrais appels ; une erreur n'est pas gardee. */
public final class CachedFeed implements PriceFeed {

    private final PriceFeed inner;
    private final Map<String, Long> cache = new HashMap<>();
    private int misses;

    public CachedFeed(PriceFeed inner) {
        this.inner = inner;
    }

    @Override
    public long price(String symbol) {
        Long known = cache.get(symbol);
        if (known != null) {
            return known;
        }
        misses++;
        long fresh = inner.price(symbol);
        cache.put(symbol, fresh);
        return fresh;
    }

    public int misses() {
        return misses;
    }
}
