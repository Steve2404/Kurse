package ch19_final.projects.p05_resilience.solution;

/** Le port vers le fournisseur : le stock d'une reference. */
@FunctionalInterface
public interface StockClient {

    int stock(String ref);
}
