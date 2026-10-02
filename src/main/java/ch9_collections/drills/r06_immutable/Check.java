package ch9_collections.drills.r06_immutable;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall06, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : [A, b, C] [A, b, C]",
            "D02 : [x, y] [x, y, z] 2 3",
            "D03 : [3, 1, 2] [solo] {a=1, b=2} true 2",
            "D04 : [3, 1, 2] [1, 2, 3]",
            "D05 : 2 20 cle=valeur [42]",
            "D06 : 0 [ab, ab] [7]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Arrays.asList(", "List.copyOf(", "Collections.unmodifiableList(", "List.of(",
            "Set.of(", "Map.of(", "Map.ofEntries(", "Map.entry(",
            "Collections.emptyList()", "Collections.nCopies(", "Collections.singletonList(",
            // Crescendo : notions des chapitres 10 a 15, interdites au chapitre 9.
            "!.stream(", "!Stream.", "!Stream<", "!Collectors", "!IntStream", "!LongStream", "!DoubleStream", "!.lines()",
            "!Optional", "!.chars()", "!catch", "!throw ", "!Locale", "!DateTimeFormatter", "!NumberFormat", "!.now()",
            "!parallel");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall06", args, EXPECTED, API);
    }
}
