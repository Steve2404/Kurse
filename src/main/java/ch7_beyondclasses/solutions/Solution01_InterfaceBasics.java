package ch7_beyondclasses.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch7_beyondclasses.exercises.Exercise01_InterfaceBasics.
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
            // Methode abstraite d'interface implementee : elle doit etre public (l'interface l'est implicitement).
            return "Vole jusqu'a " + MAX_ALTITUDE_M + "m";
        }

        @Override
        public String swim() {
            // Une classe peut implementer plusieurs interfaces : elle remplit les trous de chacune.
            return "Nage jusqu'a " + MAX_DEPTH_M + "m";
        }
    }

    public static String describeViaBothInterfaces(Duck duck) {
        // Le meme objet vu sous deux types : chaque variable ne donne acces qu'aux methodes de SON type.
        Flyable flyableRef = duck;
        Swimmable swimmableRef = duck;
        return flyableRef.fly() + " | " + swimmableRef.swim();
    }
}
