package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 10. A ne consulter qu'apres avoir essaye par
 * vous-meme dans beyondclasses.exercises.Exercise10_InnerVsStaticNestedClass.
 */
public class Solution10_InnerVsStaticNestedClass {

    private final String owner;

    public Solution10_InnerVsStaticNestedClass(String owner) {
        this.owner = owner;
    }

    class Badge {
        String print() {
            return "Badge de " + owner;
        }
    }

    static class Standalone {
        private final String label;

        Standalone(String label) {
            this.label = label;
        }

        String print() {
            return "Badge independant : " + label;
        }
    }
}
