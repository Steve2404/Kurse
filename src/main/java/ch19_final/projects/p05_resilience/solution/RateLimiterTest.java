package ch19_final.projects.p05_resilience.solution;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RateLimiterTest {

    private final ManualClock clock = new ManualClock(Instant.parse("2026-10-09T10:00:00Z"));

    private static List<Boolean> take(TokenBucket bucket, int times) {
        List<Boolean> results = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            results.add(bucket.tryAcquire());
        }
        return results;
    }

    @Test
    void burstUpToCapacityThenRefuse() {
        TokenBucket bucket = new TokenBucket(3, 1, clock);
        assertEquals(List.of(true, true, true, false, false), take(bucket, 5));
    }

    @Test
    void refillsAtTheGivenRate() {
        TokenBucket bucket = new TokenBucket(3, 2, clock);
        take(bucket, 3);
        clock.advance(Duration.ofMillis(499));
        assertFalse(bucket.tryAcquire());
        clock.advance(Duration.ofMillis(1));
        assertTrue(bucket.tryAcquire());
        assertFalse(bucket.tryAcquire());
        clock.advance(Duration.ofSeconds(1));
        assertEquals(List.of(true, true, false), take(bucket, 3));
    }

    @Test
    void neverMoreThanCapacity() {
        TokenBucket bucket = new TokenBucket(2, 5, clock);
        clock.advance(Duration.ofHours(1));
        assertEquals(List.of(true, true, false), take(bucket, 3));
    }

    @Test
    void manySmallStepsAddUpExactly() {
        // 10 pas de 100 ms a 1 jeton/s : exactement 1 jeton (un double ferait 0.9999999999999999).
        TokenBucket bucket = new TokenBucket(1, 1, clock);
        bucket.tryAcquire();
        for (int i = 0; i < 9; i++) {
            clock.advance(Duration.ofMillis(100));
            assertFalse(bucket.tryAcquire());
        }
        clock.advance(Duration.ofMillis(100));
        assertTrue(bucket.tryAcquire());
    }

    @Test
    void subMillisecondStepsAreNotLost() {
        // Des pas de 1,5 ms, avec un essai a chaque pas : la demi-milliseconde de chaque pas doit etre gardee.
        TokenBucket bucket = new TokenBucket(1, 1, clock);
        bucket.tryAcquire();
        int steps = 0;
        do {
            clock.advance(Duration.ofNanos(1_500_000));
            steps++;
        } while (!bucket.tryAcquire());
        assertEquals(667, steps);
    }

    @Test
    void timeUntilNextToken() {
        TokenBucket bucket = new TokenBucket(2, 4, clock);
        assertEquals(Duration.ZERO, bucket.timeUntilNext());
        take(bucket, 2);
        assertEquals(Duration.ofMillis(250), bucket.timeUntilNext());
        clock.advance(Duration.ofMillis(100));
        assertEquals(Duration.ofMillis(150), bucket.timeUntilNext());
        TokenBucket slow = new TokenBucket(1, 3, clock);
        slow.tryAcquire();
        assertEquals(Duration.ofMillis(334), slow.timeUntilNext());
    }

    @Test
    void bucketIsChecked() {
        assertThrows(IllegalArgumentException.class, () -> new TokenBucket(0, 1, clock));
        assertThrows(IllegalArgumentException.class, () -> new TokenBucket(1, 0, clock));
    }

    @Test
    void oneBucketPerClient() {
        RateLimiter limiter = new RateLimiter(2, 1, clock);
        assertEquals(Duration.ZERO, limiter.acquire("ada"));
        assertEquals(Duration.ZERO, limiter.acquire("ada"));
        assertEquals(Duration.ofSeconds(1), limiter.acquire("ada"));
        assertEquals(Duration.ZERO, limiter.acquire("bob"));
        assertEquals(2, limiter.clients());
        clock.advance(Duration.ofMillis(400));
        assertEquals(Duration.ofMillis(600), limiter.acquire("ada"));
    }

    @Test
    void fullBucketsCanBeForgotten() {
        RateLimiter limiter = new RateLimiter(2, 1, clock);
        limiter.acquire("ada");
        limiter.acquire("bob");
        limiter.acquire("bob");
        clock.advance(Duration.ofSeconds(1));
        // ada est de nouveau pleine, bob a encore un jeton de retard
        assertEquals(1, limiter.evictFull());
        assertEquals(1, limiter.clients());
        clock.advance(Duration.ofSeconds(1));
        assertEquals(1, limiter.evictFull());
        assertEquals(0, limiter.clients());
        assertEquals(Duration.ZERO, limiter.acquire("bob"));
    }
}
