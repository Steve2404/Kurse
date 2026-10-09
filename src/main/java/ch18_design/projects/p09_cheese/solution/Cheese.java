package ch18_design.projects.p09_cheese.solution;

/** Un article du stock, IMMUABLE : vieillir rend un nouvel article (le legacy modifiait l'ancien). */
public record Cheese(String name, int sellIn, int quality) {

    public Cheese {
        if (quality < 0) {
            throw new IllegalArgumentException("qualite negative : " + quality);
        }
    }

    public Cheese next(int quality) {
        return new Cheese(name, sellIn - 1, quality);
    }
}
