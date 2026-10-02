package pricing.engine;

import pricing.api.PricingRule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * SOLUTION - charge les regles disponibles (ServiceLoader) et cherche la meilleure combinaison.
 */
public final class PricingEngine {

    private PricingEngine() {
    }

    // ServiceLoader.load cherche les fournisseurs sur le module path ; l'ordre n'est pas garanti : on trie.
    public static List<PricingRule> loadRules() {
        return ServiceLoader.load(PricingRule.class).stream().map(ServiceLoader.Provider::get)
                .sorted(Comparator.comparing(PricingRule::name)).toList();
    }

    // Les TYPES des fournisseurs, sans les instancier (Provider.type()).
    public static List<String> providerTypes() {
        return ServiceLoader.load(PricingRule.class).stream().map(p -> p.type().getSimpleName()).sorted().toList();
    }

    public record Choice(List<String> rules, long discount) {
    }

    // Toutes les combinaisons (masque de bits sur n regles) ; une combinaison de 2 regles ou plus
    // n'est permise que si toutes sont cumulables. On garde la plus forte remise (a egalite, la plus courte).
    public static Choice best(List<PricingRule> rules, List<Long> prices) {
        Choice best = new Choice(List.of(), 0);
        for (int mask = 1; mask < 1 << rules.size(); mask++) {
            List<PricingRule> picked = new ArrayList<>();
            for (int i = 0; i < rules.size(); i++) {
                if ((mask & 1 << i) != 0) {
                    picked.add(rules.get(i));
                }
            }
            if (picked.size() > 1 && !picked.stream().allMatch(PricingRule::stackable)) {
                continue;
            }
            long discount = picked.stream().mapToLong(r -> r.discount(prices)).sum();
            if (discount > best.discount() || discount == best.discount() && picked.size() < best.rules().size()) {
                best = new Choice(picked.stream().map(PricingRule::name).toList(), discount);
            }
        }
        return best;
    }
}
