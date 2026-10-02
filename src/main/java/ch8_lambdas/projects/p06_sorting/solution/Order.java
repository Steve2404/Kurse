package ch8_lambdas.projects.p06_sorting.solution;

import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 * SOLUTION - un ordre sur les personnes : UNE methode abstraite, donc une interface fonctionnelle.
 * Les combinateurs (default et static) rendent de NOUVELLES lambdas : c'est ce que fera Comparator (chapitre 9).
 */
@FunctionalInterface
public interface Order {

    int compare(Person a, Person b);

    // Une methode publique d'Object redeclaree ne compte pas comme methode abstraite.
    @Override
    String toString();

    default Order reversed() {
        return (a, b) -> compare(b, a);
    }

    // A egalite selon this, on departage avec next.
    default Order then(Order next) {
        return (a, b) -> {
            int c = compare(a, b);
            return c != 0 ? c : next.compare(a, b);
        };
    }

    // Fabrique : un ordre a partir d'une cle entiere (ToIntFunction evite le boxing).
    static Order by(ToIntFunction<Person> key) {
        return (a, b) -> Integer.compare(key.applyAsInt(a), key.applyAsInt(b));
    }

    static Order byText(Function<Person, String> key) {
        return (a, b) -> key.apply(a).compareTo(key.apply(b));
    }
}
