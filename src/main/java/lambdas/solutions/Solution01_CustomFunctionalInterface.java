package lambdas.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans lambdas.exercises.Exercise01_CustomFunctionalInterface.
 */
public class Solution01_CustomFunctionalInterface {

    @FunctionalInterface
    interface Validator<T> {
        boolean test(T value);

        default Validator<T> and(Validator<? super T> other) {
            return value -> this.test(value) && other.test(value);
        }

        default Validator<T> or(Validator<? super T> other) {
            return value -> this.test(value) || other.test(value);
        }

        default Validator<T> negate() {
            return value -> !this.test(value);
        }
    }
}
