package ch19_final.projects.p05_resilience.solution;

/** Ni le fournisseur ni le cache ne savent : on le dit clairement, avec la cause. */
public class StockUnavailableException extends RuntimeException {

    public StockUnavailableException(String ref, RuntimeException cause) {
        super("stock inconnu pour " + ref + " : " + cause.getMessage(), cause);
    }
}
