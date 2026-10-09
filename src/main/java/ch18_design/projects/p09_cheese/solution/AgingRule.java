package ch18_design.projects.p09_cheese.solution;

/** Comment un article vieillit d'un jour. Une regle par sorte de produit (des strategies). */
@FunctionalInterface
public interface AgingRule {

    int MAX_QUALITY = 50;

    Cheese age(Cheese cheese);

    /**
     * Ajoute gain sans depasser 50... mais sans jamais BAISSER un article deja au-dessus de 50 :
     * le legacy n'ajoute que "si quality < 50", donc un article a 55 reste a 55.
     */
    static int raise(int quality, int gain) {
        return Math.max(quality, Math.min(MAX_QUALITY, quality + gain));
    }
}
