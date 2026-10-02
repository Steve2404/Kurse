package ch7_beyondclasses.projects.p01_payments.solution;

/**
 * SOLUTION - un bon d'achat : il redefinit la methode default pay() pour gerer son solde.
 */
public class Voucher implements PaymentMethod {

    private final String code;
    private long balance;

    public Voucher(String code, long balance) {
        this.code = code;
        this.balance = balance;
    }

    // Cle : la derniere lettre vaut 'A' + (somme des codes des autres caracteres, tirets exclus) % 26.
    static char checkLetter(String body) {
        int sum = 0;
        for (int i = 0; i < body.length(); i++) {
            if (body.charAt(i) != '-') {
                sum += body.charAt(i);
            }
        }
        return (char) ('A' + sum % 26);
    }

    @Override
    public String label() {
        return "bon " + code;
    }

    @Override
    public boolean isValid() {
        return checkLetter(code.substring(0, code.length() - 1)) == code.charAt(code.length() - 1);
    }

    @Override
    public long fee(long amount) {
        return 0;
    }

    // Redefinir une methode default : on peut aussi reutiliser l'originale avec PaymentMethod.super.pay(...).
    @Override
    public String pay(long amount) {
        if (isValid() && amount > balance) {
            return label() + " : REFUSE (solde " + PaymentMethod.money(balance) + ")";
        }
        String result = PaymentMethod.super.pay(amount);
        if (isValid()) {
            balance -= amount;
        }
        return result + ", reste " + PaymentMethod.money(balance);
    }
}
