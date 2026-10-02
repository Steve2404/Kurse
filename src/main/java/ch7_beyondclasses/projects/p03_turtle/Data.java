package ch7_beyondclasses.projects.p03_turtle;

/**
 * Les donnees du projet 3 (DONNEES, ne pas modifier).
 * Langage : MOVE n, RIGHT, LEFT, PENUP, PENDOWN, SQUARE n, STAIRS n, REPEAT n [ ... ] (imbricable).
 * Les mots sont separes par UN espace, crochets compris.
 */
public final class Data {

    public static final String PROGRAM =
            "SQUARE 4 PENUP MOVE 6 PENDOWN REPEAT 2 [ REPEAT 3 [ MOVE 2 RIGHT MOVE 1 LEFT ] MOVE 1 ] PENUP RIGHT MOVE 2 RIGHT MOVE 20 LEFT LEFT PENDOWN STAIRS 3";

    /** La taille de la grille. La tortue part en ligne 1, colonne 1, cap a l'est, crayon baisse. */
    public static final int ROWS = 14;
    public static final int COLS = 30;

    private Data() {
    }
}
