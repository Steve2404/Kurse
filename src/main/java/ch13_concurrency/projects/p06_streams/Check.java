package ch13_concurrency.projects.p06_streams;

import projectkit.ProjectChecker;

import java.util.List;

/**
 * Le correcteur : projet 6 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON ParallelLab, ou avec l'argument "solution".
 */
public class Check {

    static final List<String> EXPECTED = List.of(
            // EXPECTED-BEGIN
            "points entiers du disque de rayon 20000 : 1256636857 (sequentiel identique true), pi ~ 3.141592",
            "Collatz : la plus longue suite part de 837799 (525 termes)",
            "reduce : identite 0 -> 5050 / 5050 ; identite 10 -> sequentiel 5060, parallele different true",
            "lettres 178 ; collect parallele dans l'ordre true, debut [LE, PARALLELE, NE, GARANTIT]",
            "forEachOrdered [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12] ; forEach : memes elements true, taille 12",
            "findFirst 3, findAny present true, unordered().limit(10) donne 10 elements ; isParallel false puis true, sequential() false",
            "groupingByConcurrent (longueur -> nombre) {1=2, 2=7, 3=4, 4=3, 5=2, 6=2, 7=4, 8=4, 9=5, 11=1} ; toConcurrentMap, mots repetes {le=3, parallele=2}",
            "parallelSort [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11] ; parallelPrefix (sommes cumulees) [1, 3, 6, 10, 15, 21, 28, 36]");
            // EXPECTED-END

    static final List<String> API = List.of(
            "Data.RADIUS", "Data.COLLATZ_LIMIT", "Data.TEXT", ".parallel()",
            ".parallelStream()", ".isParallel()", ".sequential()", ".max(",
            ".thenComparing(", "reduce(0, Integer::sum)", "reduce(10, Integer::sum)", "reduce(0, (acc, w) -> acc + w.length(), Integer::sum)",
            ".collect(ArrayList::new, ArrayList::add, ArrayList::addAll)", ".forEach(", ".forEachOrdered(", ".findFirst()",
            ".findAny()", ".unordered()", "Collectors.groupingByConcurrent(", "Collectors.toConcurrentMap(",
            "ConcurrentMap<", "Arrays.parallelSort(", "Arrays.parallelPrefix(", "String.format(Locale.ROOT",
            // Crescendo : notions des chapitres 14 et 15 (E/S de fichiers, JDBC) ou System.exit / printStackTrace, interdites au chapitre 13.
            "!Files.", "!Path.of", "!Paths.", "!new File(", "!FileReader", "!FileWriter", "!BufferedReader", "!BufferedWriter",
            "!InputStream", "!OutputStream", "!Scanner", "!DriverManager", "!java.sql", "!System.exit", "!printStackTrace", "!.now()",
            "!.stop()");

    public static void main(String[] args) throws Exception {
        ProjectChecker.check(Check.class, "ParallelLab", args, EXPECTED, API);
    }
}
