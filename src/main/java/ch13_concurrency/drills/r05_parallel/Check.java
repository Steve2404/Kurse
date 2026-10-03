package ch13_concurrency.drills.r05_parallel;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : drill de rappel 5 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON Recall05, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "D01 : false true false",
            "D02 : 55 true 385",
            "D03 : [1, 2, 3, 4, 5, 6, 7, 8, 9, 10] [10, 20, 30, 40, 50, 60, 70, 80, 90, 100]",
            "D04 : 5 true 8",
            "D05 : ConcurrentHashMap {false=5, true=5} {1=n1, 2=n4, 3=n9, 4=n16}",
            "D06 : b,a,d,c [a, b, c, d] [B, A, D, C]");
            // EXPECTED-END

    static final List<String> API = List.of(
            ".isParallel()", ".parallelStream()", ".parallel()", ".sequential()",
            "reduce(5, Integer::sum)", "reduce(0, (acc, n) -> acc + n * n, Integer::sum)", ".forEachOrdered(", ".findFirst()",
            ".findAny()", ".unordered()", "Collectors.groupingByConcurrent(", "Collectors.toConcurrentMap(",
            "Collectors.joining(",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "Recall05", args, EXPECTED, API);
    }
}
