package ch18_design.projects.p04_orders.solution;

import java.util.List;
import java.util.Optional;

/**
 * Un PORT : l'interface est ecrite du point de vue du METIER (ce dont le service a besoin),
 * pas de la base de donnees. C'est le service qui la possede ; la base s'y adapte (le D de SOLID).
 */
public interface OrderRepository {

    void save(Order order);

    Optional<Order> find(String id);

    boolean delete(String id);

    List<Order> byCustomer(String customer);
}
