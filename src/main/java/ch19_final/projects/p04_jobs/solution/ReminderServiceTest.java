package ch19_final.projects.p04_jobs.solution;

import ch19_final.projects.p04_jobs.Data;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Des tests de concurrence DETERMINISTES : pas de Thread.sleep pour "laisser le temps", mais des verrous
 * (CountDownLatch) qui decident exactement quand chaque fil avance.
 */
class ReminderServiceTest {

    private static final Duration LONG = Duration.ofSeconds(5);

    private final Metrics metrics = new Metrics();
    private final List<String> sent = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger calls = new AtomicInteger();
    private final CountDownLatch release = new CountDownLatch(1);
    private final CountDownLatch started = new CountDownLatch(1);
    private final CountDownLatch finished = new CountDownLatch(1);
    private ReminderService service;

    /** Une passerelle rapide : "SMS-" + telephone ; "06...7" est toujours occupe, "05..." est inconnu. */
    private String quickGateway(String phone, String text) {
        calls.incrementAndGet();
        if (phone.startsWith("05")) {
            throw new GatewayException("numero inconnu : " + phone, false);
        }
        if (phone.endsWith("7")) {
            throw new GatewayException("operateur occupe", true);
        }
        sent.add(phone);
        return "SMS-" + phone;
    }

    /** Une passerelle qui attend le feu vert (release) : on controle exactement quand elle repond. */
    private String blockingGateway(String phone, String text) {
        calls.incrementAndGet();
        started.countDown();
        try {
            release.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new GatewayException("interrompu", false);
        }
        sent.add(phone);
        finished.countDown();
        return "SMS-" + phone;
    }

    private ReminderService service(SmsGateway gateway, int threads, int capacity, Duration timeout) {
        Retrier retrier = new Retrier(new RetryPolicy(3, Duration.ofMillis(100), 2, Duration.ofSeconds(1)), d -> { }, metrics);
        service = new ReminderService(gateway, retrier, threads, capacity, timeout, metrics);
        return service;
    }

    private static Notification n(long id, String phone) {
        return new Notification(id, phone, "Votre velo est pret");
    }

    @AfterEach
    void stop() {
        release.countDown();
        if (service != null) {
            service.close();
        }
    }

    @Test
    void sendAllGivesSortedResults() {
        List<SendResult> results = service(this::quickGateway, 3, 10, LONG).sendAll(List.of(
                n(3, "0600000003"), n(1, "0600000001"), n(5, "0500000005"), n(2, "0600000007"), n(4, "0700000004")));
        assertEquals(List.of(
                new SendResult(1, "envoye", "SMS-0600000001"),
                new SendResult(2, "echec", "operateur occupe"),
                new SendResult(3, "envoye", "SMS-0600000003"),
                new SendResult(4, "envoye", "SMS-0700000004"),
                new SendResult(5, "echec", "numero inconnu : 0500000005")), results);
        assertEquals(new Metrics.Snapshot(3, 2, 2, 0, 0), metrics.snapshot());
        assertEquals(7, calls.get());
    }

    @Test
    void sameTaskTwiceIsSentOnce() {
        ReminderService s = service(this::quickGateway, 2, 10, LONG);
        CompletableFuture<SendResult> first = s.send(n(1, "0600000001"));
        CompletableFuture<SendResult> second = s.send(n(1, "0600000001"));
        assertSame(first, second);
        assertEquals("envoye", second.join().status());
        assertEquals("envoye", s.send(n(1, "0600000001")).join().status());
        assertEquals(1, calls.get());
    }

    @Test
    void sameTaskFromManyThreadsIsSentOnce() {
        ReminderService s = service(this::quickGateway, 4, 50, LONG);
        ExecutorService callers = Executors.newFixedThreadPool(16);
        try {
            CountDownLatch gate = new CountDownLatch(1);
            List<CompletableFuture<CompletableFuture<SendResult>>> all = new ArrayList<>();
            for (int i = 0; i < 16; i++) {
                all.add(CompletableFuture.supplyAsync(() -> {
                    awaitQuietly(gate);
                    return s.send(n(42, "0600000042"));
                }, callers));
            }
            gate.countDown();
            all.forEach(f -> assertEquals("envoye", f.join().join().status()));
            assertEquals(1, calls.get());
        } finally {
            callers.shutdownNow();
        }
    }

    private static void awaitQuietly(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Test
    void fullQueueRefusesAtOnce() throws InterruptedException {
        ReminderService s = service(this::blockingGateway, 1, 2, LONG);
        CompletableFuture<SendResult> running = s.send(n(1, "0600000001"));
        assertTrue(started.await(5, TimeUnit.SECONDS));
        CompletableFuture<SendResult> queued1 = s.send(n(2, "0600000002"));
        CompletableFuture<SendResult> queued2 = s.send(n(3, "0600000003"));
        CompletableFuture<SendResult> refused = s.send(n(4, "0600000004"));
        assertTrue(refused.isDone());
        assertEquals(new SendResult(4, "refuse", "file pleine"), refused.join());
        assertEquals(1, metrics.snapshot().rejected());
        release.countDown();
        assertEquals(List.of("envoye", "envoye", "envoye"),
                List.of(running.join().status(), queued1.join().status(), queued2.join().status()));
    }

    @Test
    void refusedTaskCanBeAskedAgain() throws InterruptedException {
        ReminderService s = service(this::blockingGateway, 1, 1, LONG);
        s.send(n(1, "0600000001"));
        assertTrue(started.await(5, TimeUnit.SECONDS));
        CompletableFuture<SendResult> queued = s.send(n(2, "0600000002"));
        assertEquals("refuse", s.send(n(3, "0600000003")).join().status());
        release.countDown();
        queued.join();
        assertEquals("envoye", s.send(n(3, "0600000003")).join().status());
    }

    @Test
    void slowSendTimesOutButTheWorkGoesOn() throws InterruptedException {
        ReminderService s = service(this::blockingGateway, 1, 5, Duration.ofMillis(100));
        SendResult result = assertTimeoutPreemptively(Duration.ofSeconds(5), () -> s.send(n(1, "0600000001")).join());
        assertEquals(new SendResult(1, "delai depasse", "100 ms"), result);
        assertEquals(1, metrics.snapshot().timedOut());
        // Le delai a abandonne l'ATTENTE, pas le travail : l'envoi part quand meme.
        release.countDown();
        assertTrue(finished.await(5, TimeUnit.SECONDS));
        assertEquals(List.of("0600000001"), sent);
    }

    @Test
    void gracefulShutdownLetsWorkFinish() throws InterruptedException {
        ReminderService s = service(this::quickGateway, 2, 10, LONG);
        List<CompletableFuture<SendResult>> futures = List.of(s.send(n(1, "0600000001")), s.send(n(2, "0600000002")),
                s.send(n(3, "0600000003")));
        assertEquals(0, s.shutdown(Duration.ofSeconds(5)));
        futures.forEach(f -> assertEquals("envoye", f.join().status()));
    }

    @Test
    void shutdownInterruptsAfterTheGrace() throws InterruptedException {
        ReminderService s = service(this::blockingGateway, 1, 5, Duration.ofMillis(500));
        CompletableFuture<SendResult> running = s.send(n(1, "0600000001"));
        assertTrue(started.await(5, TimeUnit.SECONDS));
        CompletableFuture<SendResult> waiting1 = s.send(n(2, "0600000002"));
        CompletableFuture<SendResult> waiting2 = s.send(n(3, "0600000003"));
        assertEquals(2, s.shutdown(Duration.ofMillis(100)));
        assertEquals(new SendResult(1, "echec", "interrompu"), running.join());
        // Jamais commences : seul le delai les termine.
        assertEquals("delai depasse", waiting1.join().status());
        assertEquals("delai depasse", waiting2.join().status());
        assertEquals(1, calls.get());
    }

    @Test
    void sendAfterShutdownIsRefused() throws InterruptedException {
        ReminderService s = service(this::quickGateway, 1, 5, LONG);
        s.shutdown(Duration.ofSeconds(1));
        assertEquals(new SendResult(9, "refuse", "file pleine"), s.send(n(9, "0600000009")).join());
        assertEquals(0, calls.get());
    }

    @Test
    void operatorAdapterTranslatesErrors() {
        OperatorGateway gateway = new OperatorGateway(new Data.Operator());
        assertEquals("SMS-1", gateway.send("0612345670", "ok"));
        GatewayException busy = org.junit.jupiter.api.Assertions.assertThrows(GatewayException.class,
                () -> gateway.send("0612345671", "occupe une fois"));
        assertEquals("operateur occupe", busy.getMessage());
        assertTrue(busy.retryable());
        assertEquals("SMS-2", gateway.send("0612345671", "deuxieme essai"));
        GatewayException unknown = org.junit.jupiter.api.Assertions.assertThrows(GatewayException.class,
                () -> gateway.send("0512345674", "inconnu"));
        assertEquals("numero inconnu : 0512345674", unknown.getMessage());
        assertTrue(!unknown.retryable());
    }

    @Test
    void operatorAdapterKeepsTheInterruption() {
        OperatorGateway gateway = new OperatorGateway(new Data.Operator());
        Thread.currentThread().interrupt();
        try {
            GatewayException e = org.junit.jupiter.api.Assertions.assertThrows(GatewayException.class,
                    () -> gateway.send("0612345670", "ok"));
            assertEquals("interrompu", e.getMessage());
            assertTrue(Thread.currentThread().isInterrupted());
        } finally {
            Thread.interrupted();
        }
    }

    @Test
    void demoDataEndToEnd() {
        ReminderService s = service(new OperatorGateway(new Data.Operator()), 3, 20, LONG);
        List<SendResult> results = s.sendAll(Data.REMINDERS.stream()
                .map(r -> new Notification(Long.parseLong(r[0]), r[1], r[2])).toList());
        assertEquals(List.of("envoye", "envoye", "envoye", "envoye", "echec", "envoye", "echec", "envoye", "envoye",
                "envoye", "envoye", "envoye"), results.stream().map(SendResult::status).toList());
        assertEquals(new Metrics.Snapshot(10, 8, 2, 0, 0), metrics.snapshot());
    }
}
