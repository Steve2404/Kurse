package ch18_design.projects.p08_workflow.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WorkflowTest {

    /** Une horloge qu'on avance a la main (comme au projet 6). */
    static final class MutableClock extends Clock {
        private Instant now = Instant.parse("2026-10-09T10:00:00Z");

        void advance(Duration duration) {
            now = now.plus(duration);
        }

        @Override
        public Instant instant() {
            return now;
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }
    }

    private final MutableClock clock = new MutableClock();

    // Amene une commande neuve dans l'etat demande, par le chemin normal.
    private Order orderIn(String state) {
        Order order = new Order("CMD-1", 3400, clock);
        switch (state) {
            case "payee" -> order.pay();
            case "expediee" -> { order.pay(); order.ship(); }
            case "livree" -> { order.pay(); order.ship(); order.deliver(); }
            case "annulee" -> order.cancel();
            case "remboursee" -> { order.pay(); order.ship(); order.deliver(); order.refund(); }
            default -> { }
        }
        return order;
    }

    private static Consumer<Order> action(String name) {
        return switch (name) {
            case "payer" -> Order::pay;
            case "expedier" -> Order::ship;
            case "livrer" -> Order::deliver;
            case "annuler" -> Order::cancel;
            default -> Order::refund;
        };
    }

    // LE TABLEAU DES TRANSITIONS, case par case : 6 etats x 5 actions = 30 cas.
    @ParameterizedTest(name = "{0} + {1} -> {2}")
    @CsvSource({
            "nouvelle, payer, payee", "nouvelle, expedier, refus", "nouvelle, livrer, refus", "nouvelle, annuler, annulee", "nouvelle, rembourser, refus",
            "payee, payer, refus", "payee, expedier, expediee", "payee, livrer, refus", "payee, annuler, annulee", "payee, rembourser, refus",
            "expediee, payer, refus", "expediee, expedier, refus", "expediee, livrer, livree", "expediee, annuler, refus", "expediee, rembourser, refus",
            "livree, payer, refus", "livree, expedier, refus", "livree, livrer, refus", "livree, annuler, refus", "livree, rembourser, remboursee",
            "annulee, payer, refus", "annulee, expedier, refus", "annulee, livrer, refus", "annulee, annuler, refus", "annulee, rembourser, refus",
            "remboursee, payer, refus", "remboursee, expedier, refus", "remboursee, livrer, refus", "remboursee, annuler, refus", "remboursee, rembourser, refus"})
    void transitionTable(String from, String actionName, String expected) {
        Order order = orderIn(from);
        assertEquals(from, order.status());
        if (expected.equals("refus")) {
            IllegalStateException e = assertThrows(IllegalStateException.class, () -> action(actionName).accept(order));
            assertEquals("action refusee : " + actionName + " (commande " + from + ")", e.getMessage());
            assertEquals(from, order.status());
        } else {
            action(actionName).accept(order);
            assertEquals(expected, order.status());
        }
    }

    @Test
    void aRefusalChangesNothing() {
        Order order = orderIn("payee");
        assertThrows(IllegalStateException.class, order::deliver);
        assertEquals(List.of(new Transition("nouvelle", "payee")), order.transitions());
        assertEquals(0, order.refundedCents());
    }

    @Test
    void refunds() {
        Order paid = orderIn("payee");
        paid.cancel();
        assertEquals(3400, paid.refundedCents());
        Order fresh = orderIn("nouvelle");
        fresh.cancel();
        assertEquals(0, fresh.refundedCents());
        assertTrue(fresh.isClosed());
        assertFalse(orderIn("livree").isClosed());
    }

    @Test
    void refundUntilTheFourteenthDayIncluded() {
        Order order = orderIn("livree");
        clock.advance(Duration.ofDays(14).plusHours(13));
        order.refund();
        assertEquals("remboursee", order.status());
        assertEquals(3400, order.refundedCents());
        assertTrue(order.isClosed());
    }

    @Test
    void refundOnTheFifteenthDayIsRefused() {
        Order order = orderIn("livree");
        clock.advance(Duration.ofDays(15));
        assertEquals("delai de remboursement depasse", assertThrows(IllegalStateException.class, order::refund).getMessage());
        assertEquals("livree", order.status());
        assertEquals(0, order.refundedCents());
    }

    // Le delai part de la LIVRAISON, pas de la commande.
    @Test
    void theDelayStartsAtDelivery() {
        Order order = orderIn("expediee");
        clock.advance(Duration.ofDays(10));
        order.deliver();
        clock.advance(Duration.ofDays(14));
        order.refund();
        assertEquals("remboursee", order.status());
    }

    @Test
    void transitionsAreRecordedAndProtected() {
        Order order = orderIn("remboursee");
        assertEquals(List.of(new Transition("nouvelle", "payee"), new Transition("payee", "expediee"),
                new Transition("expediee", "livree"), new Transition("livree", "remboursee")), order.transitions());
        assertThrows(UnsupportedOperationException.class, () -> order.transitions().clear());
    }

    @Test
    void textReceipt() {
        assertEquals("""
                Commande CMD-1
                  nouvelle -> payee
                  payee -> annulee
                Etat final : annulee, rembourse : 3400 centimes
                """, new TextReceipt().export(cancelledAfterPayment()));
    }

    @Test
    void csvReceiptHasNoFooter() {
        assertEquals("""
                commande;de;vers
                CMD-1;nouvelle;payee
                CMD-1;payee;annulee
                """, new CsvReceipt().export(cancelledAfterPayment()));
        assertEquals("commande;de;vers\n", new CsvReceipt().export(orderIn("nouvelle")));
    }

    private Order cancelledAfterPayment() {
        Order order = orderIn("payee");
        order.cancel();
        return order;
    }
}
