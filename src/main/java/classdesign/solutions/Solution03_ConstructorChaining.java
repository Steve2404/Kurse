package classdesign.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 3. A ne consulter qu'apres avoir essaye par
 * vous-meme dans classdesign.exercises.Exercise03_ConstructorChaining.
 */
public class Solution03_ConstructorChaining {

    static class Vehicle {
        protected final List<String> log;

        Vehicle(List<String> log) {
            this.log = log;
            log.add("Vehicle");
        }
    }

    static class Car extends Vehicle {
        Car(List<String> log) {
            super(log);
            log.add("Car");
        }

        Car() {
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
