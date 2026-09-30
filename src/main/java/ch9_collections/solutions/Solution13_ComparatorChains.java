package ch9_collections.solutions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Corrige de l'exercice 13.
 */
public class Solution13_ComparatorChains {

    public record Player(String name, String team, int score, Integer age) {
    }

    public static Comparator<Player> byScoreDescThenName() {
        // reversed() place JUSTE apres le score : il ne retourne que lui ; le nom reste croissant.
        return Comparator.comparingInt(Player::score).reversed().thenComparing(Player::name);
    }

    public static Comparator<Player> byScoreThenNameBothDesc() {
        // reversed() a la fin retourne toute la chaine a sa gauche : score ET nom decroissants.
        return Comparator.comparingInt(Player::score).thenComparing(Player::name).reversed();
    }

    public static Comparator<Player> byTeamThenAgeNullsLast() {
        // Sans nullsLast, comparer un age null leverait NullPointerException ; il enveloppe l'ordre naturel.
        return Comparator.comparing(Player::team)
                .thenComparing(Player::age, Comparator.nullsLast(Comparator.naturalOrder()));
    }

    public static List<String> ranking(List<Player> players) {
        // Tri d'une COPIE (List.of est immuable) ; un ex aequo garde le rang precedent, sinon rang = position + 1.
        List<Player> sorted = new ArrayList<>(players);
        sorted.sort(byScoreDescThenName());
        List<String> result = new ArrayList<>();
        int rank = 0;
        for (int i = 0; i < sorted.size(); i++) {
            if (i == 0 || sorted.get(i).score() != sorted.get(i - 1).score()) {
                rank = i + 1;
            }
            result.add(rank + " " + sorted.get(i).name() + " " + sorted.get(i).score());
        }
        return result;
    }

    public static int binarySearch(List<Integer> sorted, int key) {
        // A la sortie de la boucle, low est exactement le point d'insertion ; -(low) - 1 est toujours negatif, meme pour 0.
        int low = 0;
        int high = sorted.size() - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int value = sorted.get(mid);
            if (value == key) {
                return mid;
            } else if (value < key) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -low - 1;
    }

    public static void insertSorted(List<Integer> sorted, int key) {
        // On decode le point d'insertion de binarySearch : O(log n) pour trouver la place au lieu de retrier.
        int r = Collections.binarySearch(sorted, key);
        sorted.add(r >= 0 ? r : -r - 1, key);
    }
}
