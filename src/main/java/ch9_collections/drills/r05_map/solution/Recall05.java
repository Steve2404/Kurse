package ch9_collections.drills.r05_map.solution;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * SOLUTION du drill de rappel 5 - l'interface Map.
 */
public class Recall05 {

    public static void main(String[] args) {
        Map<String, Integer> m = new TreeMap<>();
        Integer before = m.put("a", 1);
        Integer replaced = m.put("a", 2);                   // put rend l'ancienne valeur (ou null)
        m.put("b", 3);
        System.out.println("D01 : " + before + " " + replaced + " " + m + " " + m.get("z") + " " + m.getOrDefault("z", 0) + " " + m.containsKey("b")
                + " " + m.containsValue(2));
        m.putIfAbsent("a", 99);
        m.putIfAbsent("c", 5);
        Integer gone = m.remove("b");
        boolean notRemoved = m.remove("c", 99);
        System.out.println("D02 : " + m + " " + gone + " " + notRemoved);
        Map<String, Integer> counts = new TreeMap<>();
        for (String w : "le chat et le chien et le rat".split(" ")) {
            counts.merge(w, 1, Integer::sum);
        }
        System.out.println("D03 : " + counts);
        counts.merge("et", 0, (old, v) -> null);            // le remappage rend null : la cle disparait
        counts.compute("rat", (k, v) -> v == null ? 1 : v + 10);
        counts.computeIfAbsent("loup", k -> k.length());
        counts.computeIfPresent("chat", (k, v) -> v * 100);
        counts.replaceAll((k, v) -> k.equals("le") ? 0 : v);
        System.out.println("D04 : " + counts);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            sb.append(e.getKey().charAt(0)).append(e.getValue());
        }
        StringBuilder sb2 = new StringBuilder();
        counts.forEach((k, v) -> sb2.append(k.length()));
        System.out.println("D05 : " + sb + " " + sb2 + " " + counts.keySet() + " " + counts.values());
        Map<String, Integer> insertion = new LinkedHashMap<>();
        insertion.put("z", 1);
        insertion.put("a", 2);
        insertion.put("m", 3);
        Map<String, Integer> hash = new HashMap<>(insertion);
        Map<String, Integer> nulls = new HashMap<>();
        nulls.put(null, 0);
        nulls.put("k", null);
        System.out.println("D06 : " + insertion + " " + hash.size() + " " + nulls.get(null) + " " + nulls.containsKey("k") + " " + nulls.get("k"));
    }
}
