package ch12_modules.projects.p02_pricing;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Il lance TON script build.sh (depuis la racine du depot) et verifie TES modules dans ch12_modules/p02_pricing,
 * ou, avec l'argument "solution", la solution.
 */
public class Check {

    static final String SCRIPT = "build.sh";

    static final String MODULES = "ch12_modules/p02_pricing";

    static final List<String> EXPECTED = List.of(
            // SCRIPT-BEGIN
            "--- tous les fournisseurs",
            "regles : [3pour2+, coupon-15+, soldes-10] ; types [PricingRule, TenPercent, ThreeForTwo]",
            "panier [2000, 1500, 1000] total 4500 -> [3pour2] remise 1000, a payer 3500",
            "panier [4000, 3000, 2000, 1500, 500] total 11000 -> [3pour2, coupon-15] remise 3500, a payer 7500",
            "panier [9000, 2500] total 11500 -> [coupon-15] remise 1500, a payer 10000",
            "panier [800] total 800 -> [soldes-10] remise 80, a payer 720",
            "--- sans pricing.premium",
            "regles : [3pour2+, soldes-10] ; types [TenPercent, ThreeForTwo]",
            "panier [2000, 1500, 1000] total 4500 -> [3pour2] remise 1000, a payer 3500",
            "panier [4000, 3000, 2000, 1500, 500] total 11000 -> [3pour2] remise 2000, a payer 9000",
            "panier [9000, 2500] total 11500 -> [soldes-10] remise 1150, a payer 10350",
            "panier [800] total 800 -> [soldes-10] remise 80, a payer 720",
            "--- describe-module pricing.premium",
            "contains pricing.premium",
            "pricing.premium",
            "provides pricing.api.PricingRule with pricing.premium.Coupons",
            "requires java.base mandated",
            "requires pricing.api",
            "--- resolution",
            "pricing.engine binds pricing.basic",
            "pricing.engine binds pricing.premium");
            // SCRIPT-END

    static final List<String> API = List.of(
            "module pricing.api", "requires transitive pricing.api;", "uses pricing.api.PricingRule;", "provides pricing.api.PricingRule with pricing.basic.TenPercent, pricing.basic.ThreeForTwo;",
            "provides pricing.api.PricingRule with pricing.premium.Coupons;", "public static PricingRule provider()", "ServiceLoader.load(", ".stream()",
            "ServiceLoader.Provider", ".type()", "record Choice(", "1 << ",
            "requires pricing.engine;", "-m shop.app,pricing.basic,pricing.premium", "--limit-modules shop.app,pricing.basic", "--describe-module pricing.premium",
            "--show-module-resolution", " binds ",
            // Crescendo : notions des chapitres 13 a 15 (threads, E/S de fichiers, JDBC), interdites au chapitre 12.
            "!Thread", "!Executor", "!synchronized", "!Atomic", "!parallel", "!Files.",
            "!Path.of", "!Paths.", "!DriverManager", "!Connection", "!System.exit", "!.now()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.checkModules(Check.class, SCRIPT, MODULES, args, EXPECTED, API);
    }
}
