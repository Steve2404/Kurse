package pricing.basic;

import pricing.api.PricingRule;

import java.util.Comparator;
import java.util.List;

/**
 * SOLUTION - 3 pour 2 : prix tries du plus cher au moins cher, chaque 3e article est offert ; cumulable.
 */
public class ThreeForTwo implements PricingRule {

    @Override
    public String name() {
        return "3pour2";
    }

    @Override
    public long discount(List<Long> prices) {
        List<Long> sorted = prices.stream().sorted(Comparator.reverseOrder()).toList();
        long free = 0;
        for (int i = 2; i < sorted.size(); i += 3) {
            free += sorted.get(i);
        }
        return free;
    }

    @Override
    public boolean stackable() {
        return true;
    }
}
