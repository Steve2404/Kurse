package classdesign.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans classdesign.exercises.Exercise01_InheritanceBasics.
 */
public class Solution01_InheritanceBasics {

    static class Vehicle {
        protected String brand = "Toyota";
        public String publicInfo = "Vehicule generique";
        String packageInfo = "Info de paquet";

        public String honk() {
            return "Pouet";
        }
    }

    static class Car extends Vehicle {
        String describe() {
            return "Car marque " + brand + " | " + publicInfo + " | " + packageInfo;
        }
    }

    public static boolean isDefaultObjectEquality(Car a, Car b) {
        return a.equals(a) && !a.equals(b);
    }
}
