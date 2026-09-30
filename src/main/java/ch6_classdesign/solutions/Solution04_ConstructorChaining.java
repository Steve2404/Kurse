package ch6_classdesign.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise04_ConstructorChaining.
 */
public class Solution04_ConstructorChaining {

    static class Vehicle {
        protected final List<String> log;

        Vehicle(List<String> log) {
            this.log = log;
            log.add("Vehicle");
        }
    }

    static class Car extends Vehicle {
        Car(List<String> log) {
            // super(log) DOIT etre en 1re ligne : le parent est construit avant l'enfant.
            super(log);
            log.add("Car");
        }

        Car() {
            // this(...) delegue a l'autre constructeur (qui appellera super) : jamais les deux dans le meme.
            this(new ArrayList<>());
        }

        List<String> log() {
            return log;
        }
    }

    static class SimpleParent {
        String whoAmI() {
            return "SimpleParent";
        }
    }

    static class SimpleChild extends SimpleParent {
    }
}
