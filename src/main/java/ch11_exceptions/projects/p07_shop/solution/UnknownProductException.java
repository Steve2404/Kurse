package ch11_exceptions.projects.p07_shop.solution;

/**
 * SOLUTION - un produit absent du catalogue (verifiee).
 */
public class UnknownProductException extends Exception {

    public UnknownProductException(String product) {
        super("produit inconnu : " + product);
    }
}
