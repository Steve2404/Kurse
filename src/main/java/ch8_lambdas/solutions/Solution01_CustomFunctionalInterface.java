package ch8_lambdas.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise01_CustomFunctionalInterface.
 */
public class Solution01_CustomFunctionalInterface {

    @FunctionalInterface
    interface Validator<T> {
        boolean test(T value);

        default Validator<T> and(Validator<? super T> other) {
            // Un default rend un NOUVEAU validateur ; this est l'objet qui implemente l'interface.
            return value -> this.test(value) && other.test(value);
        }

        default Validator<T> or(Validator<? super T> other) {
            // Meme principe : on combine this et other dans une lambda.
            return value -> this.test(value) || other.test(value);
        }

        default Validator<T> negate() {
            // Inverser le resultat de this, sans modifier this.
            return value -> !this.test(value);
        }
    }
}
