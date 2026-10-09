package ch18_design.drills.r04_wrappers.solution;

/** Le decorateur : au plus attempts appels ; apres le dernier echec, la derniere exception repart. */
public final class RetryFeed implements PriceFeed {

    private final PriceFeed inner;
    private final int attempts;

    public RetryFeed(PriceFeed inner, int attempts) {
        this.inner = inner;
        this.attempts = attempts;
    }

    @Override
    public long price(String symbol) {
        RuntimeException last = new IllegalStateException("aucune tentative");
        for (int i = 0; i < attempts; i++) {
            try {
                return inner.price(symbol);
            } catch (RuntimeException e) {
                last = e;
            }
        }
        throw last;
    }
}
