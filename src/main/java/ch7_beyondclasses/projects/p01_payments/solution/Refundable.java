package ch7_beyondclasses.projects.p01_payments.solution;

/**
 * SOLUTION - un paiement remboursable.
 */
public interface Refundable {

    long refund(long amount);

    default String policy() {
        return "remboursable 30 jours";
    }
}
