package ch6_classdesign.solutions;

/**
 * Corrige de l'exercice 11. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise11_MethodAndFieldHiding.
 */
public class Solution11_MethodAndFieldHiding {

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

    @SuppressWarnings("static")
    public static String describeAll(Parent ref) {
        // static et champs suivent le TYPE de ref (Parent) ; seule la methode d'instance suit l'objet reel.
        return ref.staticGreet() + " | " + ref.instanceGreet() + " | " + ref.field;
    }
}
