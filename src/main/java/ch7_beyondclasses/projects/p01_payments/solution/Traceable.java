package ch7_beyondclasses.projects.p01_payments.solution;

/**
 * SOLUTION - un paiement trace pour la lutte anti-fraude.
 */
public interface Traceable {

    default String policy() {
        return "trace anti-fraude";
    }
}
