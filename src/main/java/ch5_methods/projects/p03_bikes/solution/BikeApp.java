package ch5_methods.projects.p03_bikes.solution;

import ch5_methods.projects.p03_bikes.Data;

import static ch5_methods.projects.p03_bikes.solution.Network.km;

/**
 * SOLUTION - l'application de velos en libre-service.
 */
public class BikeApp {

    // Le bloc static de la classe du main s'execute AVANT main.
    static {
        System.out.println("[charge] BikeApp");
    }

    private static int walked;
    private static int ridden;

    public static void main(String[] args) {
        System.out.println("main commence");
        // Premier usage de Network : c'est maintenant que ses initialiseurs static s'executent.
        System.out.println("Gare -> Parc : " + km(Network.index("Gare"), Network.index("Parc")) + " km, diametre " + Network.diameter()
                + " km, voisin le plus proche du Parc " + Network.closest(2) + " km");
        for (int i = 0; i < Data.STATIONS.length; i++) {
            Station.create(Data.STATIONS[i], Data.BIKES[i], Data.CAPACITY[i]);
        }
        System.out.println("depart : " + Station.snapshot());
        for (String trip : Data.TRIPS) {
            String[] p = trip.split(" ");
            System.out.println(ride(Station.get(Network.index(p[0])), Station.get(Network.index(p[1]))));
        }
        System.out.println("arrivee : " + Station.snapshot());
        System.out.println("km a velo " + ridden + ", km a pied " + walked);
        rebalance();
        Station nothing = null;
        // Un membre static s'appelle meme via une reference null : seul le TYPE declare compte.
        System.out.println("static via null : " + nothing.count() + " stations, " + Station.get(4).describe());
    }

    private static String ride(Station from, Station to) {
        StringBuilder log = new StringBuilder(from.name + "->" + to.name + " :");
        Station start = from;
        if (from.bikes == 0) {
            start = Station.nearest(from, true);
            walked += km(from.id, start.id);
            log.append(" vide, marche vers ").append(start.name).append(" (").append(km(from.id, start.id)).append(" km)");
        }
        start.bikes--;
        Station end = to;
        if (to.bikes == to.capacity) {
            end = Station.nearest(to, false);
            walked += km(end.id, to.id);
            log.append(" plein, depot a ").append(end.name).append(" (+").append(km(end.id, to.id)).append(" km a pied)");
        }
        end.bikes++;
        ridden += km(start.id, end.id);
        return log.append(" ").append(km(start.id, end.id)).append(" km").toString();
    }

    // Reequilibrage glouton : a chaque tour, la paire (surplus, manque) la plus proche echange des velos.
    private static void rebalance() {
        int n = Station.count();
        int truckKm = 0;
        StringBuilder moves = new StringBuilder("camion :");
        while (true) {
            int bestFrom = -1;
            int bestTo = -1;
            for (int a = 0; a < n; a++) {
                for (int b = 0; b < n; b++) {
                    Station sa = Station.get(a);
                    Station sb = Station.get(b);
                    boolean surplus = sa.bikes > sa.capacity / 2;
                    boolean lack = sb.bikes < sb.capacity / 2;
                    if (surplus && lack && (bestFrom < 0 || km(a, b) < km(bestFrom, bestTo))) {
                        bestFrom = a;
                        bestTo = b;
                    }
                }
            }
            if (bestFrom < 0) {
                break;
            }
            Station sa = Station.get(bestFrom);
            Station sb = Station.get(bestTo);
            int moved = Math.min(sa.bikes - sa.capacity / 2, sb.capacity / 2 - sb.bikes);
            sa.bikes -= moved;
            sb.bikes += moved;
            truckKm += km(bestFrom, bestTo);
            moves.append(' ').append(moved).append(' ').append(sa.name).append("->").append(sb.name);
        }
        System.out.println(moves + " (" + truckKm + " km) => " + Station.snapshot());
    }
}
