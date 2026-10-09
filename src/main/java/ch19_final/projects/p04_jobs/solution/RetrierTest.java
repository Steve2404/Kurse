package ch19_final.projects.p04_jobs.solution;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RetrierTest {

    private static final RetryPolicy POLICY = new RetryPolicy(4, Duration.ofMillis(100), 2, Duration.ofSeconds(1));

    private final List<Duration> sleeps = new ArrayList<>();
    private final Metrics metrics = new Metrics();
    private final AtomicInteger calls = new AtomicInteger();

    // Le Sleeper des tests ne dort pas : il note.
    private Retrier retrier(RetryPolicy policy) {
        return new Retrier(policy, sleeps::add, metrics);
    }

    /** Un appel qui echoue "occupe 1", "occupe 2"... failures fois, puis reussit. */
    private String busyThenOk(int failures) {
        int n = calls.incrementAndGet();
        if (n <= failures) {
            throw new GatewayException("occupe " + n, true);
        }
        return "SMS-" + n;
    }

    @AfterEach
    void clearInterruptFlag() {
        Thread.interrupted();
    }

    @ParameterizedTest
    @CsvSource({"1, 100", "2, 200", "3, 400", "4, 800", "5, 1000", "10, 1000"})
    void delayDoublesUpToTheCap(int failures, long millis) {
        assertEquals(Duration.ofMillis(millis), new RetryPolicy(20, Duration.ofMillis(100), 2, Duration.ofSeconds(1)).delayAfter(failures));
    }

    @Test
    void delayWithAnotherMultiplier() {
        RetryPolicy policy = new RetryPolicy(5, Duration.ofMillis(100), 1.5, Duration.ofMinutes(1));
        assertEquals(List.of(100L, 150L, 225L, 338L), List.of(policy.delayAfter(1).toMillis(), policy.delayAfter(2).toMillis(),
                policy.delayAfter(3).toMillis(), policy.delayAfter(4).toMillis()));
        assertEquals(Duration.ofMillis(100), new RetryPolicy(3, Duration.ofMillis(100), 1, Duration.ofSeconds(1)).delayAfter(3));
    }

    @Test
    void policyIsChecked() {
        assertEquals("au moins 1 essai : 0", assertThrows(IllegalArgumentException.class,
                () -> new RetryPolicy(0, Duration.ofMillis(1), 2, Duration.ofSeconds(1))).getMessage());
        assertEquals("multiplicateur inferieur a 1 : 0.5", assertThrows(IllegalArgumentException.class,
                () -> new RetryPolicy(3, Duration.ofMillis(1), 0.5, Duration.ofSeconds(1))).getMessage());
    }

    @Test
    void successAtFirstTryDoesNotWait() {
        assertEquals("SMS-1", retrier(POLICY).call(() -> busyThenOk(0)));
        assertEquals(List.of(), sleeps);
        assertEquals(0, metrics.snapshot().retries());
    }

    @Test
    void twoFailuresThenSuccess() {
        assertEquals("SMS-3", retrier(POLICY).call(() -> busyThenOk(2)));
        assertEquals(List.of(Duration.ofMillis(100), Duration.ofMillis(200)), sleeps);
        assertEquals(2, metrics.snapshot().retries());
        assertEquals(3, calls.get());
    }

    @Test
    void lastAttemptSucceeds() {
        assertEquals("SMS-4", retrier(POLICY).call(() -> busyThenOk(3)));
        assertEquals(3, sleeps.size());
    }

    @Test
    void allAttemptsFailKeepsEveryError() {
        GatewayException e = assertThrows(GatewayException.class, () -> retrier(POLICY).call(() -> busyThenOk(99)));
        assertEquals("occupe 4", e.getMessage());
        assertEquals(List.of("occupe 1", "occupe 2", "occupe 3"),
                Arrays.stream(e.getSuppressed()).map(Throwable::getMessage).toList());
        assertEquals(4, calls.get());
        assertEquals(List.of(Duration.ofMillis(100), Duration.ofMillis(200), Duration.ofMillis(400)), sleeps);
        assertEquals(3, metrics.snapshot().retries());
    }

    @Test
    void nonRetryableErrorStopsAtOnce() {
        GatewayException e = assertThrows(GatewayException.class, () -> retrier(POLICY).call(() -> {
            calls.incrementAndGet();
            throw new GatewayException("numero inconnu", false);
        }));
        assertEquals("numero inconnu", e.getMessage());
        assertEquals(0, e.getSuppressed().length);
        assertEquals(1, calls.get());
        assertEquals(List.of(), sleeps);
    }

    @Test
    void retryableThenNonRetryable() {
        GatewayException e = assertThrows(GatewayException.class, () -> retrier(POLICY).call(() -> {
            if (calls.incrementAndGet() == 1) {
                throw new GatewayException("occupe", true);
            }
            throw new GatewayException("numero inconnu", false);
        }));
        assertEquals("numero inconnu", e.getMessage());
        assertEquals(List.of("occupe"), Arrays.stream(e.getSuppressed()).map(Throwable::getMessage).toList());
        assertEquals(2, calls.get());
    }

    @Test
    void oneAttemptMeansNoRetry() {
        RetryPolicy once = new RetryPolicy(1, Duration.ofMillis(100), 2, Duration.ofSeconds(1));
        assertThrows(GatewayException.class, () -> retrier(once).call(() -> busyThenOk(1)));
        assertEquals(1, calls.get());
        assertEquals(List.of(), sleeps);
    }

    @Test
    void otherExceptionsAreNotRetried() {
        assertThrows(NullPointerException.class, () -> retrier(POLICY).call(() -> {
            calls.incrementAndGet();
            throw new NullPointerException("bug");
        }));
        assertEquals(1, calls.get());
    }

    @Test
    void interruptionDuringTheWaitStopsAndKeepsTheFlag() {
        Retrier interrupted = new Retrier(POLICY, d -> {
            throw new InterruptedException();
        }, metrics);
        GatewayException e = assertThrows(GatewayException.class, () -> interrupted.call(() -> busyThenOk(5)));
        assertEquals("interrompu pendant l'attente", e.getMessage());
        assertFalse(e.retryable());
        assertEquals(List.of("occupe 1"), Arrays.stream(e.getSuppressed()).map(Throwable::getMessage).toList());
        assertTrue(Thread.currentThread().isInterrupted());
        assertEquals(1, calls.get());
    }
}
