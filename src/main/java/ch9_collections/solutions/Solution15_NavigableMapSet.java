package ch9_collections.solutions;

import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Corrige de l'exercice 15.
 */
public class Solution15_NavigableMapSet {

    public static String closestPrice(TreeMap<Integer, String> prices, int target) {
        // floorKey / ceilingKey donnent les deux voisins en O(log n) ; null quand il n'y a pas de voisin de ce cote ; egalite -> floor.
        if (prices.isEmpty()) {
            return null;
        }
        Integer floor = prices.floorKey(target);
        Integer ceiling = prices.ceilingKey(target);

        if (floor == null) {
            return prices.get(ceiling);
        }
        if (ceiling == null) {
            return prices.get(floor);
        }
        int floorDistance = target - floor;
        int ceilingDistance = ceiling - target;
        return floorDistance <= ceilingDistance ? prices.get(floor) : prices.get(ceiling);
    }

    public static TreeSet<String> caseInsensitiveTreeSetWithComparator() {
        // CASE_INSENSITIVE_ORDER est un Comparator tout pret ; il decide aussi de l'unicite dans le TreeSet.
        return new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    }

    public static TreeSet<String> nullFriendlyTreeSet(List<String> words) {
        // Sans comparateur, TreeSet appelle compareTo sur null -> NullPointerException ;
        // nullsFirst decide d'avance ou va null et delegue le reste a l'ordre naturel.
        TreeSet<String> set = new TreeSet<>(Comparator.nullsFirst(Comparator.<String>naturalOrder()));
        set.addAll(words);
        return set;
    }
}