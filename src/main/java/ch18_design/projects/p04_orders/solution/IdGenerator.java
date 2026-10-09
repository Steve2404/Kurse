package ch18_design.projects.p04_orders.solution;

/** Un port : le prochain numero de commande. Le hasard, comme l'heure, se REMPLACE dans les tests. */
@FunctionalInterface
public interface IdGenerator {

    String next();
}
