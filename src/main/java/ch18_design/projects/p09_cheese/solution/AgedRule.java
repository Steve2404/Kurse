package ch18_design.projects.p09_cheese.solution;

/** Le fromage affine : il s'ameliore de 1 par jour, de 2 une fois la date passee, jusqu'a 50. */
public final class AgedRule implements AgingRule {

    @Override
    public Cheese age(Cheese cheese) {
        int gain = cheese.sellIn() - 1 < 0 ? 2 : 1;
        return cheese.next(AgingRule.raise(cheese.quality(), gain));
    }
}
