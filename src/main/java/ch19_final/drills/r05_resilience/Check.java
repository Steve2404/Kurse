package ch19_final.drills.r05_resilience;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Les tests de REFERENCE (dans solution/) verifient TON code ; avec l'argument "solution", le corrige.
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            "d01 : 1 executions, 1 reussies",
            "d02 : 1 executions, 1 reussies",
            "d03 : 1 executions, 1 reussies",
            "d04 : 1 executions, 1 reussies",
            "d05 : 1 executions, 1 reussies",
            "d06 : 1 executions, 1 reussies");

    static final List<String> API = List.of(
            "final class ManualClock extends Clock", "final class Bucket", "final class Breaker", "enum State",
            "class BreakerOpenException extends RuntimeException", "final class Percentiles", "Math.ceil(",
            "final class LastKnown", "!Thread.sleep", "!System.currentTimeMillis", "!System.nanoTime", "!Instant.now()",
            "max:method=18", "in:Bucket.java!double##des milliemes de jeton dans un long");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
