package ch19_final.projects.p04_jobs;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("RetryPolicy.java", "Math.pow(multiplier, failures - 1)", "Math.pow(multiplier, failures)"),
            new Mutant("RetryPolicy.java", "millis >= maxDelay.toMillis() ? maxDelay", "false ? maxDelay"),
            new Mutant("RetryPolicy.java", "if (maxAttempts < 1) {", "if (maxAttempts < 0) {"),
            new Mutant("Retrier.java", "if (!e.retryable() || attempt == policy.maxAttempts()) {", "if (attempt == policy.maxAttempts()) {"),
            new Mutant("Retrier.java", "                    earlier.forEach(e::addSuppressed);\n", ""),
            new Mutant("Retrier.java", "                earlier.add(e);\n", ""),
            new Mutant("Retrier.java", "            Thread.currentThread().interrupt();\n", ""),
            new Mutant("Retrier.java", "        metrics.retry();\n", ""),
            new Mutant("Retrier.java", "policy.delayAfter(failures)", "policy.delayAfter(1)"),
            new Mutant("ReminderService.java", "accepted.computeIfAbsent(n.taskId(), id -> start(n))", "start(n)"),
            new Mutant("ReminderService.java", "            accepted.remove(n.taskId(), result);\n", ""),
            new Mutant("ReminderService.java", "new ArrayBlockingQueue<>(queueCapacity)", "new java.util.concurrent.LinkedBlockingQueue<>()"),
            new Mutant("ReminderService.java", "if (cause instanceof TimeoutException) {", "if (cause instanceof java.util.concurrent.CancellationException) {"),
            new Mutant("ReminderService.java", "e instanceof CompletionException && e.getCause() != null ? e.getCause() : e", "e"),
            new Mutant("ReminderService.java", "                .sorted(Comparator.comparingLong(SendResult::taskId))", ""),
            new Mutant("ReminderService.java", "        return executor.shutdownNow().size();", "        executor.shutdownNow();\n        return 0;"),
            new Mutant("ReminderService.java", "            metrics.rejected();\n", ""),
            new Mutant("OperatorGateway.java", "throw new GatewayException(e.getMessage(), true);", "throw new GatewayException(e.getMessage(), false);"),
            new Mutant("OperatorGateway.java", "            Thread.currentThread().interrupt();\n", ""),
            new Mutant("Metrics.java", "failed.sum(), rejected.sum()", "rejected.sum(), failed.sum()"));

    static final List<String> API_CODE = List.of(
            "record Notification(", "class GatewayException extends RuntimeException", "interface SmsGateway",
            "interface Sleeper", "record RetryPolicy(", "final class Metrics", "LongAdder", "final class Retrier",
            "addSuppressed", "Thread.currentThread().interrupt()", "record SendResult(",
            "final class ReminderService implements AutoCloseable", "ThreadPoolExecutor", "ArrayBlockingQueue",
            "RejectedExecutionException", "CompletableFuture.supplyAsync(", "orTimeout(", "exceptionally(",
            "computeIfAbsent(", "ConcurrentHashMap", "awaitTermination(", "shutdownNow()",
            "final class OperatorGateway implements SmsGateway", "final class ReminderDemo", "Data.REMINDERS",
            "!Executors.newCachedThreadPool", "!LinkedBlockingQueue", "!printStackTrace",
            "max:method=15",
            "in:Retrier.java!Thread.sleep##le Retrier attend avec le Sleeper injecte",
            "in:ReminderService.java!Thread.sleep##le service ne dort jamais",
            "in:Retrier.java!catch (RuntimeException##on ne reessaie que les GatewayException",
            "in:ReminderService.java!Data.##le service ne connait que le port SmsGateway");

    static final List<String> API_TESTS = List.of(
            "CountDownLatch", "assertSame(", "getSuppressed()", "isInterrupted()", "sendAll(", "shutdown(Duration",
            "assertTimeoutPreemptively(", "@AfterEach", "!System.out", "!Thread.sleep", "!Sleeper.REAL");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 25, MUTANTS, API_CODE, API_TESTS);
    }
}
