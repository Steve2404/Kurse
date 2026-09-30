package ch6_classdesign.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 18. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch6_classdesign.exercises.Exercise18_FleetCapstone.
 */
public class Solution18_FleetCapstone {

    abstract static class Vehicle {
        private static int nextId = 1;

        private final int id;
        private final String plate;

        protected Vehicle(String plate) {
            // Valider avant d'affecter ; le compteur static donne un numero unique a CHAQUE vehicule.
            if (plate == null || plate.isBlank()) {
                throw new IllegalArgumentException("plaque obligatoire");
            }
            this.plate = plate;
            this.id = nextId++;
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
            // Methode template final : le calcul est commun, les deux etapes sont redefinies.
            return (long) km * costPerKmCents() + baseFee();
        }

        @Override
        public String toString() {
            // getClass() donne la VRAIE classe de l'objet (Car, Truck...), meme appele depuis Vehicle.
            return getClass().getSimpleName() + "#" + id + " " + plate;
        }
    }

    static class Car extends Vehicle {
        Car(String plate) {
            super(plate);
        }

        @Override
        protected int costPerKmCents() {
            // Le trou abstract rempli par la classe concrete.
            return 30;
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
            // Depend d'un champ propre a Truck.
            return 50 + 10 * tons;
        }

        @Override
        protected int baseFee() {
            // Redefinition d'une methode concrete du parent.
            return 2000;
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
        // Polymorphisme : tripCost utilise les versions de chaque vehicule.
        Vehicle best = fleet.get(0);
        for (Vehicle v : fleet) {
            if (v.tripCost(km) < best.tripCost(km)) {
                best = v;
            }
        }
        return best;
    }

    public static List<Truck> trucksHeavierThan(List<Vehicle> fleet, int tons) {
        // instanceof avec variable : on accede a getTons(), qui n'existe que dans Truck.
        List<Truck> result = new ArrayList<>();
        for (Vehicle v : fleet) {
            if (v instanceof Truck t && t.getTons() > tons) {
                result.add(t);
            }
        }
        return result;
    }
}
