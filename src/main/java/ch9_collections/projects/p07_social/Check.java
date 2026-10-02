package ch9_collections.projects.p07_social;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 7 (capstone) (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Social, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "ana : [bob, chloe, gina] ; 10 membres ; communautes [[ana, bob, chloe, dan, eve, fred, gina], [hugo, ines, jade]] (8 unions)",
            "suggestions : [eve=2, dan=1, fred=1] ; amis communs bob/chloe [ana]",
            "degres : {ana=0, bob=1, chloe=1, dan=2, eve=2, fred=2, gina=1} ; injoignables [hugo, ines, jade]",
            "fil de ana : [#11(bob 18h), #6(chloe 15h), #4(bob 14h), #8(ana 12h), #2(chloe 10h)]",
            "likes par tag : {collections=21, cuisine=80, java=41, map=5, set=3, sport=67, voyage=105} ; tendances [voyage=105, cuisine=80, sport=67]",
            "plus connectes : [ana, bob, chloe] ; copyOf d'un set immuable = meme objet true [collections, java]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.FRIENDS", "Data.POSTS", "class UnionFind<T extends Comparable<T>>", "Map<T, T> parent",
            "record Cursor(", "Set.copyOf(", "Map<String, Set<String>>", ".merge(",
            "Map.Entry.<String, Integer>comparingByValue().reversed()", ".retainAll(", ".removeAll(", "Queue<String> queue = new ArrayDeque<>()",
            "PriorityQueue<Cursor>", "PriorityQueue<Map.Entry<String, Integer>>", ".subList(",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Social", args, EXPECTED, API);
    }
}
