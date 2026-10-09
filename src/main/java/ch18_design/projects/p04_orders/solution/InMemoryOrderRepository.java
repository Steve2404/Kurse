package ch18_design.projects.p04_orders.solution;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Un ADAPTATEUR : la base en memoire. Demain, une base H2 (chapitre 15) implementera le meme port. */
public final class InMemoryOrderRepository implements OrderRepository {

    private final Map<String, Order> orders = new LinkedHashMap<>();

    @Override
    public void save(Order order) {
        orders.put(order.id(), order);
    }

    @Override
    public Optional<Order> find(String id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public boolean delete(String id) {
        return orders.remove(id) != null;
    }

    // Dans l'ordre d'enregistrement.
    @Override
    public List<Order> byCustomer(String customer) {
        return orders.values().stream().filter(o -> o.customer().equals(customer)).toList();
    }
}
