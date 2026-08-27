package beyondclasses.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans beyondclasses.exercises.Exercise01_InterfaceBasics.
 */
public class Solution01_InterfaceBasics {

    interface Flyable {
        double MAX_ALTITUDE_M = 12000;

        String fly();
    }

    interface Swimmable {
        double MAX_DEPTH_M = 300;

        String swim();
    }

    static class Duck implements Flyable, Swimmable {
        @Override
        public String fly() {
            return "Vole jusqu'a " + MAX_ALTITUDE_M + "m";
        }

        @Override
        public String swim() {
            return "Nage jusqu'a " + MAX_DEPTH_M + "m";
        }
    }

    public static String describeViaBothInterfaces(Duck duck) {
        Flyable flyableRef = duck;
        Swimmable swimmableRef = duck;
        return flyableRef.fly() + " | " + swimmableRef.swim();
    }
}
