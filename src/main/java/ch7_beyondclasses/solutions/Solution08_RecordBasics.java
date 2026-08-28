package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 8. A ne consulter qu'apres avoir essaye par
 * vous-meme dans beyondclasses.exercises.Exercise08_RecordBasics.
 */
public class Solution08_RecordBasics {

    record Point(int x, int y) {
        double distanceFromOrigin() {
            return Math.sqrt(x * x + y * y);
        }

        Point(int x) {
            this(x, x);
        }
    }
}
