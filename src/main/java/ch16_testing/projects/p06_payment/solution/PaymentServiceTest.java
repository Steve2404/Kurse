package ch16_testing.projects.p06_payment.solution;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/** Les tests de reference du projet 6 : Mockito fabrique les doublures et verifie les appels. */
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    OrderRepository orders;
    @Mock
    PaymentGateway gateway;
    @Mock
    Mailer mailer;
    // Pourquoi @InjectMocks : Mockito appelle le constructeur de PaymentService avec les trois simulacres.
    @InjectMocks
    PaymentService service;

    static final Order NEW_ORDER = new Order("C-1", "ana", "ana@mail.test", 4250, OrderStatus.NEW);

    @Test
    void approvedPaymentSavesPaidOrderThenSendsAReceipt() {
        when(orders.find("C-1")).thenReturn(Optional.of(NEW_ORDER));
        when(gateway.charge("ana", 4250)).thenReturn(new ChargeResult(true, "T-77", null));

        assertEquals("T-77", service.pay("C-1"));

        // Pourquoi InOrder : la banque, PUIS le depot, PUIS le courriel.
        InOrder order = inOrder(gateway, orders, mailer);
        order.verify(gateway).charge("ana", 4250);
        order.verify(orders).save(NEW_ORDER.withStatus(OrderStatus.PAID));
        order.verify(mailer).send("ana@mail.test", "Recu C-1", "Montant : 4250 ; transaction T-77");
    }

    @Test
    void declinedPaymentMarksTheOrderFailedAndTellsWhy() {
        when(orders.find("C-1")).thenReturn(Optional.of(NEW_ORDER));
        when(gateway.charge(anyString(), anyLong())).thenReturn(new ChargeResult(false, null, "plafond atteint"));

        assertEquals("REFUSE", service.pay("C-1"));

        // Pourquoi un ArgumentCaptor : on recupere l'objet passe a save pour l'examiner.
        ArgumentCaptor<Order> saved = ArgumentCaptor.forClass(Order.class);
        verify(orders).save(saved.capture());
        assertAll(
                () -> assertEquals(OrderStatus.FAILED, saved.getValue().status()),
                () -> assertEquals("C-1", saved.getValue().id()));
        verify(mailer).send("ana@mail.test", "Paiement refuse", "plafond atteint");
    }

    @Test
    void oneTimeoutIsRetriedOnce() {
        when(orders.find("C-1")).thenReturn(Optional.of(NEW_ORDER));
        when(gateway.charge("ana", 4250))
                .thenThrow(new GatewayTimeoutException("30 s"))
                .thenReturn(new ChargeResult(true, "T-78", null));

        assertEquals("T-78", service.pay("C-1"));
        verify(gateway, times(2)).charge("ana", 4250);
    }

    @Test
    void twoTimeoutsGiveUpWithoutAThirdTry() {
        when(orders.find("C-1")).thenReturn(Optional.of(NEW_ORDER));
        when(gateway.charge(anyString(), anyLong())).thenThrow(new GatewayTimeoutException("30 s"));

        assertEquals("INDISPONIBLE", service.pay("C-1"));
        verify(gateway, times(2)).charge("ana", 4250);
        verify(orders).save(NEW_ORDER.withStatus(OrderStatus.FAILED));
        verify(mailer).send("ana@mail.test", "Paiement impossible", "Reessayez plus tard");
    }

    @Test
    void alreadyPaidOrderNeverReachesTheBank() {
        when(orders.find("C-1")).thenReturn(Optional.of(NEW_ORDER.withStatus(OrderStatus.PAID)));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> service.pay("C-1"));
        assertEquals("deja payee : C-1", e.getMessage());
        verifyNoInteractions(gateway, mailer);
        verify(orders, never()).save(any());
    }

    @Test
    void zeroAmountIsRefusedBeforeTheBank() {
        when(orders.find("C-0")).thenReturn(Optional.of(new Order("C-0", "bob", "bob@mail.test", 0, OrderStatus.NEW)));

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> service.pay("C-0"));
        assertEquals("montant invalide : C-0", e.getMessage());
        verifyNoInteractions(gateway);
    }

    @Test
    void oneCentIsEnough() {
        when(orders.find("C-2")).thenReturn(Optional.of(new Order("C-2", "bob", "bob@mail.test", 1, OrderStatus.NEW)));
        when(gateway.charge("bob", 1)).thenReturn(new ChargeResult(true, "T-1", null));

        assertEquals("T-1", service.pay("C-2"));
    }

    @Test
    void unknownOrder() {
        when(orders.find("X")).thenReturn(Optional.empty());

        NoSuchElementException e = assertThrows(NoSuchElementException.class, () -> service.pay("X"));
        assertEquals("commande inconnue : X", e.getMessage());
        verifyNoInteractions(gateway, mailer);
    }

    @Test
    void retryFailedCountsOnlyTheOrdersFinallyPaid() {
        Order a = new Order("A", "ana", "ana@mail.test", 100, OrderStatus.FAILED);
        Order b = new Order("B", "bob", "bob@mail.test", 200, OrderStatus.FAILED);
        Order c = new Order("C", "cid", "cid@mail.test", 300, OrderStatus.FAILED);
        when(orders.findByStatus(OrderStatus.FAILED)).thenReturn(List.of(a, b, c));
        when(orders.find("A")).thenReturn(Optional.of(a));
        when(orders.find("B")).thenReturn(Optional.of(b));
        when(orders.find("C")).thenReturn(Optional.of(c));
        when(gateway.charge(eq("ana"), anyLong())).thenReturn(new ChargeResult(true, "T-A", null));
        when(gateway.charge(eq("bob"), anyLong())).thenReturn(new ChargeResult(false, null, "carte expiree"));
        when(gateway.charge(eq("cid"), anyLong())).thenThrow(new GatewayTimeoutException("30 s"));

        assertEquals(1, service.retryFailed());
        verify(mailer).send("ana@mail.test", "Recu A", "Montant : 100 ; transaction T-A");
        verify(mailer).send("bob@mail.test", "Paiement refuse", "carte expiree");
        verify(mailer).send("cid@mail.test", "Paiement impossible", "Reessayez plus tard");
        verifyNoMoreInteractions(mailer);
    }
}
