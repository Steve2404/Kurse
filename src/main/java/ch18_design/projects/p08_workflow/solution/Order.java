package ch18_design.projects.p08_workflow.solution;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Le CONTEXTE : il garde l'etat courant et lui DELEGUE chaque action. Il ne contient aucun "if" sur
 * l'etat : ajouter un etat (par exemple "en preparation") ne demande pas de le modifier.
 */
public final class Order {

    private final String id;
    private final long amountCents;
    private final Clock clock;
    private OrderState state = new NewState();
    private final List<Transition> transitions = new ArrayList<>();
    private long refundedCents;
    private LocalDate deliveredOn;

    public Order(String id, long amountCents, Clock clock) {
        this.id = id;
        this.amountCents = amountCents;
        this.clock = clock;
    }

    public void pay() {
        change(state.pay(this));
    }

    public void ship() {
        change(state.ship(this));
    }

    public void deliver() {
        change(state.deliver(this));
    }

    public void cancel() {
        change(state.cancel(this));
    }

    public void refund() {
        change(state.refund(this));
    }

    // Appele seulement si l'etat a accepte : un refus lance une exception AVANT, et rien ne change.
    private void change(OrderState next) {
        transitions.add(new Transition(state.label(), next.label()));
        state = next;
    }

    IllegalStateException refused(String action) {
        return new IllegalStateException("action refusee : " + action + " (commande " + state.label() + ")");
    }

    void recordRefund(long cents) {
        refundedCents += cents;
    }

    void markDelivered() {
        deliveredOn = today();
    }

    LocalDate today() {
        return LocalDate.now(clock);
    }

    LocalDate deliveredOn() {
        return deliveredOn;
    }

    public String id() {
        return id;
    }

    public long amountCents() {
        return amountCents;
    }

    public String status() {
        return state.label();
    }

    public boolean isClosed() {
        return state.isClosed();
    }

    public long refundedCents() {
        return refundedCents;
    }

    public List<Transition> transitions() {
        return List.copyOf(transitions);
    }
}
