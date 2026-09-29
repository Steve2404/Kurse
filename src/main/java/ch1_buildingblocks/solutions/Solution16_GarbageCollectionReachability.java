package ch1_buildingblocks.solutions;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Corrige de l'exercice 16. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch1_buildingblocks.exercises.Exercise16_GarbageCollectionReachability.
 */
public class Solution16_GarbageCollectionReachability {

    public static Set<String> reachable(Map<String, String> variables, Map<String, List<String>> fields) {
        // On part des variables vivantes (les "racines") et on suit les references.
        // Le Set "seen" empeche de tourner en rond dans un ilot N1 <-> N2.
        Set<String> seen = new HashSet<>();
        Deque<String> toVisit = new ArrayDeque<>();
        for (String target : variables.values()) {
            if (target != null) {
                toVisit.push(target);
            }
        }
        while (!toVisit.isEmpty()) {
            String current = toVisit.pop();
            if (seen.add(current)) {
                for (String next : fields.getOrDefault(current, List.of())) {
                    toVisit.push(next);
                }
            }
        }
        return seen;
    }

    public static List<String> eligibleForGc(List<String> allObjects, Map<String, String> variables,
                                             Map<String, List<String>> fields) {
        // Eligible = non atteignable depuis une racine, meme si d'autres objets pointent encore sur lui.
        Set<String> alive = reachable(variables, fields);
        List<String> eligible = new ArrayList<>();
        for (String object : allObjects) {
            if (!alive.contains(object)) {
                eligible.add(object);
            }
        }
        return eligible;
    }
}
