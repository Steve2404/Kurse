package ch18_design.projects.p09_cheese.solution;

/** Les produits qui s'abiment : baseLoss par jour, le double une fois la date passee, jamais sous 0. */
public final class DecayRule implements AgingRule {

    private final int baseLoss;

    public DecayRule(int baseLoss) {
        this.baseLoss = baseLoss;
    }

    @Override
    public Cheese age(Cheese cheese) {
        int sellIn = cheese.sellIn() - 1;
        int loss = sellIn < 0 ? baseLoss * 2 : baseLoss;
        return cheese.next(Math.max(0, cheese.quality() - loss));
    }
}
