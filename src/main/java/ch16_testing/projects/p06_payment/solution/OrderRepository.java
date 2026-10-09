package ch16_testing.projects.p06_payment.solution;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Optional<Order> find(String id);

    List<Order> findByStatus(OrderStatus status);

    void save(Order order);
}
