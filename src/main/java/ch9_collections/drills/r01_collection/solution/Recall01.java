package ch9_collections.drills.r01_collection.solution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * SOLUTION du drill de rappel 1 - l'interface Collection, commune aux List, Set et Queue.
 */
public class Recall01 {

    public static void main(String[] args) {
        Collection<String> c = new ArrayList<>();
        boolean added = c.add("java");
        c.add("map");
        c.add("java");
        System.out.println("D01 : " + added + " " + c.size() + " " + c.isEmpty() + " " + c.contains("map") + " " + c);
        Collection<String> set = new HashSet<>();
        boolean first = set.add("java");
        boolean again = set.add("java");
        System.out.println("D02 : " + first + " " + again + " " + set.size());
        boolean removed = c.remove("java");                 // retire la PREMIERE occurrence
        boolean missing = c.remove("python");
        System.out.println("D03 : " + removed + " " + missing + " " + c);
        Collection<Integer> numbers = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        boolean changed = numbers.removeIf(n -> n % 2 == 0);
        StringBuilder sb = new StringBuilder();
        numbers.forEach(n -> sb.append(n).append(';'));
        System.out.println("D04 : " + changed + " " + numbers + " " + sb);
        Collection<String> a = new ArrayList<>(List.of("x", "y", "z"));
        Collection<String> b = List.of("y", "z", "w");
        a.addAll(b);
        boolean retained = a.retainAll(List.of("y", "w"));
        System.out.println("D05 : " + a + " " + retained + " " + a.containsAll(List.of("y", "w")));
        c.clear();
        System.out.println("D06 : " + c.isEmpty() + " " + new ArrayList<>(List.of(1, 2)).equals(List.of(1, 2)) + " " + new HashSet<>(List.of(1, 2)).equals(List.of(1, 2)));
    }
}
