package ch7_beyondclasses.projects.p01_payments;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 1 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON PaymentsApp, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "plafond 5000.00, seuil 3-D Secure 300.00",
            "carte Alice ****1486 : OK 450.00 + frais 7.00",
            "  3-D Secure, remboursable 30 jours + trace anti-fraude, rembourse 450.00",
            "  3-D Secure exige : true",
            "carte Bob ****1487 : REFUSE (invalide)",
            "  3-D Secure exige : false",
            "virement Chloe FR : OK 1200.00 + frais 0.00",
            "  remboursable 30 jours, rembourse 1080.00",
            "virement Dan GB : OK 800.00 + frais 5.00",
            "  remboursable 30 jours, rembourse 720.00",
            "virement Eve DE : REFUSE (invalide)",
            "bon ABCD-1234-X : REFUSE (invalide), reste 50.00",
            "carte Fanny ****1486 : REFUSE (plafond 5000.00)",
            "  3-D Secure, remboursable 30 jours + trace anti-fraude, rembourse 6000.00",
            "  3-D Secure exige : true",
            "paiements acceptes 3/7, frais totaux 12.00",
            "bon GIFT-2026-G : OK 12.00 + frais 0.00, reste 18.00 | bon GIFT-2026-G : REFUSE (solde 18.00)",
            "cle de Luhn de 453957876362148 : 6 ; cle de bon GIFT-2026- : G");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.METHODS", "Data.AMOUNTS", "interface PaymentMethod", "interface SecurePayment extends PaymentMethod, Refundable",
            "implements SecurePayment, Traceable", "default String pay(", "private String line(", "static String money(",
            "private static int value(", "SecurePayment.super.policy()", "Traceable.super.policy()", "Refundable.super.policy()",
            "PaymentMethod.super.pay(", "long MAX_CENTS", "instanceof Refundable r",
            // Crescendo : notions des chapitres 8 a 15, interdites au chapitre 7.
            "!List", "!Map", "!Set<", "!ArrayList", "!::", "!Comparable", "!Comparator", "!.stream(",
            "!.lines()", "!Optional", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.chars()",
            "!.now()", "!re:\\(\\s*\\)\\s*->|\\(\\w+(?:\\s*,\\s*\\w+)*\\)\\s*->|[=(]\\s*\\w+\\s*->##lambda (chapitre 8)", "!re:(?:class|interface|record)\\s+\\w+\\s*<##type generique (chapitre 9)");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "PaymentsApp", args, EXPECTED, API);
    }
}
