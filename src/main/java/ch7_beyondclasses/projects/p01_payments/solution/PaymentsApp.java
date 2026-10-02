package ch7_beyondclasses.projects.p01_payments.solution;

import ch7_beyondclasses.projects.p01_payments.Data;

/**
 * SOLUTION du projet 1 - les moyens de paiement.
 */
public class PaymentsApp {

    static PaymentMethod create(String line) {
        String[] p = line.split(" ");
        return switch (p[0]) {
            case "CARD" -> new CreditCard(p[1], p[2]);
            case "IBAN" -> new BankTransfer(p[1], p[2]);
            default -> new Voucher(p[1], Long.parseLong(p[2]));
        };
    }

    public static void main(String[] args) {
        System.out.println("plafond " + PaymentMethod.money(PaymentMethod.MAX_CENTS) + ", seuil 3-D Secure " + PaymentMethod.money(SecurePayment.THRESHOLD_3DS));
        PaymentMethod[] methods = new PaymentMethod[Data.METHODS.length];
        for (int i = 0; i < methods.length; i++) {
            methods[i] = create(Data.METHODS[i]);
        }
        long fees = 0;
        int valid = 0;
        for (int i = 0; i < methods.length; i++) {
            PaymentMethod m = methods[i];
            long amount = Data.AMOUNTS[i];
            System.out.println(m.pay(amount));
            if (m.isValid() && amount <= PaymentMethod.MAX_CENTS) {
                fees += m.fee(amount);
                valid++;
            }
            // Le type de la reference (PaymentMethod) ne connait pas policy() : on teste puis on utilise la variable de pattern.
            if (m.isValid() && m instanceof Refundable r) {
                System.out.println("  " + r.policy() + ", rembourse " + PaymentMethod.money(r.refund(amount)));
            }
            if (m instanceof SecurePayment s) {
                System.out.println("  3-D Secure exige : " + s.needs3ds(amount));
            }
        }
        System.out.println("paiements acceptes " + valid + "/" + methods.length + ", frais totaux " + PaymentMethod.money(fees));
        Voucher v = new Voucher("GIFT-2026-" + Voucher.checkLetter("GIFT-2026-"), 3000);
        System.out.println(v.pay(1200) + " | " + v.pay(2000));
        System.out.println("cle de Luhn de 453957876362148 : " + PaymentMethod.luhnCheckDigit("453957876362148") + " ; cle de bon GIFT-2026- : "
                + Voucher.checkLetter("GIFT-2026-"));
    }
}
