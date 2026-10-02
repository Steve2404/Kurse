package ch9_collections.projects.p04_generics.solution;

/**
 * SOLUTION - un record GENERIQUE a deux parametres de type.
 */
public record Pair<A, B>(A first, B second) {

    // Une methode d'instance qui rend un type generique "inverse".
    public Pair<B, A> swap() {
        return new Pair<>(second, first);
    }

    // Une methode static generique doit DECLARER ses propres parametres de type (les A et B du record ne s'appliquent pas).
    public static <X> Pair<X, X> twin(X value) {
        return new Pair<>(value, value);
    }
}
