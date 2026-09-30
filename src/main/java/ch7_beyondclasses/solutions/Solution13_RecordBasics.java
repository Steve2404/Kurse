package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 13. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise13_RecordBasics.
 */
public class Solution13_RecordBasics {

    record Point(int x, int y) {
        double distanceFromOrigin() {
            // Un record peut avoir ses methodes ; x et y sont lisibles directement (champs private final).
            return Math.sqrt(x * x + y * y);
        }

        Point(int x) {
            // Un constructeur non canonique DOIT commencer par this(...) vers le canonique.
            this(x, x);
        }
    }
}
