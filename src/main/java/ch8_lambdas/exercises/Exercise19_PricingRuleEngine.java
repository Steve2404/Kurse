package ch8_lambdas.exercises;

import ch8_lambdas.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * EXERCICE 19 - Un moteur de regles de prix fait de Predicate, UnaryOperator et Function (niveau : avance)
 * ======================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_CustomFunctionalInterface.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une boutique a des regles : "client VIP : -10 %", "plus de 5 articles :
 * -2 euros", "commande de moins de 20 euros : +3 euros de port". Chaque
 * regle = une CONDITION (Predicate<Order>) + un EFFET sur le prix
 * (UnaryOperator<Long>, en centimes). Le moteur applique, dans l'ordre,
 * toutes les regles dont la condition est vraie. Tout est fait de
 * lambdas : ajouter une regle ne demande AUCUNE nouvelle classe.
 *
 *
 * ==================================================================
 * TODO 1 : Rule.appliesTo(order)    et    TODO 2 : Rule.applyTo(cents)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. appliesTo : rendre when.test(order).
 *   2. applyTo : rendre effect.apply(cents).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 3 : percentOff(p)    TODO 4 : minus(cents)    TODO 5 : plus(cents)    [fabriques d'effets]
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   percentOff(10).apply(5000L) -> 4500 ; minus(200).apply(5000L) -> 4800 ; plus(300).apply(1500L) -> 1800
 *   minus ne descend jamais sous 0 : minus(200).apply(100L) -> 0
 *
 * -- Le plan --
 *
 *   1. Chaque fabrique rend une lambda UnaryOperator<Long> ; minus utilise Math.max(0, ...).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 6 : Engine.price(order)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   regles : VIP -10 %, plus de 5 articles -200, moins de 2000 -> +300
 *   Order(Ada, 5000, 6, vip)  -> 5000 -> 4500 -> 4300          (la 3e ne s'applique pas)
 *   Order(Tim, 1500, 1, !vip) -> 1500 -> 1800                  (seule la 3e)
 *
 * -- Le plan --
 *
 *   1. price = order.cents() ; pour chaque regle (dans l'ordre) : si appliesTo(order), price = applyTo(price).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Oui : appliesTo et applyTo (TODO 1 et 2).
 *
 *
 * ==================================================================
 * TODO 7 : Engine.explain(order)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. La liste des NOMS des regles qui s'appliquent, dans l'ordre.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 8 : pipeline(effects)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Coller plusieurs effets en UNE seule fonction : Function.identity(),
 * puis andThen(effet) pour chacun, dans l'ordre. Une liste vide rend la
 * fonction identite (le prix ne change pas).
 *
 * -- Essayons a la main --
 *
 *   pipeline([percentOff(10), minus(200)]).apply(5000L) -> 4300 ; pipeline([]).apply(7L) -> 7
 *
 * -- Le plan --
 *
 *   1. Function<Long, Long> result = Function.identity() ; pour chaque effet : result = result.andThen(effet).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 9 : vipOnly()    [composition de Predicate]
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Un Predicate<Order> : vip ET plus de 3 articles, en composant deux predicats avec and().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - UnaryOperator<Long> est une Function<Long, Long> : on peut le passer a andThen.
 */
public class Exercise19_PricingRuleEngine {

    public record Order(String customer, long cents, int items, boolean vip) {
    }

    public record Rule(String name, Predicate<Order> when, UnaryOperator<Long> effect) {
        public boolean appliesTo(Order order) {
            throw new UnsupportedOperationException("TODO 1 : implementer appliesTo()");
        }

        public long applyTo(long cents) {
            throw new UnsupportedOperationException("TODO 2 : implementer applyTo()");
        }
    }

    public static UnaryOperator<Long> percentOff(int percent) {
        throw new UnsupportedOperationException("TODO 3 : implementer percentOff()");
    }

    public static UnaryOperator<Long> minus(long cents) {
        throw new UnsupportedOperationException("TODO 4 : implementer minus()");
    }

    public static UnaryOperator<Long> plus(long cents) {
        throw new UnsupportedOperationException("TODO 5 : implementer plus()");
    }

    public static class Engine {
        private final List<Rule> rules = new ArrayList<>();

        public Engine add(Rule rule) {
            rules.add(rule);
            return this;
        }

        public long price(Order order) {
            throw new UnsupportedOperationException("TODO 6 : implementer price()");
        }

        public List<String> explain(Order order) {
            throw new UnsupportedOperationException("TODO 7 : implementer explain()");
        }
    }

    public static Function<Long, Long> pipeline(List<UnaryOperator<Long>> effects) {
        throw new UnsupportedOperationException("TODO 8 : implementer pipeline()");
    }

    public static Predicate<Order> vipOnly() {
        throw new UnsupportedOperationException("TODO 9 : implementer vipOnly()");
    }

    public static void main(String[] args) {
        Order ada = new Order("Ada", 5000, 6, true);
        Order tim = new Order("Tim", 1500, 1, false);
        Rule vip = new Rule("vip", Order::vip, x -> x);
        ExerciseChecker.check("Rule.appliesTo et applyTo", vip.appliesTo(ada) && !vip.appliesTo(tim)
                && new Rule("x", o -> true, c -> c + 1).applyTo(9) == 10);
        ExerciseChecker.check("percentOff, minus (plancher 0), plus",
                percentOff(10).apply(5000L) == 4500 && minus(200).apply(5000L) == 4800 && minus(200).apply(100L) == 0
                        && plus(300).apply(1500L) == 1800);

        Engine engine = new Engine()
                .add(new Rule("vip -10%", Order::vip, percentOff(10)))
                .add(new Rule("gros panier -2e", o -> o.items() > 5, minus(200)))
                .add(new Rule("port +3e", o -> o.cents() < 2000, plus(300)));
        ExerciseChecker.check("price : Ada 4300, Tim 1800", engine.price(ada) == 4300 && engine.price(tim) == 1800);
        ExerciseChecker.check("explain : Ada [vip -10%, gros panier -2e], Tim [port +3e]",
                engine.explain(ada).equals(List.of("vip -10%", "gros panier -2e")) && engine.explain(tim).equals(List.of("port +3e")));
        ExerciseChecker.check("pipeline : 4300 et identite",
                pipeline(List.of(percentOff(10), minus(200))).apply(5000L) == 4300 && pipeline(List.of()).apply(7L) == 7);
        ExerciseChecker.check("vipOnly : Ada oui, VIP avec 2 articles non",
                vipOnly().test(ada) && !vipOnly().test(new Order("Bo", 100, 2, true)) && !vipOnly().test(tim));

        ExerciseChecker.summary();
    }
}
