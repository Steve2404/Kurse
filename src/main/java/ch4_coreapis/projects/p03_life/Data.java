package ch4_coreapis.projects.p03_life;

/**
 * Les donnees du projet 3 (DONNEES, ne pas modifier).
 * '#' = cellule vivante, '.' = cellule morte.
 */
public final class Data {

    /** Un planeur (en haut a gauche) et un clignotant (a droite). */
    public static final String[] WORLD = {
        "..........",
        "..#.......",
        "...#...###",
        ".###......",
        "..........",
        "..........",
        ".........."
    };

    /** Un clignotant seul, pour detecter une periode. */
    public static final String[] BLINKER = {
        ".....",
        "..#..",
        "..#..",
        "..#..",
        "....."
    };

    public static final int GENERATIONS = 4;

    private Data() {
    }
}
