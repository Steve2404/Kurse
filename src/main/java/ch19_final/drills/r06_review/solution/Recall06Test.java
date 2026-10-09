package ch19_final.drills.r06_review.solution;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les tests de reference du drill 6 : chacun montre un defaut de Data.Before. */
class Recall06Test {

    @Test
    void d01() {
        assertEquals(30, Fixes.totalCents(List.of("0.1", "0.2")));
        assertEquals(1999 + 29, Fixes.totalCents(List.of("19.99", "0.29")));
        assertEquals(0, Fixes.totalCents(List.of()));
        assertThrows(ArithmeticException.class, () -> Fixes.totalCents(List.of("1.001")));
        assertTrue(Fixes.isPromo(new String("DOUBLE")));
        assertFalse(Fixes.isPromo(null));
        assertFalse(Fixes.isPromo("double"));
    }

    @Test
    void d02() {
        assertEquals(List.of("BRONZE", "SILVER", "SILVER", "GOLD"),
                List.of(Fixes.tier(299), Fixes.tier(300), Fixes.tier(999), Fixes.tier(1000)));
        assertEquals(LocalDate.of(2025, 1, 14), Fixes.lastValidDay(LocalDate.of(2024, 1, 15)));
        assertEquals(LocalDate.of(2026, 1, 14), Fixes.lastValidDay(LocalDate.of(2025, 1, 15)));
        assertEquals(LocalDate.of(2025, 2, 27), Fixes.lastValidDay(LocalDate.of(2024, 2, 29)));
    }

    @Test
    void d03() {
        assertEquals(List.of("b", "a", "c"), Fixes.distinctInOrder(List.of("b", "a", "b", "c", "a")));
        List<String> big = new ArrayList<>();
        for (int i = 0; i < 200_000; i++) {
            big.add("t" + (i % 150_000));
        }
        List<String> distinct = assertTimeoutPreemptively(Duration.ofSeconds(2), () -> Fixes.distinctInOrder(big));
        assertEquals(150_000, distinct.size());
        assertEquals("t149999", distinct.get(149_999));
    }

    @Test
    void d04() {
        assertEquals("", Fixes.join(List.of()));
        assertEquals("a", Fixes.join(List.of("a")));
        assertEquals("a, b, c", Fixes.join(List.of("a", "b", "c")));
        List<String> many = new ArrayList<>();
        for (int i = 0; i < 100_000; i++) {
            many.add("tache " + i);
        }
        String joined = assertTimeoutPreemptively(Duration.ofSeconds(2), () -> Fixes.join(many));
        assertTrue(joined.endsWith("tache 99998, tache 99999"));
    }

    /** Un flux qui retient s'il a ete ferme, et peut echouer a la lecture. */
    static final class Spy extends Reader {
        private final Reader inner;
        private final boolean broken;
        boolean closed;

        Spy(String text, boolean broken) {
            this.inner = new StringReader(text);
            this.broken = broken;
        }

        @Override
        public int read(char[] buffer, int offset, int length) throws IOException {
            if (broken) {
                throw new IOException("disque illisible");
            }
            return inner.read(buffer, offset, length);
        }

        @Override
        public void close() {
            closed = true;
        }
    }

    @Test
    void d05() throws IOException {
        Spy ok = new Spy("a\nb\nc", false);
        assertEquals(3, Fixes.countLines(ok));
        assertTrue(ok.closed);
        Spy broken = new Spy("a", true);
        assertEquals("disque illisible", assertThrows(IOException.class, () -> Fixes.countLines(broken)).getMessage());
        assertTrue(broken.closed);
        assertEquals(0, Fixes.countLines(new StringReader("")));
    }

    @Test
    void d06() throws Exception {
        SafeCounter counter = new SafeCounter();
        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            CountDownLatch gate = new CountDownLatch(1);
            List<CompletableFuture<Void>> all = new ArrayList<>();
            for (int t = 0; t < 8; t++) {
                all.add(CompletableFuture.runAsync(() -> {
                    try {
                        gate.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    for (int i = 0; i < 10_000; i++) {
                        counter.increment();
                    }
                }, pool));
            }
            gate.countDown();
            CompletableFuture.allOf(all.toArray(new CompletableFuture[0])).join();
        } finally {
            pool.shutdownNow();
        }
        assertEquals(80_000, counter.value());
        assertEquals(1, new HashSet<>(List.of(new Customer("C1", "a@b.c"), new Customer("C1", "a@b.c"))).size());
        assertEquals("Customer[C1, a***@example.org]", new Customer("C1", "ada@example.org").toString());
        assertEquals("Customer[C2, ***]", new Customer("C2", "pas-une-adresse").toString());
    }
}
