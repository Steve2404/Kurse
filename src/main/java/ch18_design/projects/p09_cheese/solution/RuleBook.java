package ch18_design.projects.p09_cheese.solution;

import java.util.Map;

/**
 * La FABRIQUE des regles : le SEUL endroit qui connait les noms des produits. Un nouveau produit,
 * c'est une ligne dans la table (et une regle, si aucune ne convient).
 */
public final class RuleBook {

    static final String BIO = "Bio ";
    // Le sel ne bouge jamais : une lambda suffit (une regle d'une ligne n'a pas besoin de classe).
    static final AgingRule SALT = cheese -> cheese;
    static final AgingRule STANDARD = new DecayRule(1);
    static final Map<String, AgingRule> SPECIAL = Map.of(
            "Brie", new DecayRule(2),
            "Comte", new AgedRule(),
            "Billet degustation", new TicketRule(),
            "Sel de Guerande", SALT);

    private RuleBook() {
    }

    public static AgingRule ruleFor(String name) {
        if (name.startsWith(BIO)) {
            return new BioRule(ruleFor(name.substring(BIO.length())));
        }
        return SPECIAL.getOrDefault(name, STANDARD);
    }
}
