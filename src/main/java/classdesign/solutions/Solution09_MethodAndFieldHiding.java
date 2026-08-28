package classdesign.solutions;

/**
 * Corrige de l'exercice 9. A ne consulter qu'apres avoir essaye par
 * vous-meme dans classdesign.exercises.Exercise09_MethodAndFieldHiding.
 */
public class Solution09_MethodAndFieldHiding {

    static class Parent {
        static String staticGreet() {
            return "Parent.static";
        }

        String instanceGreet() {
            return "Parent.instance";
        }

        String field = "Parent.field";
    }

    static class Child extends Parent {
        static String staticGreet() {
            return "Child.static";
        }

        @Override
        String instanceGreet() {
            return "Child.instance";
        }

        String field = "Child.field";
    }

    public static String describeAll(Parent ref) {
        return ref.staticGreet() + " | " + ref.instanceGreet() + " | " + ref.field;
    }
}
