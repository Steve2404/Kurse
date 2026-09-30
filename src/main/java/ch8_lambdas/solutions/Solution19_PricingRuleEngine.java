package ch8_lambdas.solutions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * Corrige de l'exercice 19. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch8_lambdas.exercises.Exercise19_PricingRuleEngine.
 */
public class Solution19_PricingRuleEngine {

    public record Order(String customer, long cents, int items, boolean vip) {
    }

    public record Rule(String name, Predicate<Order> when, UnaryOperator<Long> effect) {
        public boolean appliesTo(Order order) {
            // La condition est une donnee (un Predicate) : on l'interroge.
            return when.test(order);
        }

        public long applyTo(long cents) {
            // L'effet est une fonction : on l'applique (boxing long <-> Long automatique).
            return effect.apply(cents);
        }
    }

    public static UnaryOperator<Long> percentOff(int percent) {
        // Fabrique de lambda : percent est capture.
        return cents -> cents - cents * percent / 100;
    }

    public static UnaryOperator<Long> minus(long cents) {
        // Math.max empeche un prix negatif.
        return price -> Math.max(0, price - cents);
    }

    public static UnaryOperator<Long> plus(long cents) {
        // Le montant est capture : il est effectivement final, donc la lambda peut le lire.
        return price -> price + cents;
    }

    public static class Engine {
        private final List<Rule> rules = new ArrayList<>();

        public Engine add(Rule rule) {
            // Rendre this permet d'enchainer les add (interface fluide).
            rules.add(rule);
            return this;
        }

        public long price(Order order) {
            // Chaque regle qui s'applique transforme le prix courant, dans l'ordre.
            long price = order.cents();
            for (Rule rule : rules) {
                if (rule.appliesTo(order)) {
                    price = rule.applyTo(price);
                }
            }
            return price;
        }

        public List<String> explain(Order order) {
            // Meme parcours que price, mais on garde les noms des regles qui s'appliquent au lieu de modifier le prix.
            List<String> names = new ArrayList<>();
            for (Rule rule : rules) {
                if (rule.appliesTo(order)) {
                    names.add(rule.name());
                }
            }
            return names;
        }
    }

    public static Function<Long, Long> pipeline(List<UnaryOperator<Long>> effects) {
        // identity() est l'element neutre ; andThen colle les effets dans l'ordre.
        Function<Long, Long> result = Function.identity();
        for (UnaryOperator<Long> effect : effects) {
            result = result.andThen(effect);
        }
        return result;
    }

    public static Predicate<Order> vipOnly() {
        // Deux predicats composes avec and().
        Predicate<Order> isVip = Order::vip;
        Predicate<Order> bigBasket = o -> o.items() > 3;
        return isVip.and(bigBasket);
    }
}
