package ch18_design.projects.p04_orders.solution;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Les adaptateurs se testent seuls, puis une fois tous branches par la racine de composition. */
class AdaptersTest {

    static final LocalDateTime NOON = LocalDateTime.of(2026, 10, 9, 12, 0);

    @Test
    void sequentialIdsStartAtOne() {
        SequentialIds ids = new SequentialIds("CMD-");
        assertEquals(List.of("CMD-1", "CMD-2", "CMD-3"), List.of(ids.next(), ids.next(), ids.next()));
    }

    @Test
    void inMemoryRepository() {
        InMemoryOrderRepository repository = new InMemoryOrderRepository();
        Order a = new Order("1", "ada", List.of("tarte"), 1850, NOON);
        Order b = new Order("2", "bob", List.of("tarte"), 1850, NOON);
        repository.save(a);
        repository.save(b);
        assertEquals(Optional.of(b), repository.find("2"));
        assertEquals(List.of(a), repository.byCustomer("ada"));
        assertTrue(repository.delete("1"));
        assertFalse(repository.delete("1"));
        assertEquals(Optional.empty(), repository.find("1"));
    }

    @Test
    void orderCopiesItsItems() {
        List<String> items = new ArrayList<>(List.of("tarte"));
        Order order = new Order("1", "ada", items, 1850, NOON);
        items.add("baguette");
        assertEquals(List.of("tarte"), order.items());
        assertThrows(UnsupportedOperationException.class, () -> order.items().add("x"));
    }

    @Test
    void consoleNotifierWritesToTheGivenStream() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ConsoleNotifier notifier = new ConsoleNotifier(new PrintStream(bytes, true, StandardCharsets.UTF_8));
        Order order = new Order("CMD-7", "ada@example.org", List.of("tarte"), 1850, NOON);
        notifier.orderConfirmed(order);
        notifier.orderCancelled(order);
        assertEquals(List.of("MAIL a ada@example.org : commande CMD-7 confirmee, total 1850 centimes",
                "MAIL a ada@example.org : commande CMD-7 annulee"), bytes.toString(StandardCharsets.UTF_8).lines().toList());
    }

    // Le test d'integration : les VRAIS adaptateurs, branches par BakeryApp, avec une horloge fixe.
    @Test
    void compositionRootWiresTheRealAdapters() {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        OrderService service = BakeryApp.create(Clock.fixed(Instant.parse("2026-10-09T08:30:00Z"), ZoneOffset.UTC),
                new PrintStream(bytes, true, StandardCharsets.UTF_8));
        Order order = service.place("ada@example.org", List.of("baguette", "croissant", "croissant"));
        assertEquals("CMD-1", order.id());
        assertEquals(340, order.totalCents());
        assertEquals("MAIL a ada@example.org : commande CMD-1 confirmee, total 340 centimes",
                bytes.toString(StandardCharsets.UTF_8).strip());
        assertEquals(List.of(order), service.history("ada@example.org"));
        assertEquals("article inconnu : pizza",
                assertThrows(IllegalArgumentException.class, () -> service.place("ada@example.org", List.of("pizza"))).getMessage());
    }
}
