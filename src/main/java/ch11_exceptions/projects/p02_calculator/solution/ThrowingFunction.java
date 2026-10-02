package ch11_exceptions.projects.p02_calculator.solution;

/**
 * SOLUTION - une interface fonctionnelle dont la methode DECLARE une exception verifiee :
 * Function.apply ne le permet pas, d'ou cette interface.
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> {

    R apply(T value) throws SyntaxException;
}
