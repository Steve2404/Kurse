package ch18_design.projects.p04_orders.solution;

import java.time.LocalDateTime;
import java.util.List;

/** Une commande enregistree. La liste est copiee : personne ne la modifie apres coup. */
public record Order(String id, String customer, List<String> items, long totalCents, LocalDateTime placedAt) {

    public Order {
        items = List.copyOf(items);
    }
}
