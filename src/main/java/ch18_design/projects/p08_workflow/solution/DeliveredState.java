package ch18_design.projects.p08_workflow.solution;

import java.time.LocalDate;

/** Livree : remboursable pendant 14 jours apres la livraison, le 14e jour compris. */
public final class DeliveredState implements OrderState {

    static final int REFUND_DAYS = 14;

    @Override
    public String label() {
        return "livree";
    }

    @Override
    public OrderState refund(Order order) {
        LocalDate limit = order.deliveredOn().plusDays(REFUND_DAYS);
        if (order.today().isAfter(limit)) {
            throw new IllegalStateException("delai de remboursement depasse");
        }
        order.recordRefund(order.amountCents());
        return ClosedState.refunded();
    }
}
