package ch9_collections.projects.p03_graphs;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 3 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Graphs, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "ordre des cours : [bases, algo, io, poo, collections, jdbc, lambdas, streams, concurrence]",
            "avec un cycle : [] -> cycle detecte",
            "voisins de Lyon : {Geneve=150, Marseille=315, Paris=465} ; profondeur depuis Paris : [Paris, Lille, Bruxelles, Lyon, Geneve, Marseille, Toulouse, Bordeaux, Nantes]",
            "moins d'etapes Lille-Toulouse : [Lille, Paris, Lyon, Marseille, Toulouse]",
            "moins de km : Lille > Paris > Nantes > Bordeaux > Toulouse = 1200 km (9 villes fixees)",
            "composantes : [[Ajaccio, Bastia], [Bordeaux, Bruxelles, Geneve, Lille, Lyon, Marseille, Nantes, Paris, Toulouse]]",
            "max glissant (3) : [12, 12, 12, 8, 8, 8, 9, 9]",
            "deque : sommet z, pop z, pollLast c, reste [a, b], peekLast b ; vide : poll null, peek null");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.COURSES", "Data.ROADS", "Map<String, List<String>>", "Queue<String> ready = new PriorityQueue<>()",
            "Deque<String> stack = new ArrayDeque<>()", ".push(", ".pop()", ".offer(",
            ".poll()", "record Step(", "Comparator.comparingInt(Step::km)", ".peekFirst()",
            ".peekLast()", ".pollFirst()", ".pollLast()", ".offerLast(",
            ".offerFirst(", "LinkedList<String> path", "visited.add(",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Graphs", args, EXPECTED, API);
    }
}
