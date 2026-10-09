package ch19_final.drills.r04_concurrency;

import projectkit.TestKit;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
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
            "interface Sleeper", "class RetryableException extends RuntimeException", "final class Retry", "addSuppressed",
            "Thread.currentThread().interrupt()", "final class BoundedPool", "ArrayBlockingQueue", "AbortPolicy",
            "final class Async", "orTimeout(", "exceptionally(", "CompletionException", "final class OnceCache",
            "computeIfAbsent(", "final class Shutdown", "awaitTermination(", "shutdownNow()", "!Thread.sleep",
            "!LinkedBlockingQueue", "max:method=18");

    public static void main(String[] args) throws Exception {
        TestKit.checkRecall(Check.class, args, EXPECTED, API);
    }
}
