package ch7_beyondclasses.projects.p04_routes.solution;

/**
 * SOLUTION - une tournee : l'ordre de visite (indices dans le tableau des villes), retour au depart inclus.
 */
public record Route(int[] order, double km) {

    // Un record IMBRIQUE (implicitement static) : un petit resultat nomme.
    public record Stats(int legs, double longest, String longestLeg) {
    }

    public Route {
        order = order.clone();
    }

    @Override
    public int[] order() {
        return order.clone();
    }

    public static double length(int[] order, double[][] dist) {
        double total = 0;
        for (int i = 0; i < order.length; i++) {
            total += dist[order[i]][order[(i + 1) % order.length]];
        }
        return total;
    }

    public String describe(City[] cities) {
        StringBuilder sb = new StringBuilder();
        for (int i : order) {
            sb.append(cities[i].initials()).append('-');
        }
        return sb.append(cities[order[0]].initials()).append(" = ").append(Math.round(km)).append(" km").toString();
    }

    public Stats stats(City[] cities, double[][] dist) {
        double longest = 0;
        String leg = "";
        for (int i = 0; i < order.length; i++) {
            int a = order[i];
            int b = order[(i + 1) % order.length];
            if (dist[a][b] > longest) {
                longest = dist[a][b];
                leg = cities[a].initials() + "-" + cities[b].initials();
            }
        }
        return new Stats(order.length, Math.round(longest), leg);
    }
}
