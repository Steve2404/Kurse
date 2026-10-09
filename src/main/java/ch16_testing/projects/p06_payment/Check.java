package ch16_testing.projects.p06_payment;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON PaymentService et TES tests, ou avec l'argument "solution".
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("PaymentService.java", "if (order.status() == OrderStatus.PAID) {", "if (false) {"),
            new Mutant("PaymentService.java", "order.totalCents() <= 0", "order.totalCents() < 0"),
            new Mutant("PaymentService.java", "attempt <= 2", "attempt <= 1"),
            new Mutant("PaymentService.java", "attempt <= 2", "attempt <= 3"),
            new Mutant("PaymentService.java", "orders.save(order.withStatus(OrderStatus.PAID));", "orders.save(order);"),
            new Mutant("PaymentService.java",
                    "orders.save(order.withStatus(OrderStatus.PAID));\n        mailer.send(order.email(), \"Recu \" + orderId, \"Montant : \" + order.totalCents() + \" ; transaction \" + result.transactionId());",
                    "mailer.send(order.email(), \"Recu \" + orderId, \"Montant : \" + order.totalCents() + \" ; transaction \" + result.transactionId());\n        orders.save(order.withStatus(OrderStatus.PAID));"),
            new Mutant("PaymentService.java", "\"Montant : \" + order.totalCents() + \" ; transaction \"", "\"Montant : \" + order.totalCents() / 100 + \" ; transaction \""),
            new Mutant("PaymentService.java",
                    "orders.save(order.withStatus(OrderStatus.FAILED));\n            mailer.send(order.email(), \"Paiement refuse\"",
                    "mailer.send(order.email(), \"Paiement refuse\""),
            new Mutant("PaymentService.java", "\"Paiement refuse\", result.reason()", "\"Paiement refuse\", \"\""),
            new Mutant("PaymentService.java", "mailer.send(order.email(), \"Paiement impossible\", \"Reessayez plus tard\");", ""),
            new Mutant("PaymentService.java", "if (!outcome.equals(DECLINED) && !outcome.equals(UNAVAILABLE)) {", "if (!outcome.equals(UNAVAILABLE)) {"),
            new Mutant("PaymentService.java", "gateway.charge(order.customer(), order.totalCents())", "gateway.charge(order.email(), order.totalCents())"),
            new Mutant("PaymentService.java", "return result.transactionId();", "return \"OK\";"));

    static final List<String> API_CODE = List.of(
            "record Order(String id, String customer, String email, long totalCents, OrderStatus status)",
            "enum OrderStatus", "record ChargeResult(boolean approved, String transactionId, String reason)",
            "class GatewayTimeoutException extends RuntimeException", "interface PaymentGateway", "interface OrderRepository",
            "interface Mailer", "final class PaymentService",
            "PaymentService(OrderRepository orders, PaymentGateway gateway, Mailer mailer)",
            "String pay(String orderId)", "int retryFailed()", "catch (GatewayTimeoutException");

    static final List<String> API_TESTS = List.of(
            "@ExtendWith(MockitoExtension.class)", "@Mock", "@InjectMocks", "when(", ".thenReturn(", ".thenThrow(",
            "verify(", "times(2)", "never()", "verifyNoInteractions(", "verifyNoMoreInteractions(",
            "ArgumentCaptor", ".capture()", ".getValue()", "inOrder(", "anyString()", "anyLong()", "eq(",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 9, MUTANTS, API_CODE, API_TESTS);
    }
}
