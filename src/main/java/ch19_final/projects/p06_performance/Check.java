package ch19_final.projects.p06_performance;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 */
public class Check {

    static final List<Mutant> MUTANTS = List.of(
            new Mutant("FastReport.java", "} else if (!seen.add(line)) {", "} else if (events.stream().anyMatch(e -> false) || !seen.add(line)) {"),
            new Mutant("FastReport.java", "Set<String> seen = new HashSet<>();", "java.util.Collection<String> seen = new ArrayList<>();"),
            new Mutant("FastReport.java", "Comparator.comparingInt(Person::points).reversed()", "Comparator.comparingInt(Person::points)"),
            new Mutant("FastReport.java", ".reversed().thenComparing(Person::name)", ".reversed()"),
            new Mutant("FastReport.java", "day.getValue() > perDay.get(best)", "day.getValue() >= perDay.get(best)"),
            new Mutant("FastReport.java", "                invalid++;\n", ""),
            new Mutant("FastReport.java", "                duplicates++;\n", ""),
            new Mutant("FastReport.java", ".append(\"lignes : \").append(lines.size())", ".append(\"lignes : \").append(journal.events().size())"),
            new Mutant("FastReport.java", "counts.merge(e.column(), 1, Integer::sum);", "counts.merge(e.column(), 1, (a, b) -> a);"),
            new Mutant("TaskEvent.java", "\\\\d{1,4};.+", "\\\\d{1,5};.+"),
            new Mutant("TaskEvent.java", "line.substring(b + 1, c)", "line.substring(b, c)"),
            new Mutant("Bench.java", "        for (int i = 0; i < warmups; i++) {\n            sink += task.get().hashCode();\n        }\n", ""),
            new Mutant("Bench.java", "return nanos[nanos.length / 2];", "return nanos[(nanos.length - 1) / 2];"),
            new Mutant("Bench.java", "if (warmups < 0 || runs < 1) {", "if (warmups < 0 || runs < 0) {"));

    static final List<String> API_CODE = List.of(
            "record TaskEvent(", "static final Pattern", "Pattern.compile(", "final class FastReport", "HashSet",
            "StringBuilder", "final class Bench", "record Result(", "System.nanoTime()", "volatile", "Arrays.sort(",
            "final class ReportDemo", "Data.LegacyReport", "Data.generate(", "!LinkedList", "!.matches(\"", "!split(",
            "max:method=18",
            "in:FastReport.java!.contains(##aucune recherche lineaire dans une liste (contains)",
            "in:FastReport.java!= out +##le texte se construit avec StringBuilder",
            "in:FastReport.java!Legacy##la version rapide ne s'appuie pas sur le legacy");

    static final List<String> API_TESTS = List.of(
            "Data.LegacyReport.report(", "@RepeatedTest", "SplittableRandom", "assertTimeoutPreemptively(",
            "Data.generate(", "@ParameterizedTest", "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 300, MUTANTS, API_CODE, API_TESTS);
    }
}
