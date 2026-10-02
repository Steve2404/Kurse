package ch9_collections.drills.r06_immutable.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * SOLUTION du drill de rappel 6 - collections immuables, non modifiables et de taille fixe.
 */
public class Recall06 {

    public static void main(String[] args) {
        String[] array = {"a", "b", "c"};
        List<String> fixed = Arrays.asList(array);          // adossee au tableau, taille fixe
        fixed.set(0, "A");
        array[2] = "C";
        System.out.println("D01 : " + fixed + " " + Arrays.toString(array));
        List<String> source = new ArrayList<>(List.of("x", "y"));
        List<String> copy = List.copyOf(source);             // copie immuable : independante de la source
        List<String> view = Collections.unmodifiableList(source);   // vue non modifiable : SUIT la source
        source.add("z");
        System.out.println("D02 : " + copy + " " + view + " " + copy.size() + " " + view.size());
        List<Integer> of = List.of(3, 1, 2);
        Set<String> set = Set.of("solo");
        Map<String, Integer> map = Map.of("a", 1, "b", 2);
        System.out.println("D03 : " + of + " " + set + " " + new java.util.TreeMap<>(map) + " " + of.contains(2) + " " + map.get("b"));
        List<Integer> sortedCopy = new ArrayList<>(of);      // pour trier, on copie dans une liste modifiable
        Collections.sort(sortedCopy);
        System.out.println("D04 : " + of + " " + sortedCopy);
        Map<String, Integer> entries = Map.ofEntries(Map.entry("k1", 10), Map.entry("k2", 20));
        System.out.println("D05 : " + entries.size() + " " + entries.get("k2") + " " + Map.entry("cle", "valeur") + " " + List.copyOf(Set.of(42)));
        List<String> emptyList = Collections.emptyList();
        System.out.println("D06 : " + emptyList.size() + " " + Collections.nCopies(2, "ab") + " " + Collections.singletonList(7));
    }
}
