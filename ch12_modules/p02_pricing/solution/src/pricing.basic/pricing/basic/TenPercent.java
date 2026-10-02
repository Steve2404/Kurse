package pricing.basic;

import pricing.api.PricingRule;

import java.util.List;

/**
 * SOLUTION - -10 % sur tout le panier ; non cumulable. Un fournisseur sans methode provider()
 * doit etre public et avoir un constructeur public sans argument.
 */
public class TenPercent implements PricingRule {

    @Override
    public String name() {
        return "soldes-10";
    }

    @Override
    public long discount(List<Long> prices) {
        return prices.stream().mapToLong(Long::longValue).sum() / 10;
    }

    @Override
    public boolean stackable() {
        return false;
    }
}
