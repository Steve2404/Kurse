package ch7_beyondclasses.projects.p03_turtle.solution;

/**
 * SOLUTION - les quatre directions, dans le sens des aiguilles d'une montre.
 */
public enum Direction {
    NORTH(-1, 0), EAST(0, 1), SOUTH(1, 0), WEST(0, -1);

    final int dr;
    final int dc;

    Direction(int dr, int dc) {
        this.dr = dr;
        this.dc = dc;
    }

    // ordinal() donne la position ; Math.floorMod gere les quarts de tour negatifs.
    Direction turn(int quarters) {
        return values()[Math.floorMod(ordinal() + quarters, values().length)];
    }
}
