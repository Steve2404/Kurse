package ch15_jdbc.projects.p07_bikes.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Le plan de reequilibrage (pur Java) : chaque station vise la moitie de sa capacite (division entiere).
 * Les stations en trop donnent aux stations en manque, dans l'ordre des identifiants : le i-eme velo en trop va au i-eme manque.
 */
final class Rebalancer {

    private Rebalancer() {
    }

    static int target(int capacity) {
        return capacity / 2;
    }

    /** occupancy : station -> {velos presents, capacite}, triee par station. Rend des mouvements {de, vers}, un par velo. */
    static List<String[]> plan(Map<String, int[]> occupancy) {
        List<String> givers = new ArrayList<>();
        List<String> takers = new ArrayList<>();
        // Une station apparait autant de fois qu'elle a de velos a donner (ou a recevoir).
        occupancy.forEach((station, v) -> {
            int gap = v[0] - target(v[1]);
            for (int i = 0; i < gap; i++) {
                givers.add(station);
            }
            for (int i = 0; i < -gap; i++) {
                takers.add(station);
            }
        });
        List<String[]> moves = new ArrayList<>();
        for (int i = 0; i < Math.min(givers.size(), takers.size()); i++) {
            moves.add(new String[]{givers.get(i), takers.get(i)});
        }
        return moves;
    }
}
