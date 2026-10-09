package ch18_design.drills.r04_wrappers.solution;

import java.util.List;

/** Le decorateur journal : "ACME = 1234" ou "ACME : erreur", puis rend ou relance tel quel. */
public final class LoggedFeed implements PriceFeed {

    private final PriceFeed inner;
    private final List<String> log;

    public LoggedFeed(PriceFeed inner, List<String> log) {
        this.inner = inner;
        this.log = log;
    }

    @Override
    public long price(String symbol) {
        try {
            long price = inner.price(symbol);
            log.add(symbol + " = " + price);
            return price;
        } catch (RuntimeException e) {
            log.add(symbol + " : erreur");
            throw e;
        }
    }
}
