package ch6_classdesign.solutions;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise01_InheritanceBasics.
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
            // Car herite brand (protected), publicInfo (public) et packageInfo (meme paquet) de Vehicle.
            return "Car marque " + brand + " | " + publicInfo + " | " + packageInfo;
        }
    }

    public static boolean isDefaultObjectEquality(Car a, Car b) {
        // Sans redefinition, equals vient d'Object et compare les ADRESSES (==).
        return a.equals(a) && !a.equals(b);
    }
}
