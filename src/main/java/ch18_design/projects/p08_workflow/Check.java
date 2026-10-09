package ch18_design.projects.p08_workflow;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 8 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("NewState.java", "public OrderState pay(Order order) {\n        return new PaidState();", "public OrderState pay(Order order) {\n        return new ShippedState();"),
            new Mutant("NewState.java", "public OrderState cancel(Order order) {\n        return ClosedState.cancelled();", "public OrderState cancel(Order order) {\n        return ClosedState.refunded();"),
            new Mutant("PaidState.java", "        order.recordRefund(order.amountCents());\n", ""),
            new Mutant("PaidState.java", "public OrderState ship(Order order) {\n        return new ShippedState();", "public OrderState ship(Order order) {\n        return new DeliveredState();"),
            new Mutant("ShippedState.java", "public OrderState deliver(Order order) {", "public OrderState cancel(Order order) {\n        return ClosedState.cancelled();\n    }\n\n    @Override\n    public OrderState deliver(Order order) {"),
            new Mutant("ShippedState.java", "        order.markDelivered();\n", ""),
            new Mutant("DeliveredState.java", "order.today().isAfter(limit)", "!order.today().isBefore(limit)"),
            new Mutant("DeliveredState.java", "order.recordRefund(order.amountCents());", "order.recordRefund(order.amountCents() / 2);"),
            new Mutant("ClosedState.java", "public boolean isClosed() {\n        return true;", "public boolean isClosed() {\n        return label.equals(\"annulee\");"),
            new Mutant("OrderState.java", "throw order.refused(\"rembourser\");", "return this;"),
            new Mutant("Order.java", "transitions.add(new Transition(state.label(), next.label()));", "transitions.add(new Transition(next.label(), next.label()));"),
            new Mutant("Order.java", "return List.copyOf(transitions);", "return transitions;"),
            new Mutant("Order.java", "\"action refusee : \" + action + \" (commande \" + state.label() + \")\"", "\"action refusee : \" + action"),
            new Mutant("Order.java", "deliveredOn = today();", "deliveredOn = today().minusDays(1);"),
            new Mutant("ReceiptExporter.java", "return out.append(footer(order)).toString();", "return out.toString();"),
            new Mutant("TextReceipt.java", "return \"  \" + transition + \"\\n\";", "return \"  \" + transition.to() + \"\\n\";"),
            new Mutant("CsvReceipt.java", "return \"commande;de;vers\\n\";", "return \"commande;vers;de\\n\";"));

    static final List<String> API_CODE = List.of(
            "record Transition(", "interface OrderState", "default OrderState pay(Order", "final class NewState implements OrderState",
            "final class PaidState implements OrderState", "final class ShippedState implements OrderState",
            "final class DeliveredState implements OrderState", "final class ClosedState implements OrderState", "final class Order",
            "abstract class ReceiptExporter", "public final String export(Order", "protected abstract String",
            "final class TextReceipt extends ReceiptExporter", "final class CsvReceipt extends ReceiptExporter",
            "LocalDate.now(clock)", "!switch", "!instanceof", "!Legacy",
            "max:method=10",
            "in:Order.java!if (##le contexte ne teste pas son etat : il delegue",
            "in:Order.java!equals(##le contexte ne compare pas son etat : il delegue",
            "in:CsvReceipt.java!String export(##le squelette n'est ecrit qu'une fois, dans ReceiptExporter",
            "in:TextReceipt.java!String export(##le squelette n'est ecrit qu'une fois, dans ReceiptExporter");

    static final List<String> API_TESTS = List.of(
            "@ParameterizedTest", "@CsvSource", "extends Clock", "new TextReceipt()", "new CsvReceipt()", "assertThrows(",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 30, MUTANTS, API_CODE, API_TESTS);
    }
}
