package pricing.api;

import java.util.List;

/**
 * SOLUTION - l'interface de service : une regle de remise.
 */
public interface PricingRule {

    String name();

    // La remise (en centimes) accordee sur un panier de prix unitaires.
    long discount(List<Long> prices);

    // Une regle cumulable peut s'ajouter a d'autres ; une non cumulable doit rester seule.
    boolean stackable();
}
