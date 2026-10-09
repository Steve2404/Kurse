package ch18_design.projects.p08_workflow.solution;

/** Expediee : le colis roule ; on ne peut plus annuler, seulement constater la livraison. */
public final class ShippedState implements OrderState {

    @Override
    public String label() {
        return "expediee";
    }

    @Override
    public OrderState deliver(Order order) {
        order.markDelivered();
        return new DeliveredState();
    }
}
