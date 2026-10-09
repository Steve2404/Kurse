package ch18_design.projects.p04_orders.solution;

import java.util.Optional;

/** Un port : le prix d'un article, s'il existe. */
@FunctionalInterface
public interface Catalog {

    Optional<Long> price(String item);
}
