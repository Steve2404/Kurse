package collections.solutions;

import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Corrige de l'exercice 7.
 */
public class Solution07_NavigableMapSet {

    public static String closestPrice(TreeMap<Integer, String> prices, int target) {
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
        return new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    }

    public static String explainNullPointerException() {
        return "Un TreeSet doit pouvoir comparer les elements entre eux (via compareTo ou un "
                + "Comparator) pour maintenir l'ordre trie ; comparer un element a null n'a pas de "
                + "sens et leve donc une NullPointerException, contrairement a un HashSet qui se "
                + "base sur hashCode()/equals() et accepte un null.";
    }
}