package ch18_design.projects.p04_orders.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

/**
 * Le service se teste SANS base, SANS mail, SANS horloge reelle : on lui injecte des doublures.
 * Les doublures ecrites a la main sont imbriquees ici ; Mockito sert pour la panne et les verifications.
 */
class OrderServiceTest {

    // Un vendredi a 10 h 15, en UTC : la boutique est ouverte.
    static final Instant FRIDAY_10_15 = Instant.parse("2026-10-09T10:15:00Z");
    static final Catalog CATALOG = item -> Optional.ofNullable(Map.of("baguette", 120L, "croissant", 110L).get(item));

    /** Une doublure ecrite a la main : elle NOTE ce qu'on lui demande, pour qu'on le verifie ensuite. */
    static final class RecordingNotifier implements Notifier {
        final List<String> events = new ArrayList<>();

        @Override
        public void orderConfirmed(Order order) {
            events.add("confirmee " + order.id());
        }

        @Override
        public void orderCancelled(Order order) {
            events.add("annulee " + order.id());
        }
    }

    private final InMemoryOrderRepository repository = new InMemoryOrderRepository();
    private final RecordingNotifier notifier = new RecordingNotifier();

    private static Clock at(String isoInstant) {
        return Clock.fixed(Instant.parse(isoInstant), ZoneOffset.UTC);
    }

    private OrderService serviceAt(Clock clock) {
        return new OrderService(repository, notifier, CATALOG, new SequentialIds("T-"), clock);
    }

    @Test
    void placeSavesNotifiesAndReturnsTheOrder() {
        Order order = serviceAt(Clock.fixed(FRIDAY_10_15, ZoneOffset.UTC)).place("ada", List.of("baguette", "croissant", "croissant"));
        assertEquals(new Order("T-1", "ada", List.of("baguette", "croissant", "croissant"), 340,
                LocalDateTime.of(2026, 10, 9, 10, 15)), order);
        assertEquals(Optional.of(order), repository.find("T-1"));
        assertEquals(List.of("confirmee T-1"), notifier.events);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2026-10-09T06:59:59Z", "2026-10-09T19:00:00Z", "2026-10-11T10:00:00Z"})
    void closedShopRefusesAndTouchesNothing(String instant) {
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> serviceAt(at(instant)).place("ada", List.of("baguette")));
        assertEquals("boutique fermee", e.getMessage());
        assertEquals(List.of(), repository.byCustomer("ada"));
        assertEquals(List.of(), notifier.events);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2026-10-09T07:00:00Z", "2026-10-09T18:59:59Z", "2026-10-10T12:00:00Z"})
    void openingHoursAreInclusiveAt7AndExclusiveAt19(String instant) {
        assertEquals("T-1", serviceAt(at(instant)).place("ada", List.of("baguette")).id());
    }

    @Test
    void unknownItemAndEmptyOrderAreRefused() {
        OrderService service = serviceAt(Clock.fixed(FRIDAY_10_15, ZoneOffset.UTC));
        assertEquals("article inconnu : pizza",
                assertThrows(IllegalArgumentException.class, () -> service.place("ada", List.of("baguette", "pizza"))).getMessage());
        assertEquals("commande vide",
                assertThrows(IllegalArgumentException.class, () -> service.place("ada", List.of())).getMessage());
        assertEquals(List.of(), repository.byCustomer("ada"));
    }

    // Avec Mockito : un mail en panne ne doit pas faire perdre la commande.
    @Test
    void failingNotifierDoesNotLoseTheOrder() {
        Notifier broken = mock(Notifier.class);
        doThrow(new IllegalStateException("serveur de mail en panne")).when(broken).orderConfirmed(any());
        OrderService service = new OrderService(repository, broken, CATALOG, () -> "M-1", Clock.fixed(FRIDAY_10_15, ZoneOffset.UTC));
        Order order = service.place("ada", List.of("baguette"));
        assertEquals(Optional.of(order), repository.find("M-1"));
        verify(broken).orderConfirmed(order);
    }

    @Test
    void refusedOrderNeverReachesTheNotifier() {
        Notifier silent = mock(Notifier.class);
        OrderService service = new OrderService(repository, silent, CATALOG, () -> "M-1", at("2026-10-11T10:00:00Z"));
        assertThrows(IllegalStateException.class, () -> service.place("ada", List.of("baguette")));
        verifyNoInteractions(silent);
    }

    // Deux services sur le MEME depot, a deux heures differentes : l'horloge injectee fait avancer le temps.
    @Test
    void cancelWithinThirtyMinutesIncluded() {
        serviceAt(Clock.fixed(FRIDAY_10_15, ZoneOffset.UTC)).place("ada", List.of("baguette"));
        assertTrue(serviceAt(at("2026-10-09T10:45:00Z")).cancel("T-1"));
        assertEquals(Optional.empty(), repository.find("T-1"));
        assertEquals(List.of("confirmee T-1", "annulee T-1"), notifier.events);
    }

    @Test
    void cancelTooLateOrUnknownIsRefused() {
        serviceAt(Clock.fixed(FRIDAY_10_15, ZoneOffset.UTC)).place("ada", List.of("baguette"));
        assertFalse(serviceAt(at("2026-10-09T10:45:01Z")).cancel("T-1"));
        assertFalse(serviceAt(at("2026-10-09T10:20:00Z")).cancel("T-9"));
        assertTrue(repository.find("T-1").isPresent());
        assertEquals(List.of("confirmee T-1"), notifier.events);
    }

    @Test
    void historyListsTheCustomerOrdersInOrder() {
        OrderService service = serviceAt(Clock.fixed(FRIDAY_10_15, ZoneOffset.UTC));
        service.place("ada", List.of("baguette"));
        service.place("bob", List.of("croissant"));
        service.place("ada", List.of("croissant"));
        assertEquals(List.of("T-1", "T-3"), service.history("ada").stream().map(Order::id).toList());
    }
}
