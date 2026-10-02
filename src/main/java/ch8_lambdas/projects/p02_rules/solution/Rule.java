package ch8_lambdas.projects.p02_rules.solution;

import java.util.function.Predicate;

/**
 * SOLUTION - une regle compilee : un Predicate&lt;String&gt; sous un nom NON generique,
 * pour pouvoir creer un tableau Rule[] (new Predicate&lt;String&gt;[n] est interdit : tableau generique).
 */
@FunctionalInterface
public interface Rule extends Predicate<String> {
}
