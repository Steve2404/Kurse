package ch18_design.projects.p04_orders;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("OrderService.java", "now.getHour() < 7", "now.getHour() < 6"),
            new Mutant("OrderService.java", "now.getHour() >= 19", "now.getHour() > 19"),
            new Mutant("OrderService.java", "now.getDayOfWeek() == DayOfWeek.SUNDAY || ", ""),
            new Mutant("OrderService.java", "if (items.isEmpty()) {", "if (items == null) {"),
            new Mutant("OrderService.java", "items.stream().mapToLong(this::priceOf).sum()", "items.stream().distinct().mapToLong(this::priceOf).sum()"),
            new Mutant("OrderService.java", "catalog.price(item).orElseThrow(() -> new IllegalArgumentException(\"article inconnu : \" + item))", "catalog.price(item).orElse(0L)"),
            new Mutant("OrderService.java", "        repository.save(order);\n", ""),
            new Mutant("OrderService.java", "customer, items, total, now);", "customer, items, total, now.withHour(12));"),
            new Mutant("OrderService.java", "        try {\n            notifier.orderConfirmed(order);\n        } catch (RuntimeException e) {", "        notifier.orderConfirmed(order);\n        if (order == null) {"),
            new Mutant("OrderService.java", "now.isAfter(order.get().placedAt().plus(CANCEL_WINDOW))", "!now.isBefore(order.get().placedAt().plus(CANCEL_WINDOW))"),
            new Mutant("OrderService.java", "        repository.delete(id);\n", ""),
            new Mutant("OrderService.java", "        notifier.orderCancelled(order.get());\n", ""),
            new Mutant("SequentialIds.java", "counter++;\n        return prefix + counter;", "return prefix + counter++;"),
            new Mutant("InMemoryOrderRepository.java", ".filter(o -> o.customer().equals(customer))", ".filter(o -> !o.customer().isEmpty())"),
            new Mutant("InMemoryOrderRepository.java", "new LinkedHashMap<>()", "new java.util.TreeMap<>(java.util.Comparator.reverseOrder())"),
            new Mutant("Order.java", "items = List.copyOf(items);", "items = java.util.Collections.unmodifiableList(items);"),
            new Mutant("ConsoleNotifier.java", "\" annulee\"", "\" annulee !\""));

    static final List<String> API_CODE = List.of(
            "record Order(", "interface OrderRepository", "interface Notifier", "interface Catalog", "interface IdGenerator",
            "final class OrderService", "final class InMemoryOrderRepository implements OrderRepository",
            "final class ConsoleNotifier implements Notifier", "final class SequentialIds implements IdGenerator",
            "final class BakeryApp", "LocalDateTime.now(clock)", "Clock.systemDefaultZone()",
            "!Legacy", "!Random", "!static OrderService instance",
            "max:method=10",
            "in:OrderService.java!System.##le service n'ecrit pas sur la console (System.)",
            "in:OrderService.java!now()##le service ne lit pas l'heure systeme (now())",
            "in:OrderService.java!new InMemoryOrderRepository##le service ne fabrique pas ses adaptateurs",
            "in:OrderService.java!new ConsoleNotifier##le service ne fabrique pas ses adaptateurs",
            "in:OrderService.java!new SequentialIds##le service ne fabrique pas ses adaptateurs",
            "in:OrderService.java!Clock.system##le service ne choisit pas son horloge",
            "in:ConsoleNotifier.java!System.out##l'adaptateur recoit son flux, il ne prend pas System.out",
            "in:BakeryApp.java=new OrderService(");

    static final List<String> API_TESTS = List.of(
            "implements Notifier", "Clock.fixed(", "mock(", "doThrow(", "verify(", "verifyNoInteractions(",
            "ByteArrayOutputStream", "BakeryApp.create(", "@ParameterizedTest", "assertThrows(",
            "!System.out", "!Thread.sleep", "!systemDefaultZone");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 15, MUTANTS, API_CODE, API_TESTS);
    }
}
