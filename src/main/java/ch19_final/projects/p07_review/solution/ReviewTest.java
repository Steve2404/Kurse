package ch19_final.projects.p07_review.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Une revue de code prouvee par des tests : chaque defaut de la demande de fusion du collegue a son test,
 * qui echouerait avec son code et passe avec le code corrige. L'horloge est loin d'aujourd'hui (janvier 2025) :
 * un LocalDate.now() oublie dans le code se voit tout de suite.
 */
class ReviewTest {

    private static final LocalDate TODAY = LocalDate.of(2025, 1, 14);
    private static final Clock CLOCK = Clock.fixed(Instant.parse("2025-01-14T12:00:00Z"), ZoneOffset.UTC);

    private LoyaltyService serviceWithAda() {
        LoyaltyService service = new LoyaltyService(CLOCK);
        service.register(new Customer("C1", "Ada", "ada@example.org"));
        return service;
    }

    private static Purchase buy(long cents, LocalDate date) {
        return new Purchase("C1", cents, date, null);
    }

    // ------------------------------------------------------------------ 1. equals sans hashCode

    @Test
    void equalCustomersAreOneInAHashSet() {
        Set<Customer> set = new HashSet<>(List.of(new Customer("C1", "Ada", "a@b.c"), new Customer("C1", "Ada", "a@b.c")));
        assertEquals(1, set.size());
        assertEquals(new Customer("C1", "Ada", "a@b.c").hashCode(), new Customer("C1", "Ada", "a@b.c").hashCode());
    }

    // ------------------------------------------------------------------ 2. l'argent en double

    @Test
    void moneyIsExact() {
        LoyaltyService service = serviceWithAda();
        service.record(buy(10, TODAY));
        service.record(buy(20, TODAY));
        assertEquals(30, service.totalSpentCents("C1"));
    }

    @ParameterizedTest
    @CsvSource({"19.99, 1999", "0.29, 29", "0.1, 10", "10, 1000", "1234567.89, 123456789"})
    void importedAmountsAreExactCents(String amount, long cents) throws IOException {
        assertEquals(cents, PurchaseImporter.read(new StringReader("C1;" + amount + ";2025-01-01")).get(0).cents());
    }

    // ------------------------------------------------------------------ 3. == sur des String

    @Test
    void promoCodeTypedByTheCustomerWorks() {
        LoyaltyService service = serviceWithAda();
        assertEquals(100, service.record(new Purchase("C1", 5000, TODAY, new String("DOUBLE"))));
    }

    @Test
    void promoCodeReadFromAFileWorks() throws IOException {
        LoyaltyService service = serviceWithAda();
        Purchase imported = PurchaseImporter.read(new StringReader("C1;50.00;2025-01-10;DOUBLE")).get(0);
        assertEquals(100, service.record(imported));
    }

    // ------------------------------------------------------------------ 4. les paliers : "a partir de"

    @ParameterizedTest
    @CsvSource({"0, BRONZE", "299, BRONZE", "300, SILVER", "999, SILVER", "1000, GOLD", "5000, GOLD"})
    void tierThresholdsAreInclusive(int points, Tier expected) {
        assertEquals(expected, Tier.of(points));
    }

    @Test
    void goldCustomersEarnDouble() {
        LoyaltyService service = serviceWithAda();
        assertEquals(1000, service.record(buy(100_000, TODAY)));
        assertEquals(Tier.GOLD, service.tier("C1"));
        assertEquals(20, service.record(buy(1000, TODAY)));
        assertEquals(40, service.record(new Purchase("C1", 1000, TODAY, "DOUBLE")));
    }

    @Test
    void onePointPerFullEuro() {
        LoyaltyService service = serviceWithAda();
        assertEquals(9, service.record(buy(999, TODAY)));
        assertEquals(0, service.record(buy(99, TODAY)));
        assertEquals(1, service.record(buy(150, TODAY)));
    }

    // ------------------------------------------------------------------ 5. l'heure cachee, 6. les annees bissextiles

    @Test
    void pointsExpireOneYearLaterEvenAfterALeapYear() {
        LoyaltyService service = serviceWithAda();
        service.record(buy(5000, LocalDate.of(2024, 1, 15)));
        service.record(buy(3000, LocalDate.of(2024, 1, 14)));
        // le 14 janvier 2025 : le lot du 15 janvier 2024 vaut encore (365 jours seulement : 2024 est bissextile)
        assertEquals(50, service.balance("C1"));
    }

    @Test
    void futurePurchasesAreRefused() {
        LoyaltyService service = serviceWithAda();
        assertEquals("achat dans le futur : 2025-01-15",
                assertThrows(IllegalArgumentException.class, () -> service.record(buy(100, TODAY.plusDays(1)))).getMessage());
        assertEquals(1, service.record(buy(100, TODAY)));
    }

    // ------------------------------------------------------------------ 7. l'erreur avalee, 8. le flux jamais ferme

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "C1;10;2025-01-01\\nC1;dix;2025-01-02\\nC1;30;2025-01-03 | ligne 2 : montant invalide : dix",
            "C1;10.001;2025-01-01                                 | ligne 1 : montant invalide : 10.001",
            "C1;-5;2025-01-01                                     | ligne 1 : montant invalide : -5",
            "C1;10;2025-13-01                                     | ligne 1 : date invalide : 2025-13-01",
            "\\nC1;10                                              | ligne 2 : 3 ou 4 champs attendus",
            "C1;10;2025-01-01;A;B                                 | ligne 1 : 3 ou 4 champs attendus"})
    void badLinesStopTheImportWithTheirNumber(String text, String message) {
        String content = text.strip().replace("\\n", "\n");
        assertEquals(message, assertThrows(IllegalArgumentException.class,
                () -> PurchaseImporter.read(new StringReader(content))).getMessage());
    }

    @Test
    void importReadsEverythingSkippingBlankLines() throws IOException {
        List<Purchase> all = PurchaseImporter.read(new StringReader("C1;10;2025-01-01\n\nC2;0.50;2025-01-02;\nC1;3;2025-01-03;DOUBLE\n"));
        assertEquals(List.of(new Purchase("C1", 1000, LocalDate.of(2025, 1, 1), null),
                new Purchase("C2", 50, LocalDate.of(2025, 1, 2), null),
                new Purchase("C1", 300, LocalDate.of(2025, 1, 3), "DOUBLE")), all);
    }

    /** Un flux qui retient s'il a ete ferme. */
    static final class TrackingReader extends Reader {
        private final Reader inner;
        boolean closed;

        TrackingReader(String text) {
            this.inner = new StringReader(text);
        }

        @Override
        public int read(char[] buffer, int offset, int length) throws IOException {
            return inner.read(buffer, offset, length);
        }

        @Override
        public void close() throws IOException {
            closed = true;
            inner.close();
        }
    }

    @Test
    void readerIsClosedAfterSuccess() throws IOException {
        TrackingReader reader = new TrackingReader("C1;10;2025-01-01");
        PurchaseImporter.read(reader);
        assertTrue(reader.closed);
    }

    @Test
    void readerIsClosedEvenAfterAnError() {
        TrackingReader reader = new TrackingReader("C1;dix;2025-01-01");
        assertThrows(IllegalArgumentException.class, () -> PurchaseImporter.read(reader));
        assertTrue(reader.closed);
    }

    // ------------------------------------------------------------------ 9. la liste interne exposee

    @Test
    void historyCannotBeChangedFromOutside() {
        LoyaltyService service = serviceWithAda();
        service.record(buy(1000, TODAY));
        List<Purchase> history = service.history("C1");
        assertThrows(UnsupportedOperationException.class, () -> history.add(buy(1, TODAY)));
        service.record(buy(2000, TODAY));
        assertEquals(1, history.size());
        assertEquals(2, service.history("C1").size());
    }

    // ------------------------------------------------------------------ 10. l'etat static partage

    @Test
    void twoServicesDoNotShareCustomers() {
        LoyaltyService first = serviceWithAda();
        first.record(buy(50_000, TODAY));
        LoyaltyService second = serviceWithAda();
        assertEquals(0, second.balance("C1"));
        assertEquals(500, first.balance("C1"));
        LoyaltyService empty = new LoyaltyService(CLOCK);
        assertEquals("client inconnu : C1", assertThrows(NoSuchElementException.class, () -> empty.balance("C1")).getMessage());
    }

    // ------------------------------------------------------------------ 11. plusieurs fils

    @Test
    void concurrentPurchasesAreAllKept() throws Exception {
        LoyaltyService service = serviceWithAda();
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
                    for (int i = 0; i < 500; i++) {
                        service.record(buy(100, TODAY));
                    }
                }, pool));
            }
            gate.countDown();
            CompletableFuture.allOf(all.toArray(new CompletableFuture[0])).join();
        } finally {
            pool.shutdownNow();
        }
        assertEquals(4000, service.history("C1").size());
        assertEquals(400_000, service.totalSpentCents("C1"));
    }

    // ------------------------------------------------------------------ 12. la donnee personnelle dans les journaux

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {"ada@example.org | Customer[C1, Ada, a***@example.org]",
            "bob.martin@example.org | Customer[C1, Ada, b***@example.org]", "pas-une-adresse | Customer[C1, Ada, ***]",
            "@example.org | Customer[C1, Ada, ***]"})
    void toStringHidesTheEmail(String email, String expected) {
        assertEquals(expected, new Customer("C1", "Ada", email).toString());
    }

    // ------------------------------------------------------------------ les regles qui restent

    @Test
    void registeringTwiceIsRefused() {
        LoyaltyService service = serviceWithAda();
        assertEquals("client deja inscrit : C1", assertThrows(IllegalArgumentException.class,
                () -> service.register(new Customer("C1", "Ada bis", "x@y.z"))).getMessage());
        assertThrows(NoSuchElementException.class, () -> service.record(new Purchase("C9", 100, TODAY, null)));
        assertThrows(IllegalArgumentException.class, () -> new Purchase("C1", -1, TODAY, null));
    }
}
