package ch8_lambdas.projects.p07_orders.solution;

import java.util.function.Predicate;

/**
 * SOLUTION - une regle de validation : un Predicate&lt;Purchase&gt; sous un nom non generique,
 * ce qui permet un tableau Validation[] (un tableau de Predicate&lt;Purchase&gt; ne se cree pas).
 */
@FunctionalInterface
public interface Validation extends Predicate<Purchase> {
}
