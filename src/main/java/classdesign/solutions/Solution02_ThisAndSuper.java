package classdesign.solutions;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans classdesign.exercises.Exercise02_ThisAndSuper.
 */
public class Solution02_ThisAndSuper {

    static class Parent {
        String label = "Parent-label";

        String describe() {
            return "Parent.describe";
        }
    }

    static class Child extends Parent {
        String label = "Child-label";

        String describeAll(String label) {
            return "param=" + label + " | this=" + this.label + " | super=" + super.label;
        }

        @Override
        String describe() {
            return super.describe() + " + Child.describe";
        }
    }
}
