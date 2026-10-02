package ch9_collections.projects.p04_generics.solution;

/**
 * SOLUTION - une interface fonctionnelle GENERIQUE, avec une methode default elle-meme generique (son propre C).
 */
@FunctionalInterface
public interface Transformer<A, B> {

    B transform(A input);

    default <C> Transformer<A, C> then(Transformer<? super B, ? extends C> next) {
        return a -> next.transform(transform(a));
    }
}
