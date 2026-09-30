package ch6_classdesign.solutions;

/**
 * Corrige de l'exercice 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise02_ThisAndSuper.
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
            // label seul = le parametre ; this.label = le champ de Child ; super.label = le champ cache de Parent.
            return "param=" + label + " | this=" + this.label + " | super=" + super.label;
        }

        @Override
        String describe() {
            // super.describe() reutilise la version du parent avant d'ajouter la sienne.
            return super.describe() + " + Child.describe";
        }
    }
}
