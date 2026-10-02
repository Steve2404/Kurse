package pricing.premium;

import pricing.api.PricingRule;

import java.util.List;

/**
 * SOLUTION - une "fabrique" de fournisseur : ServiceLoader appelle la methode public static provider()
 * (la classe n'a alors besoin ni d'implementer l'interface, ni d'un constructeur public).
 */
public final class Coupons {

    private Coupons() {
    }

    public static PricingRule provider() {
        return new PricingRule() {
            @Override
            public String name() {
                return "coupon-15";
            }

            @Override
            public long discount(List<Long> prices) {
                return prices.stream().mapToLong(Long::longValue).sum() >= 10_000 ? 1_500 : 0;
            }

            @Override
            public boolean stackable() {
                return true;
            }
        };
    }
}
