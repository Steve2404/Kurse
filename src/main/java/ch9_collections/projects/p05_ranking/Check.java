package ch9_collections.projects.p05_ranking;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Ranking, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "ordre naturel : [Adam, Bob, Emma, Hugo, Ines, Lea, Noah, Zoe]",
            "classement (score desc, age, nom) : [Hugo, Ines, Bob, Zoe, Lea, Noah, Adam, Emma]",
            "par equipe : [Hugo, Noah, Zoe, Ines, Lea, Emma, Bob, Adam] ; par bonus (null a la fin) : [Zoe, Lea, Noah, Ines, Bob, Adam, Emma, Hugo]",
            "rangs : Hugo=1/1 Ines=1/1 Bob=1/1 Zoe=4/2 Lea=4/2 Noah=4/2 Adam=7/3 Emma=8/4",
            "TreeSet par score : [Emma, Adam, Zoe, Hugo] (4 sur 8)",
            "navigation : floorKey(1300) 1200, ceilingKey(1300) 1500, lowerKey(1200) 980, higherKey(1500) null, firstEntry 870=[Emma], lastKey 1500",
            "vues : headMap(1200) {870=[Emma], 980=[Adam]}, tailMap(1200) [1200, 1500], subMap(900, 1300) [980, 1200], descending [1500, 1200, 980, 870]",
            "ages : [25, 29, 31, 37, 42], first 25, last 42, floor(30) 29, ceiling(30) 31, headSet(31) [25, 29], tailSet(31, false) [37, 42], pollFirst 25 -> [29, 31, 37, 42]",
            "intervalles tries [[1,3], [2,6], [5,7], [8,10], [9,12], [15,18], [17,20]] -> fusion [[1,7], [8,12], [15,20]] ; planning max [[1,3], [5,7], [8,10], [15,18]]",
            "medianes : 5.0 10.0 5.0 4.0 5.0 6.0 7.0 7.5 8.0 7.5");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.PLAYERS", "Data.INTERVALS", "implements Comparable<Player>", "public int compareTo(Player",
            "implements Comparable<Interval>", "Collections.sort(", "Comparator.comparingInt(", ".reversed()",
            ".thenComparing(", "Comparator.naturalOrder()", "Comparator.nullsLast(", "NavigableMap<Integer, List<String>>",
            ".floorKey(", ".ceilingKey(", ".lowerKey(", ".higherKey(",
            ".firstEntry()", ".headMap(", ".tailMap(", ".subMap(",
            ".descendingMap()", "NavigableSet<Integer>", ".floor(", ".ceiling(",
            ".headSet(", ".tailSet(", ".pollFirst()", "new PriorityQueue<>(Collections.reverseOrder())",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Ranking", args, EXPECTED, API);
    }
}
