package ch7_beyondclasses.projects.p06_fleet.solution;

import ch7_beyondclasses.projects.p06_fleet.Data;

/**
 * SOLUTION du projet 6 - la flotte et le polymorphisme.
 */
public class FleetApp {

    static int index(String place) {
        for (int i = 0; i < Data.PLACES.length; i++) {
            if (Data.PLACES[i].equals(place)) {
                return i;
            }
        }
        return -1;
    }

    // Dijkstra en O(n^2) : temps en minutes ; une liaison n'est utilisable que si le vehicule a une vitesse > 0 sur ce mode.
    static String fastest(Vehicle v) {
        int n = Data.PLACES.length;
        double[] time = new double[n];
        int[] prev = new int[n];
        boolean[] done = new boolean[n];
        java.util.Arrays.fill(time, Double.MAX_VALUE);
        java.util.Arrays.fill(prev, -1);
        int from = index(Data.FROM);
        int to = index(Data.TO);
        time[from] = 0;
        for (int round = 0; round < n; round++) {
            int u = -1;
            for (int i = 0; i < n; i++) {
                if (!done[i] && time[i] < Double.MAX_VALUE && (u < 0 || time[i] < time[u])) {
                    u = i;
                }
            }
            if (u < 0) {
                break;
            }
            done[u] = true;
            for (String link : Data.LINKS) {
                String[] p = link.split(" ");
                int a = index(p[0]);
                int b = index(p[1]);
                int other = a == u ? b : b == u ? a : -1;
                int speed = v.speedOn(Mode.valueOf(p[2]));
                if (other < 0 || speed == 0) {
                    continue;
                }
                double t = time[u] + Integer.parseInt(p[3]) * 60.0 / speed;
                if (t < time[other]) {
                    time[other] = t;
                    prev[other] = u;
                }
            }
        }
        if (time[to] == Double.MAX_VALUE) {
            return "inaccessible";
        }
        StringBuilder path = new StringBuilder(Data.PLACES[to]);
        for (int at = prev[to]; at >= 0; at = prev[at]) {
            path.insert(0, Data.PLACES[at] + " > ");
        }
        return path + " en " + Math.round(time[to] * 10) / 10.0 + " min";
    }

    public static void main(String[] args) {
        Vehicle[] fleet = {new Car("Clio"), new Boat("Nautilus"), new Amphibian("Hippo"), new Plane("Concorde"), new Drone("Bzz")};
        for (Vehicle v : fleet) {
            System.out.println(v + " [" + v.capabilities() + "] : " + fastest(v));
        }

        Amphibian hippo = (Amphibian) fleet[2];   // downcast : le type de l'OBJET est bien Amphibian
        Car asCar = hippo;                        // upcast implicite
        Vehicle asVehicle = hippo;
        Object asObject = hippo;
        System.out.println("un seul objet : " + asCar.roadSpeed() + " " + asVehicle.kind() + " " + asCar.horn() + " " + (asObject instanceof Sailable)
                + " " + ((Sailable) asObject).seaSpeed() + " " + asObject.getClass().getSimpleName());

        Flyable[] flyers = new Flyable[2];
        Drivable[] drivers = new Drivable[fleet.length];
        int f = 0;
        int d = 0;
        for (Vehicle v : fleet) {
            if (v instanceof Flyable fl) {
                flyers[f++] = fl;
            }
            if (v instanceof Drivable dr) {
                drivers[d++] = dr;
            }
        }
        StringBuilder air = new StringBuilder("altitudes :");
        for (Flyable fl : flyers) {
            air.append(' ').append(fl.altitude());
        }
        StringBuilder horns = new StringBuilder("klaxons :");
        for (int i = 0; i < d; i++) {
            horns.append(' ').append(drivers[i].horn());
        }
        System.out.println(air + " ; " + horns + " (" + d + " roulants)");

        // Avant un cast vers une classe, on verifie : un Boat n'est pas un Car.
        StringBuilder safe = new StringBuilder("casts surs :");
        for (Vehicle v : fleet) {
            if (v instanceof Car) {
                Car car = (Car) v;
                safe.append(' ').append(car.name).append("=voiture");
            } else {
                safe.append(' ').append(v.name).append("=non");
            }
        }
        System.out.println(safe);
    }
}
