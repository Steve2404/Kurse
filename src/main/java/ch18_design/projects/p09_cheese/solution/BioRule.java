package ch18_design.projects.p09_cheese.solution;

/**
 * Etape 7, un DECORATEUR (projet 6) : un produit bio perd sa qualite deux fois plus vite que la regle
 * qu'il enveloppe. S'il en gagne (un comte bio), rien ne change.
 */
public final class BioRule implements AgingRule {

    private final AgingRule inner;

    public BioRule(AgingRule inner) {
        this.inner = inner;
    }

    @Override
    public Cheese age(Cheese cheese) {
        Cheese aged = inner.age(cheese);
        int lost = cheese.quality() - aged.quality();
        return lost > 0 ? cheese.next(Math.max(0, cheese.quality() - 2 * lost)) : aged;
    }
}
