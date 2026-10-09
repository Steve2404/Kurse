package ch18_design.drills.r04_wrappers.solution;

import ch18_design.drills.r04_wrappers.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Les tests de reference du drill 4. */
class Recall04Test {

    static final Map<String, Double> EUROS = Map.of("acme", 12.345, "bolt", 7.0, "cork", 0.994);

    @Test
    void d01() {
        ExchangeAdapter adapter = new ExchangeAdapter(new Data.OldExchange(EUROS, 0));
        assertEquals(1235, adapter.price("ACME"));
        assertEquals(700, adapter.price("BOLT"));
        assertEquals(99, adapter.price("CORK"));
        assertEquals("symbole inconnu : zinc", assertThrows(NoSuchElementException.class, () -> adapter.price("ZINC")).getMessage());
    }

    @Test
    void d02() {
        Data.OldExchange exchange = new Data.OldExchange(EUROS, 1);
        CachedFeed cached = new CachedFeed(new ExchangeAdapter(exchange));
        assertThrows(IllegalStateException.class, () -> cached.price("ACME"));
        assertEquals(1235, cached.price("ACME"));
        assertEquals(1235, cached.price("ACME"));
        assertEquals(700, cached.price("BOLT"));
        assertEquals(3, cached.misses());
        assertEquals(3, exchange.calls());
    }

    @Test
    void d03() {
        Data.OldExchange exchange = new Data.OldExchange(EUROS, 2);
        assertEquals(700, new RetryFeed(new ExchangeAdapter(exchange), 3).price("BOLT"));
        assertEquals(3, exchange.calls());
        Data.OldExchange worse = new Data.OldExchange(EUROS, 5);
        assertEquals("bourse injoignable",
                assertThrows(IllegalStateException.class, () -> new RetryFeed(new ExchangeAdapter(worse), 4).price("BOLT")).getMessage());
        assertEquals(4, worse.calls());
    }

    @Test
    void d04() {
        PriceFeed broken = symbol -> {
            throw new IllegalStateException("panne");
        };
        assertEquals(650, new BestFeed(List.of(symbol -> 700, broken, symbol -> 650, symbol -> 900)).price("BOLT"));
        assertEquals("aucune source pour BOLT",
                assertThrows(IllegalStateException.class, () -> new BestFeed(List.of(broken)).price("BOLT")).getMessage());
    }

    @Test
    void d05() {
        IllegalStateException failure = new IllegalStateException("panne");
        List<String> log = new ArrayList<>();
        LoggedFeed logged = new LoggedFeed(symbol -> {
            if (symbol.equals("ZINC")) {
                throw failure;
            }
            return 42;
        }, log);
        assertEquals(42, logged.price("ACME"));
        assertSame(failure, assertThrows(IllegalStateException.class, () -> logged.price("ZINC")));
        assertEquals(List.of("ACME = 42", "ZINC : erreur"), log);
    }

    // L'emboitement : le journal voit chaque demande, le cache evite les rappels, les tentatives insistent.
    @Test
    void d06() {
        Data.OldExchange exchange = new Data.OldExchange(EUROS, 1);
        List<String> log = new ArrayList<>();
        PriceFeed feed = new LoggedFeed(new CachedFeed(new RetryFeed(new ExchangeAdapter(exchange), 2)), log);
        assertEquals(1235, feed.price("ACME"));
        assertEquals(1235, feed.price("ACME"));
        assertEquals(2, exchange.calls());
        assertEquals(List.of("ACME = 1235", "ACME = 1235"), log);
    }
}
