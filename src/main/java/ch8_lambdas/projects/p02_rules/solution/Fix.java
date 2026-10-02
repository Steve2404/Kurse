package ch8_lambdas.projects.p02_rules.solution;

import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * SOLUTION - un correctif : quand problem est vrai, on applique repair. Un record peut contenir des lambdas.
 */
public record Fix(String name, Predicate<String> problem, UnaryOperator<String> repair) {
}
