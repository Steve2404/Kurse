package ch18_design.projects.p08_workflow.solution;

/** Nouvelle : on peut la payer, ou l'annuler (rien a rembourser). */
public final class NewState implements OrderState {

    @Override
    public String label() {
        return "nouvelle";
    }

    @Override
    public OrderState pay(Order order) {
        return new PaidState();
    }

    @Override
    public OrderState cancel(Order order) {
        return ClosedState.cancelled();
    }
}
