package beyondclasses.solutions;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans beyondclasses.exercises.Exercise02_DefaultMethodDiamond.
 */
public class Solution02_DefaultMethodDiamond {

    interface Greeter {
        default String greet(String name) {
            return "Bonjour " + name;
        }
    }

    interface Waver {
        default String greet(String name) {
            return "Coucou " + name;
        }
    }

    interface Describable {
        String name();

        default String describe() {
            return "Je suis " + name();
        }
    }

    static class Robot implements Greeter, Waver, Describable {
        private final String label;

        Robot(String label) {
            this.label = label;
        }

        @Override
        public String greet(String name) {
            return Greeter.super.greet(name) + " / " + Waver.super.greet(name);
        }

        @Override
        public String name() {
            return label;
        }
    }
}
