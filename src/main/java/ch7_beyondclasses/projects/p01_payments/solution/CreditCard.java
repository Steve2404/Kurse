package ch7_beyondclasses.projects.p01_payments.solution;

/**
 * SOLUTION - une carte bancaire : deux interfaces apportent policy(), il FAUT trancher.
 */
public class CreditCard implements SecurePayment, Traceable {

    private final String number;
    private final String holder;

    public CreditCard(String number, String holder) {
        this.number = number;
        this.holder = holder;
    }

    // Les methodes d'interface sont public : l'implementation doit l'etre aussi.
    @Override
    public String label() {
        return "carte " + holder + " ****" + number.substring(number.length() - 4);
    }

    @Override
    public boolean isValid() {
        return PaymentMethod.luhnValid(number);
    }

    // 1,5 % + 0,25 EUR, arrondi au centime.
    @Override
    public long fee(long amount) {
        return Math.round(amount * 0.015) + 25;
    }

    @Override
    public long refund(long amount) {
        return amount;
    }

    // Conflit "en losange" : SecurePayment.policy() et Traceable.policy() -> redefinition obligatoire.
    @Override
    public String policy() {
        return SecurePayment.super.policy() + " + " + Traceable.super.policy();
    }
}
