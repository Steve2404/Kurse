package ch18_design.projects.p04_orders.solution;

import java.io.PrintStream;
import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * La RACINE DE COMPOSITION : le seul endroit qui choisit les vrais adaptateurs et les branche.
 * Les "new" des adaptateurs sont ICI, et nulle part ailleurs.
 */
public final class BakeryApp {

    static final Map<String, Long> PRICES = Map.of("baguette", 120L, "croissant", 110L, "tarte", 1850L);

    private BakeryApp() {
    }

    public static OrderService create(Clock clock, PrintStream out) {
        Catalog catalog = item -> Optional.ofNullable(PRICES.get(item));
        return new OrderService(new InMemoryOrderRepository(), new ConsoleNotifier(out), catalog,
                new SequentialIds("CMD-"), clock);
    }

    public static void main(String[] args) {
        OrderService service = create(Clock.systemDefaultZone(), System.out);
        try {
            System.out.println(service.place("ada@example.org", List.of("baguette", "croissant", "croissant")));
        } catch (IllegalStateException e) {
            System.out.println("refus : " + e.getMessage());
        }
    }
}
