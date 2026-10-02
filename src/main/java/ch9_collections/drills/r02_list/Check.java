package ch9_collections.drills.r02_list;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 2 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall02, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [a, B, c] b c 2 -1",
            "D02 : [10, 30] 1",
            "D03 : [BANANE, KIWI, POMME]",
            "D04 : [1, 5]",
            "D05 : 0a1b2ccba",
            "D06 : [2, 4] [d, m, f] df d [m, f]");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".add(0, ", ".set(1, ", ".indexOf(", "nums.remove(1)",
            "Integer.valueOf(20)", ".lastIndexOf(", ".replaceAll(", ".sort(null)",
            ".subList(", "ListIterator<String>", ".nextIndex()", ".hasPrevious()",
            ".previous()", "Iterator<Integer>", "i.remove()", "LinkedList<String>",
            ".getFirst()", ".getLast()",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall02", args, EXPECTED, API);
    }
}
