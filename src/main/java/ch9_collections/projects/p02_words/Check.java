package ch9_collections.projects.p02_words;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Words, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "mots distincts 20, java 2, python null, getOrDefault(python) 0, containsKey(set) true, containsValue(4) false",
            "ordre d'apparition : java aime collections liste map set associe cle ...",
            "top 5 : [set=3, collections=2, java=2, liste=2, map=2]",
            "index : collections[1, 3] java[1, 3] liste[1, 3] map[1, 2] set[1, 2, 3]",
            "requete java & collections -> [1, 3]",
            "requete map | streams -> [1, 2, 3]",
            "requete set - map -> [3]",
            "requete liste & chien -> []",
            "anagrammes : [[arme, mare, rame], [chien, chine, niche]]",
            "merge/compute : {java=20, set=12}, remove(set, 999) false, entry k=1, ofEntries {a=1, b=2}",
            "sets : hash 4 elements, linked [set, map, java, liste], tree [java, liste, map, set], add en double false");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.DOCS", "Data.QUERIES", "new HashMap<>()", "new LinkedHashMap<>()",
            "new TreeMap<>()", ".merge(", ".putIfAbsent(", ".computeIfAbsent(",
            ".getOrDefault(", ".containsKey(", ".containsValue(", "PriorityQueue<Map.Entry<String, Integer>>",
            "Map.Entry.comparingByValue()", "Map.Entry.comparingByKey(", ".entrySet()", ".retainAll(",
            ".addAll(", ".removeAll(", ".values().removeIf(", ".compute(",
            ".computeIfPresent(", "Map.entry(", "Map.ofEntries(", "new LinkedHashSet<>(",
            "new TreeSet<>(", "new HashSet<>(",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Words", args, EXPECTED, API);
    }
}
