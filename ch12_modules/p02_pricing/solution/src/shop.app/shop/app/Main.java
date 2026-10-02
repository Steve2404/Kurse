package shop.app;

import pricing.api.PricingRule;
import pricing.engine.PricingEngine;

import java.util.List;

/**
 * SOLUTION du projet 2 - la caisse : elle prend les regles TROUVEES a l'execution.
 */
public class Main {

    static final List<List<Long>> CARTS = List.of(
            List.of(2_000L, 1_500L, 1_000L),
            List.of(4_000L, 3_000L, 2_000L, 1_500L, 500L),
            List.of(9_000L, 2_500L),
            List.of(800L));

    public static void main(String[] args) {
        List<PricingRule> rules = PricingEngine.loadRules();
        System.out.println("regles : " + rules.stream().map(r -> r.name() + (r.stackable() ? "+" : "")).toList() + " ; types " + PricingEngine.providerTypes());
        for (List<Long> cart : CARTS) {
            long total = cart.stream().mapToLong(Long::longValue).sum();
            PricingEngine.Choice best = PricingEngine.best(rules, cart);
            System.out.println("panier " + cart + " total " + total + " -> " + best.rules() + " remise " + best.discount() + ", a payer " + (total - best.discount()));
        }
    }
}
