package ch18_design.projects.p08_workflow.solution;

/** Payee : on peut l'expedier, ou l'annuler, et alors on rembourse tout. */
public final class PaidState implements OrderState {

    @Override
    public String label() {
        return "payee";
    }

    @Override
    public OrderState ship(Order order) {
        return new ShippedState();
    }

    @Override
    public OrderState cancel(Order order) {
        order.recordRefund(order.amountCents());
        return ClosedState.cancelled();
    }
}
