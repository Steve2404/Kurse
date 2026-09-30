package ch6_classdesign.exercises;

import ch6_classdesign.ExerciseChecker;

import java.util.ArrayList;
import java.util.List;

/**
 * EXERCICE 18 (CAPSTONE) - Une flotte de vehicules : heritage, constructeurs, abstract, redefinition, final, polymorphisme (niveau : capstone)
 * ========================================================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_InheritanceBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une entreprise loue des vehicules. Vehicle est abstraite : chaque
 * vehicule a un numero unique (un compteur static), une plaque, et un
 * prix au kilometre que CHAQUE sorte de vehicule doit fixer (abstract).
 * Le calcul du prix d'un trajet est le meme pour tous : il est final
 * dans Vehicle (methode template). Un camion ajoute des frais fixes
 * (redefinition de baseFee) et un prix qui depend de sa charge.
 *
 *   Car   : 30 centimes/km, pas de frais fixes
 *   Truck : 50 + 10 x tonnes centimes/km, 2000 centimes de frais fixes
 *   Bike  : 5 centimes/km (deja ecrit)
 *
 *
 * ==================================================================
 * TODO 1 : Vehicle(plate)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. plate null ou blank -> IllegalArgumentException("plaque obligatoire").
 *   2. this.plate = plate ; this.id = nextId++ (le compteur static est partage par TOUS les vehicules).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non. Les enfants appellent ce constructeur avec super(plate).
 *
 *
 * ==================================================================
 * TODO 2 : Vehicle.tripCost(km)    [final : le plan commun]
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   Car, 100 km      -> 100 x 30 + 0 = 3000
 *   Truck 2 t, 100 km -> 100 x (50 + 20) + 2000 = 9000
 *
 * -- Le plan --
 *
 *   1. Rendre (long) km * costPerKmCents() + baseFee().
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : costPerKmCents() et baseFee() sont redefinies par les enfants.
 *
 *
 * ==================================================================
 * TODO 3 : Vehicle.toString()
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 *   "Car#7 AB-123-CD"   (le nom simple de la VRAIE classe, le numero, la plaque)
 *
 * -- Le plan --
 *
 *   1. Rendre getClass().getSimpleName() + "#" + id + " " + plate.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 4 : Car.costPerKmCents()    TODO 5 : Truck.costPerKmCents()    TODO 6 : Truck.baseFee()
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. Car : 30.   2. Truck : 50 + 10 * tons.   3. Truck.baseFee : 2000.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 *
 * ==================================================================
 * TODO 7 : cheapest(fleet, km)    et    TODO 8 : trucksHeavierThan(fleet, tons)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. cheapest : le vehicule au plus petit tripCost(km) (le premier en cas d'egalite).
 *   2. trucksHeavierThan : parcourir, garder ceux qui sont des Truck (instanceof Truck t) avec t.getTons() > tons.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non.
 *
 * Exemple a verifier : voir les tests de main().
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - (long) km * ... evite un debordement int sur de longs trajets.
 */
public class Exercise18_FleetCapstone {

    abstract static class Vehicle {
        private static int nextId = 1;

        private final int id;
        private final String plate;

        protected Vehicle(String plate) {
            throw new UnsupportedOperationException("TODO 1 : implementer Vehicle(plate)");
        }

        public int getId() {
            return id;
        }

        public String getPlate() {
            return plate;
        }

        protected abstract int costPerKmCents();

        protected int baseFee() {
            return 0;
        }

        public final long tripCost(int km) {
            throw new UnsupportedOperationException("TODO 2 : implementer tripCost()");
        }

        @Override
        public String toString() {
            throw new UnsupportedOperationException("TODO 3 : implementer toString()");
        }
    }

    static class Car extends Vehicle {
        Car(String plate) {
            super(plate);
        }

        @Override
        protected int costPerKmCents() {
            throw new UnsupportedOperationException("TODO 4 : implementer Car.costPerKmCents()");
        }
    }

    static class Truck extends Vehicle {
        private final int tons;

        Truck(String plate, int tons) {
            super(plate);
            this.tons = tons;
        }

        public int getTons() {
            return tons;
        }

        @Override
        protected int costPerKmCents() {
            throw new UnsupportedOperationException("TODO 5 : implementer Truck.costPerKmCents()");
        }

        @Override
        protected int baseFee() {
            throw new UnsupportedOperationException("TODO 6 : implementer Truck.baseFee()");
        }
    }

    static class Bike extends Vehicle {
        Bike(String plate) {
            super(plate);
        }

        @Override
        protected int costPerKmCents() {
            return 5;
        }
    }

    public static Vehicle cheapest(List<Vehicle> fleet, int km) {
        throw new UnsupportedOperationException("TODO 7 : implementer cheapest()");
    }

    public static List<Truck> trucksHeavierThan(List<Vehicle> fleet, int tons) {
        throw new UnsupportedOperationException("TODO 8 : implementer trucksHeavierThan()");
    }

    public static void main(String[] args) {
        Vehicle car = new Car("AB-123-CD");
        Vehicle small = new Truck("TR-1", 2);
        Vehicle big = new Truck("TR-2", 12);
        Vehicle bike = new Bike("VE-9");
        ExerciseChecker.check("ids uniques et croissants (compteur static partage)",
                small.getId() == car.getId() + 1 && big.getId() == car.getId() + 2 && bike.getId() == car.getId() + 3);
        String error = null;
        try {
            new Car(" ");
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        ExerciseChecker.check("plaque obligatoire", "plaque obligatoire".equals(error));

        ExerciseChecker.check("tripCost : Car 3000, Truck 2 t 9000, Bike 500",
                car.tripCost(100) == 3000 && small.tripCost(100) == 9000 && bike.tripCost(100) == 500);
        ExerciseChecker.check("toString : Car#" + car.getId() + " AB-123-CD", car.toString().equals("Car#" + car.getId() + " AB-123-CD")
                && big.toString().startsWith("Truck#"));

        List<Vehicle> fleet = new ArrayList<>(List.of(car, small, big, bike));
        ExerciseChecker.check("cheapest(100 km) == le velo", cheapest(fleet, 100) == bike);
        ExerciseChecker.check("cheapest(1 km) : la voiture (30) bat le velo (5) ? non, le velo reste moins cher", cheapest(fleet, 1) == bike);
        ExerciseChecker.check("trucksHeavierThan(5) == [TR-2]", trucksHeavierThan(fleet, 5).equals(List.of(big)));

        ExerciseChecker.summary();
    }
}
