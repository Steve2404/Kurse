package ch7_beyondclasses.projects.p01_payments.solution;

/**
 * SOLUTION - une interface peut en etendre PLUSIEURS (extends, pas implements).
 */
public interface SecurePayment extends PaymentMethod, Refundable {

    long THRESHOLD_3DS = 30_000;

    default boolean needs3ds(long amount) {
        return amount > THRESHOLD_3DS;
    }

    // Une sous-interface peut redefinir une methode default de son parent.
    @Override
    default String policy() {
        return "3-D Secure, " + Refundable.super.policy();   // X.super.m() : la version d'une super-interface DIRECTE
    }
}
