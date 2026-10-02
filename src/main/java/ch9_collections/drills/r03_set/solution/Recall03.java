package ch9_collections.drills.r03_set.solution;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * SOLUTION du drill de rappel 3 - Set, HashSet, LinkedHashSet, TreeSet.
 */
public class Recall03 {

    public static void main(String[] args) {
        List<String> source = List.of("delta", "alpha", "charlie", "alpha", "bravo");
        Set<String> hash = new HashSet<>(source);
        Set<String> linked = new LinkedHashSet<>(source);
        Set<String> tree = new TreeSet<>(source);
        System.out.println("D01 : " + hash.size() + " " + linked + " " + tree);
        NavigableSet<Integer> n = new TreeSet<>(List.of(10, 5, 20, 15, 30));
        System.out.println("D02 : " + n.first() + " " + n.last() + " " + n.floor(12) + " " + n.ceiling(12) + " " + n.lower(10) + " " + n.higher(30));
        System.out.println("D03 : " + n.headSet(15) + " " + n.tailSet(15) + " " + n.subSet(5, 20) + " " + n.headSet(15, true) + " " + n.descendingSet());
        Integer polled = n.pollFirst();
        System.out.println("D04 : " + polled + " " + n.pollLast() + " " + n);
        Set<Integer> a = new TreeSet<>(Set.of(1, 2, 3, 4));
        Set<Integer> b = Set.of(3, 4, 5);
        Set<Integer> union = new TreeSet<>(a);
        union.addAll(b);
        Set<Integer> inter = new TreeSet<>(a);
        inter.retainAll(b);
        Set<Integer> diff = new TreeSet<>(a);
        diff.removeAll(b);
        System.out.println("D05 : " + union + " " + inter + " " + diff);
        Set<String> byLength = new TreeSet<>((x, y) -> x.length() - y.length());   // compare == 0 : consideres EGAUX
        byLength.addAll(List.of("ab", "cd", "efg", "h"));
        System.out.println("D06 : " + byLength + " " + byLength.size() + " " + byLength.contains("zz"));
    }
}
