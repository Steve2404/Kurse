package ch18_design.projects.p09_cheese.solution;

/**
 * Le billet de degustation : sa valeur monte a l'approche de l'evenement (+1, +2 a moins de 10 jours,
 * +3 a moins de 5), puis tombe a 0 quand l'evenement est passe.
 */
public final class TicketRule implements AgingRule {

    @Override
    public Cheese age(Cheese cheese) {
        int sellIn = cheese.sellIn() - 1;
        if (sellIn < 0) {
            return cheese.next(0);
        }
        int gain = 1 + (sellIn < 10 ? 1 : 0) + (sellIn < 5 ? 1 : 0);
        return cheese.next(AgingRule.raise(cheese.quality(), gain));
    }
}
