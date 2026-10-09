package ch19_final.projects.p05_resilience.solution;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CircuitBreakerTest {

    private final ManualClock clock = new ManualClock(Instant.parse("2026-10-09T10:00:00Z"));
    private final CircuitBreaker breaker = new CircuitBreaker(3, Duration.ofSeconds(10), clock,
            e -> !(e instanceof IllegalArgumentException));
    private final AtomicInteger calls = new AtomicInteger();

    private String ok() {
        calls.incrementAndGet();
        return "ok";
    }

    private String down() {
        calls.incrementAndGet();
        throw new IllegalStateException("en panne");
    }

    private void fail(int times) {
        for (int i = 0; i < times; i++) {
            assertEquals("en panne", assertThrows(IllegalStateException.class, () -> breaker.call(this::down)).getMessage());
        }
    }

    @Test
    void closedLetsCallsThrough() {
        assertEquals("ok", breaker.call(this::ok));
        assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
    }

    @Test
    void thresholdFailuresInARowOpenIt() {
        fail(2);
        assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
        fail(1);
        assertEquals(CircuitBreaker.State.OPEN, breaker.state());
        assertEquals(3, calls.get());
    }

    @Test
    void aSuccessResetsTheCount() {
        fail(2);
        breaker.call(this::ok);
        fail(2);
        assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
    }

    @Test
    void openRefusesWithoutCallingAndSaysWhen() {
        fail(3);
        CircuitOpenException e = assertThrows(CircuitOpenException.class, () -> breaker.call(this::ok));
        assertEquals("circuit ouvert : reessayer dans 10 s", e.getMessage());
        clock.advance(Duration.ofMillis(6500));
        assertEquals("circuit ouvert : reessayer dans 4 s",
                assertThrows(CircuitOpenException.class, () -> breaker.call(this::ok)).getMessage());
        clock.advance(Duration.ofMillis(3499));
        assertEquals("circuit ouvert : reessayer dans 1 s",
                assertThrows(CircuitOpenException.class, () -> breaker.call(this::ok)).getMessage());
        assertEquals(3, calls.get());
    }

    @Test
    void afterTheDelayOneTrialClosesItAgain() {
        fail(3);
        clock.advance(Duration.ofSeconds(10));
        assertEquals("ok", breaker.call(this::ok));
        assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
        fail(2);
        assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
    }

    @Test
    void failedTrialReopensForAFullDelay() {
        fail(3);
        clock.advance(Duration.ofSeconds(12));
        fail(1);
        assertEquals(CircuitBreaker.State.OPEN, breaker.state());
        assertEquals("circuit ouvert : reessayer dans 10 s",
                assertThrows(CircuitOpenException.class, () -> breaker.call(this::ok)).getMessage());
    }

    @Test
    void onlyOneTrialAtATime() throws Exception {
        fail(3);
        clock.advance(Duration.ofSeconds(10));
        CountDownLatch inTrial = new CountDownLatch(1);
        CountDownLatch finish = new CountDownLatch(1);
        CompletableFuture<String> trial = CompletableFuture.supplyAsync(() -> breaker.call(() -> {
            inTrial.countDown();
            try {
                finish.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return "essai";
        }));
        assertTrue(inTrial.await(5, TimeUnit.SECONDS));
        assertEquals(CircuitBreaker.State.HALF_OPEN, breaker.state());
        assertEquals("circuit ouvert : essai en cours",
                assertThrows(CircuitOpenException.class, () -> breaker.call(this::ok)).getMessage());
        finish.countDown();
        assertEquals("essai", trial.get(5, TimeUnit.SECONDS));
        assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
    }

    @Test
    void clientErrorsAreNotFailures() {
        for (int i = 0; i < 5; i++) {
            assertThrows(IllegalArgumentException.class, () -> breaker.call(() -> {
                throw new IllegalArgumentException("reference inconnue");
            }));
        }
        assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
        fail(2);
        assertThrows(IllegalArgumentException.class, () -> breaker.call(() -> {
            throw new IllegalArgumentException("reference inconnue");
        }));
        fail(2);
        assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
    }

    @Test
    void clientErrorDuringTrialClosesIt() {
        fail(3);
        clock.advance(Duration.ofSeconds(10));
        assertThrows(IllegalArgumentException.class, () -> breaker.call(() -> {
            throw new IllegalArgumentException("reference inconnue");
        }));
        assertEquals(CircuitBreaker.State.CLOSED, breaker.state());
    }
}
