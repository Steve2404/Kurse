package ch19_final.projects.p05_resilience.solution;

import ch19_final.projects.p05_resilience.Data;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResilientStockTest {

    private final ManualClock clock = new ManualClock(Instant.parse("2026-10-09T10:00:00Z"));
    private final AtomicBoolean down = new AtomicBoolean();
    private final AtomicInteger calls = new AtomicInteger();
    private final LatencyRecorder latency = new LatencyRecorder(10);
    private final CircuitBreaker breaker = new CircuitBreaker(2, Duration.ofSeconds(30), clock,
            e -> !(e instanceof IllegalArgumentException));

    /** Un fournisseur de test : 30 ms quand il va bien, 2 s pour echouer en panne. */
    private int supplier(String ref) {
        calls.incrementAndGet();
        if (ref.equals("NOPE")) {
            throw new IllegalArgumentException("reference inconnue : NOPE");
        }
        if (down.get()) {
            clock.advance(Duration.ofSeconds(2));
            throw new IllegalStateException("fournisseur indisponible");
        }
        clock.advance(Duration.ofMillis(30));
        return 14;
    }

    private final ResilientStock stock = new ResilientStock(this::supplier, breaker, latency, clock);

    // ------------------------------------------------------------------ les percentiles

    @ParameterizedTest
    @CsvSource({"50, 50", "90, 90", "95, 100", "99, 100", "100, 100", "1, 10", "10, 10", "11, 20"})
    void nearestRankPercentiles(double p, long expected) {
        LatencyRecorder r = new LatencyRecorder(100);
        for (long ms : new long[]{70, 10, 100, 40, 20, 90, 30, 60, 80, 50}) {
            r.record(Duration.ofMillis(ms));
        }
        assertEquals(expected, r.percentile(p));
    }

    @Test
    void averageLiesPercentilesDoNot() {
        LatencyRecorder r = new LatencyRecorder(100);
        for (int i = 0; i < 99; i++) {
            r.record(Duration.ofMillis(10));
        }
        r.record(Duration.ofSeconds(5));
        assertEquals(10, r.percentile(50));
        assertEquals(10, r.percentile(99));
        assertEquals(5000, r.percentile(100));
        assertEquals("n=100 p50=10 ms p95=10 ms p99=10 ms max=5000 ms", r.summary());
    }

    @Test
    void windowKeepsOnlyTheLastMeasures() {
        LatencyRecorder r = new LatencyRecorder(3);
        for (long ms : new long[]{900, 800, 1, 2, 3}) {
            r.record(Duration.ofMillis(ms));
        }
        assertEquals(3, r.count());
        assertEquals(3, r.percentile(100));
        assertEquals(2, r.percentile(50));
    }

    @Test
    void percentileErrors() {
        LatencyRecorder r = new LatencyRecorder(3);
        assertEquals("aucune mesure", r.summary());
        assertEquals("aucune mesure", assertThrows(IllegalStateException.class, () -> r.percentile(50)).getMessage());
        r.record(Duration.ofMillis(5));
        assertEquals("percentile hors de ]0, 100] : 0.0", assertThrows(IllegalArgumentException.class, () -> r.percentile(0)).getMessage());
        assertThrows(IllegalArgumentException.class, () -> r.percentile(100.5));
        assertEquals(5, r.percentile(0.1));
    }

    // ------------------------------------------------------------------ le stock protege

    @Test
    void freshAnswerFromTheSupplier() {
        assertEquals(new StockAnswer("CHAIN-11", 14, "fournisseur"), stock.stock("CHAIN-11"));
        assertEquals(1, latency.count());
        assertEquals(30, latency.percentile(100));
    }

    @Test
    void outageFallsBackOnTheLastKnownValueWithItsAge() {
        stock.stock("CHAIN-11");
        clock.advance(Duration.ofSeconds(10));
        down.set(true);
        assertEquals(new StockAnswer("CHAIN-11", 14, "cache (12 s)"), stock.stock("CHAIN-11"));
        assertEquals(2000, latency.percentile(100));
    }

    @Test
    void cacheKeepsTheLatestValue() {
        stock.stock("CHAIN-11");
        clock.advance(Duration.ofSeconds(10));
        stock.stock("CHAIN-11");
        clock.advance(Duration.ofSeconds(5));
        down.set(true);
        assertEquals("cache (7 s)", stock.stock("CHAIN-11").source());
    }

    @Test
    void unknownValueDuringOutageIsAClearError() {
        down.set(true);
        StockUnavailableException e = assertThrows(StockUnavailableException.class, () -> stock.stock("BRAKE-V"));
        assertEquals("stock inconnu pour BRAKE-V : fournisseur indisponible", e.getMessage());
        assertTrue(e.getCause() instanceof IllegalStateException);
    }

    @Test
    void openCircuitAnswersFastWithoutCallingTheSupplier() {
        stock.stock("CHAIN-11");
        down.set(true);
        stock.stock("CHAIN-11");
        stock.stock("CHAIN-11");
        assertEquals(CircuitBreaker.State.OPEN, breaker.state());
        int before = calls.get();
        Instant asked = clock.instant();
        StockAnswer answer = stock.stock("CHAIN-11");
        assertEquals("cache (4 s)", answer.source());
        assertEquals(before, calls.get());
        assertEquals(asked, clock.instant());
        assertEquals(0, latency.percentile(10));
    }

    @Test
    void openCircuitWithoutCacheSaysWhy() {
        down.set(true);
        assertThrows(StockUnavailableException.class, () -> stock.stock("CHAIN-11"));
        assertThrows(StockUnavailableException.class, () -> stock.stock("CHAIN-11"));
        StockUnavailableException e = assertThrows(StockUnavailableException.class, () -> stock.stock("CHAIN-11"));
        assertEquals("stock inconnu pour CHAIN-11 : circuit ouvert : reessayer dans 30 s", e.getMessage());
    }

    @Test
    void unknownReferenceIsNotHidden() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> stock.stock("NOPE"));
        assertEquals("reference inconnue : NOPE", e.getMessage());
        assertThrows(IllegalArgumentException.class, () -> stock.stock("NOPE"));
        assertThrows(IllegalArgumentException.class, () -> stock.stock("NOPE"));
        assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
        assertEquals(3, latency.count());
    }

    @Test
    void recoversAfterTheOutage() {
        stock.stock("CHAIN-11");
        down.set(true);
        stock.stock("CHAIN-11");
        stock.stock("CHAIN-11");
        down.set(false);
        clock.advance(Duration.ofSeconds(30));
        assertEquals("fournisseur", stock.stock("CHAIN-11").source());
        assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
    }

    @Test
    void demoScenarioWithTheProvidedSupplier() {
        ManualClock demoClock = new ManualClock(Data.START);
        Data.PartsSupplier supplier = new Data.PartsSupplier(demoClock);
        CircuitBreaker demoBreaker = new CircuitBreaker(3, Duration.ofSeconds(4), demoClock, e -> !(e instanceof IllegalArgumentException));
        ResilientStock demo = new ResilientStock(supplier::stock, demoBreaker, new LatencyRecorder(50), demoClock);
        StringBuilder sources = new StringBuilder();
        for (int second = 1; second <= 20; second++) {
            demoClock.advance(Duration.ofSeconds(1));
            sources.append(demo.stock("TUBE-700").source().startsWith("cache") ? 'C' : 'F');
        }
        assertEquals("FFFFCCCCCCCCCCFFFFFF", sources.toString());
        assertEquals(14, supplier.calls());
        assertThrows(IllegalArgumentException.class, () -> demo.stock("SADDLE"));
    }
}
