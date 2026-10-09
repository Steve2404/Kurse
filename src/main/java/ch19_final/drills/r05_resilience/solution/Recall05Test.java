package ch19_final.drills.r05_resilience.solution;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference du drill 5 : tout le temps passe par la ManualClock. */
class Recall05Test {

    private static final Instant T0 = Instant.parse("2026-10-09T10:00:00Z");
    private final ManualClock clock = new ManualClock(T0);

    @Test
    void d01() {
        assertEquals(T0, clock.instant());
        clock.advance(Duration.ofMinutes(5));
        assertEquals(T0.plusSeconds(300), clock.instant());
        assertEquals(ZoneOffset.UTC, clock.getZone());
        assertTrue(clock instanceof Clock);
        assertThrows(UnsupportedOperationException.class, () -> clock.withZone(ZoneOffset.ofHours(2)));
    }

    @Test
    void d02() {
        Bucket bucket = new Bucket(3, 2, clock);
        assertTrue(bucket.tryAcquire());
        assertTrue(bucket.tryAcquire());
        assertTrue(bucket.tryAcquire());
        assertFalse(bucket.tryAcquire());
        clock.advance(Duration.ofMillis(499));
        assertFalse(bucket.tryAcquire());
        clock.advance(Duration.ofMillis(1));
        assertTrue(bucket.tryAcquire());
        clock.advance(Duration.ofHours(1));
        int taken = 0;
        while (bucket.tryAcquire()) {
            taken++;
        }
        assertEquals(3, taken);
    }

    @Test
    void d03() {
        Bucket bucket = new Bucket(1, 1, clock);
        assertEquals(0, bucket.waitMillis());
        bucket.tryAcquire();
        assertEquals(1000, bucket.waitMillis());
        int steps = 0;
        do {
            clock.advance(Duration.ofNanos(1_500_000));
            steps++;
        } while (!bucket.tryAcquire());
        assertEquals(667, steps);
        Bucket third = new Bucket(1, 3, clock);
        third.tryAcquire();
        assertEquals(334, third.waitMillis());
    }

    @Test
    void d04() {
        Breaker breaker = new Breaker(3, Duration.ofSeconds(10), clock);
        AtomicInteger calls = new AtomicInteger();
        Runnable fail = () -> assertThrows(IllegalStateException.class, () -> breaker.call(() -> {
            calls.incrementAndGet();
            throw new IllegalStateException("panne");
        }));
        fail.run();
        fail.run();
        assertEquals("ok", breaker.call(() -> "ok"));
        fail.run();
        fail.run();
        assertEquals(Breaker.State.CLOSED, breaker.state());
        fail.run();
        assertEquals(Breaker.State.OPEN, breaker.state());
        assertEquals("ouvert : encore 10 s", assertThrows(BreakerOpenException.class, () -> breaker.call(() -> "x")).getMessage());
        clock.advance(Duration.ofMillis(6500));
        assertEquals("ouvert : encore 4 s", assertThrows(BreakerOpenException.class, () -> breaker.call(() -> "x")).getMessage());
        assertEquals(5, calls.get());
    }

    @Test
    void d05() {
        Breaker breaker = new Breaker(1, Duration.ofSeconds(10), clock);
        assertThrows(IllegalStateException.class, () -> breaker.call(() -> {
            throw new IllegalStateException("panne");
        }));
        clock.advance(Duration.ofSeconds(10));
        assertThrows(IllegalStateException.class, () -> breaker.call(() -> {
            throw new IllegalStateException("encore");
        }));
        assertEquals(Breaker.State.OPEN, breaker.state());
        assertEquals("ouvert : encore 10 s", assertThrows(BreakerOpenException.class, () -> breaker.call(() -> "x")).getMessage());
        clock.advance(Duration.ofSeconds(10));
        AtomicBoolean seen = new AtomicBoolean();
        assertEquals("repare", breaker.call(() -> {
            seen.set(breaker.state() == Breaker.State.HALF_OPEN);
            return "repare";
        }));
        assertTrue(seen.get());
        assertEquals(Breaker.State.CLOSED, breaker.state());
    }

    @Test
    void d06() {
        long[] values = {70, 10, 100, 40, 20, 90, 30, 60, 80, 50};
        assertEquals(50, Percentiles.of(values, 50));
        assertEquals(100, Percentiles.of(values, 95));
        assertEquals(20, Percentiles.of(values, 11));
        assertEquals(10, Percentiles.of(values, 1));
        assertEquals(70, values[0]);
        assertEquals("aucune valeur", assertThrows(IllegalArgumentException.class, () -> Percentiles.of(new long[0], 50)).getMessage());
        assertThrows(IllegalArgumentException.class, () -> Percentiles.of(values, 0));
        LastKnown<String, Integer> stock = new LastKnown<>();
        AtomicBoolean down = new AtomicBoolean();
        java.util.function.Function<String, Integer> source = ref -> {
            if (down.get()) {
                throw new IllegalStateException("fournisseur indisponible");
            }
            return ref.length();
        };
        assertEquals(8, stock.get("CHAIN-11", source));
        down.set(true);
        assertEquals(8, stock.get("CHAIN-11", source));
        assertEquals("fournisseur indisponible", assertThrows(IllegalStateException.class, () -> stock.get("BRAKE", source)).getMessage());
    }
}
