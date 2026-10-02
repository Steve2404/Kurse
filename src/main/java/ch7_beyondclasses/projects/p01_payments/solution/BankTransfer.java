package ch7_beyondclasses.projects.p01_payments.solution;

/**
 * SOLUTION - un virement : gratuit dans la zone SEPA de l'exemple, 5 EUR ailleurs.
 */
public class BankTransfer implements PaymentMethod, Refundable {

    private static final String SEPA = "FR DE ES IT BE NL";
    private final String iban;
    private final String holder;

    public BankTransfer(String iban, String holder) {
        this.iban = iban;
        this.holder = holder;
    }

    @Override
    public String label() {
        return "virement " + holder + " " + iban.substring(0, 2);
    }

    @Override
    public boolean isValid() {
        return PaymentMethod.ibanValid(iban);
    }

    @Override
    public long fee(long amount) {
        return SEPA.contains(iban.substring(0, 2)) ? 0 : 500;
    }

    // Un virement n'est rembourse qu'a 90 %.
    @Override
    public long refund(long amount) {
        return amount * 9 / 10;
    }

    // Pas de redefinition de policy() : une seule interface la fournit, elle est heritee telle quelle.
}
