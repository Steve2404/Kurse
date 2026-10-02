package ch9_collections.drills.r04_queue;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 4 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall04, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : a a a b [c]",
            "D02 : null null true 1",
            "D03 : [3, 2, 1] 3 3 [2, 1]",
            "D04 : [a, b, c, d] ad ad [b, c] b c",
            "D05 : 1245 54 2",
            "D06 : a aa bb 1");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Queue<String> q = new LinkedList<>()", ".offer(", ".peek()", ".element()",
            ".poll()", ".remove()", "Deque<Integer> stack", ".push(",
            ".pop()", ".offerFirst(", ".offerLast(", ".addFirst(",
            ".addLast(", ".peekFirst()", ".peekLast()", ".pollFirst()",
            ".pollLast()", ".removeFirst()", ".getLast()", "new PriorityQueue<>(",
            "Collections.reverseOrder()",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall04", args, EXPECTED, API);
    }
}
