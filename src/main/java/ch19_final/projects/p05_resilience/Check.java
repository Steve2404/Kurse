package ch19_final.projects.p05_resilience;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("TokenBucket.java", "milliTokens = Math.min(capacity, milliTokens + elapsedMillis * perSecond);", "milliTokens = milliTokens + elapsedMillis * perSecond;"),
            new Mutant("TokenBucket.java", "last = last.plusMillis(elapsedMillis);", "last = now;"),
            new Mutant("TokenBucket.java", "if (milliTokens < ONE) {", "if (milliTokens <= 0) {"),
            new Mutant("TokenBucket.java", "(missing + perSecond - 1) / perSecond", "missing / perSecond"),
            new Mutant("RateLimiter.java", "buckets.values().removeIf(TokenBucket::isFull);", "buckets.clear();"),
            new Mutant("RateLimiter.java", "buckets.computeIfAbsent(client, c -> new TokenBucket(capacity, perSecond, clock))", "new TokenBucket(capacity, perSecond, clock)"),
            new Mutant("CircuitBreaker.java", "if (state == State.HALF_OPEN || failures >= threshold) {", "if (failures >= threshold) {"),
            new Mutant("CircuitBreaker.java", "failures >= threshold", "failures > threshold"),
            new Mutant("CircuitBreaker.java", "    private synchronized void onSuccess() {\n        state = State.CLOSED;\n        failures = 0;", "    private synchronized void onSuccess() {\n        state = State.CLOSED;"),
            new Mutant("CircuitBreaker.java", "if (clock.instant().isBefore(reopen)) {", "if (!clock.instant().isAfter(reopen)) {"),
            new Mutant("CircuitBreaker.java", "            if (trialRunning) {", "            if (false) {"),
            new Mutant("CircuitBreaker.java", "                onSuccess();\n            }\n            throw e;", "                onFailure();\n            }\n            throw e;"),
            new Mutant("CircuitBreaker.java", "+ 999) / 1000", ") / 1000"),
            new Mutant("LatencyRecorder.java", "int rank = (int) Math.ceil(p / 100 * count);", "int rank = (int) Math.round(p / 100 * count);"),
            new Mutant("LatencyRecorder.java", "long[] sorted = Arrays.copyOf(window, count);", "long[] sorted = Arrays.copyOf(window, window.length);"),
            new Mutant("LatencyRecorder.java", "count = Math.min(count + 1, window.length);", "count = count + 1 > window.length ? 1 : count + 1;"),
            new Mutant("ResilientStock.java", "lastKnown.put(ref, new Known(quantity, clock.instant()));", "lastKnown.putIfAbsent(ref, new Known(quantity, clock.instant()));"),
            new Mutant("ResilientStock.java", "        } catch (IllegalArgumentException e) {\n            throw e; // une reference inconnue : le cache n'y peut rien, et ce n'est pas une panne\n", ""),
            new Mutant("ResilientStock.java", "            latency.record(Duration.between(start, clock.instant()));", "            latency.record(Duration.ZERO);"),
            new Mutant("ManualClock.java", "now = now.plus(duration);", "now = now.plus(duration.toSeconds(), java.time.temporal.ChronoUnit.SECONDS);"));

    static final List<String> API_CODE = List.of(
            "final class ManualClock extends Clock", "final class TokenBucket", "final class RateLimiter",
            "class CircuitOpenException extends RuntimeException", "final class CircuitBreaker", "enum State",
            "Predicate<RuntimeException>", "final class LatencyRecorder", "interface StockClient", "record StockAnswer(",
            "class StockUnavailableException extends RuntimeException", "final class ResilientStock",
            "final class ResilienceDemo", "synchronized", "computeIfAbsent(", "Arrays.sort(", "Math.ceil(", "finally",
            "Data.PartsSupplier", "!Thread.sleep", "!System.currentTimeMillis", "!System.nanoTime", "!Instant.now()",
            "max:method=15",
            "in:TokenBucket.java!double##les jetons se comptent en entiers (milliemes dans un long)",
            "in:CircuitBreaker.java!Thread##le disjoncteur ne dort pas et ne cree pas de fil",
            "in:ResilientStock.java!Data.##le stock protege ne connait que le port StockClient");

    static final List<String> API_TESTS = List.of(
            "ManualClock", "advance(", "@ParameterizedTest", "CountDownLatch", "assertThrows(", "getMessage()",
            "!System.out", "!Thread.sleep", "!Clock.system");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 35, MUTANTS, API_CODE, API_TESTS);
    }
}
