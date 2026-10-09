package ch16_testing.projects.p06_payment.solution;

import java.util.NoSuchElementException;

/** Encaisse les commandes : la banque, puis le depot, puis le courriel, toujours dans cet ordre. */
public final class PaymentService {

    static final String DECLINED = "REFUSE";
    static final String UNAVAILABLE = "INDISPONIBLE";

    private final OrderRepository orders;
    private final PaymentGateway gateway;
    private final Mailer mailer;

    public PaymentService(OrderRepository orders, PaymentGateway gateway, Mailer mailer) {
        this.orders = orders;
        this.gateway = gateway;
        this.mailer = mailer;
    }

    // Piege : une commande deja payee ne doit JAMAIS repasser par la banque (double debit).
    // Pourquoi save avant send : le courriel promet ce que le depot contient deja.
    public String pay(String orderId) {
        Order order = orders.find(orderId).orElseThrow(() -> new NoSuchElementException("commande inconnue : " + orderId));
        if (order.status() == OrderStatus.PAID) {
            throw new IllegalStateException("deja payee : " + orderId);
        }
        if (order.totalCents() <= 0) {
            throw new IllegalStateException("montant invalide : " + orderId);
        }
        ChargeResult result = chargeWithOneRetry(order);
        if (result == null) {
            orders.save(order.withStatus(OrderStatus.FAILED));
            mailer.send(order.email(), "Paiement impossible", "Reessayez plus tard");
            return UNAVAILABLE;
        }
        if (!result.approved()) {
            orders.save(order.withStatus(OrderStatus.FAILED));
            mailer.send(order.email(), "Paiement refuse", result.reason());
            return DECLINED;
        }
        orders.save(order.withStatus(OrderStatus.PAID));
        mailer.send(order.email(), "Recu " + orderId, "Montant : " + order.totalCents() + " ; transaction " + result.transactionId());
        return result.transactionId();
    }

    // Pourquoi UNE seule nouvelle tentative : un 2e delai depasse signale une vraie panne ; insister surchargerait la banque.
    private ChargeResult chargeWithOneRetry(Order order) {
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                return gateway.charge(order.customer(), order.totalCents());
            } catch (GatewayTimeoutException e) {
                // on retente une fois
            }
        }
        return null;
    }

    // Retente toutes les commandes en echec ; rend le nombre de commandes enfin payees.
    public int retryFailed() {
        int paid = 0;
        for (Order order : orders.findByStatus(OrderStatus.FAILED)) {
            String outcome = pay(order.id());
            if (!outcome.equals(DECLINED) && !outcome.equals(UNAVAILABLE)) {
                paid++;
            }
        }
        return paid;
    }
}
