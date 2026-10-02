// SOLUTION - le LOCALISATEUR de service : il declare qu'il utilise le service (uses) et le recherche avec ServiceLoader.
module pricing.engine {
    // transitive : l'API de PricingEngine expose des PricingRule.
    requires transitive pricing.api;
    exports pricing.engine;
    uses pricing.api.PricingRule;
}
