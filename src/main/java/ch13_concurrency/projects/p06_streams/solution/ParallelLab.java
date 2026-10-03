package ch13_concurrency.projects.p06_streams.solution;

import ch13_concurrency.projects.p06_streams.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * SOLUTION du projet 6 - les streams paralleles : ce qui reste juste, et ce qui ne l'est plus.
 */
public class ParallelLab {

    // Racine carree entiere exacte (Math.sqrt peut se tromper d'une unite sur les grands nombres).
    static long isqrt(long n) {
        long r = (long) Math.sqrt(n);
        while (r * r > n) {
            r--;
        }
        while ((r + 1) * (r + 1) <= n) {
            r++;
        }
        return r;
    }

    // Nombre de points entiers (x, y) avec x^2 + y^2 <= R^2 : pour chaque colonne x, 2 * isqrt(R^2 - x^2) + 1 points.
    static long latticePoints(LongStream xs, long r) {
        return xs.map(x -> 2 * isqrt(r * r - x * x) + 1).sum();
    }

    static int collatzLength(long n) {
        int steps = 1;
        while (n != 1) {
            n = n % 2 == 0 ? n / 2 : 3 * n + 1;
            steps++;
        }
        return steps;
    }

    public static void main(String[] args) {
        long r = Data.RADIUS;
        long sequential = latticePoints(LongStream.rangeClosed(-r, r), r);
        long parallel = latticePoints(LongStream.rangeClosed(-r, r).parallel(), r);
        System.out.println("points entiers du disque de rayon " + r + " : " + parallel + " (sequentiel identique " + (sequential == parallel) + "), pi ~ "
                + String.format(Locale.ROOT, "%.6f", (double) parallel / (r * r)));

        // max avec un comparateur TOTAL (longueur, puis le plus petit depart) : le resultat ne depend pas du decoupage.
        Comparator<int[]> longest = Comparator.<int[]>comparingInt(p -> p[1]).thenComparing(p -> -p[0]);
        int[] best = IntStream.range(1, Data.COLLATZ_LIMIT).parallel().mapToObj(n -> new int[] {n, collatzLength(n)}).max(longest).orElseThrow();
        System.out.println("Collatz : la plus longue suite part de " + best[0] + " (" + best[1] + " termes)");

        List<Integer> numbers = IntStream.rangeClosed(1, 100).boxed().toList();
        // L'IDENTITE doit etre neutre (0 pour +) : en parallele, elle est ajoutee une fois PAR morceau.
        int goodSeq = numbers.stream().reduce(0, Integer::sum);
        int goodPar = numbers.parallelStream().reduce(0, Integer::sum);
        int badSeq = numbers.stream().reduce(10, Integer::sum);
        int badPar = numbers.parallelStream().reduce(10, Integer::sum);
        System.out.println("reduce : identite 0 -> " + goodSeq + " / " + goodPar + " ; identite 10 -> sequentiel " + badSeq + ", parallele different " + (badPar != badSeq));

        // reduce a 3 arguments : identite, accumulateur (resultat + element), combineur (resultat + resultat).
        List<String> words = Arrays.asList(Data.TEXT.split(" "));
        int letters = words.parallelStream().reduce(0, (acc, w) -> acc + w.length(), Integer::sum);
        // collect a 3 arguments : fournisseur, accumulateur, combineur ; l'ORDRE de rencontre est conserve.
        List<String> collected = words.parallelStream().map(String::toUpperCase).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        System.out.println("lettres " + letters + " ; collect parallele dans l'ordre " + collected.equals(words.stream().map(String::toUpperCase).toList())
                + ", debut " + collected.subList(0, 4));

        // forEach parallele : ordre quelconque ; forEachOrdered : l'ordre de la source.
        List<Integer> anyOrder = Collections.synchronizedList(new ArrayList<>());
        List<Integer> ordered = Collections.synchronizedList(new ArrayList<>());
        IntStream.rangeClosed(1, 12).parallel().forEach(anyOrder::add);
        IntStream.rangeClosed(1, 12).parallel().forEachOrdered(ordered::add);
        List<Integer> sorted = new ArrayList<>(anyOrder);
        Collections.sort(sorted);
        System.out.println("forEachOrdered " + ordered + " ; forEach : memes elements " + sorted.equals(ordered) + ", taille " + anyOrder.size());

        // findFirst reste deterministe ; findAny peut rendre n'importe lequel ; unordered() libere les contraintes d'ordre.
        int first = IntStream.range(0, 1_000).parallel().filter(n -> n % 7 == 3).findFirst().orElseThrow();
        boolean any = IntStream.range(0, 1_000).parallel().filter(n -> n % 7 == 3).findAny().isPresent();
        long limited = IntStream.range(0, 1_000).parallel().unordered().limit(10).count();
        Stream<String> s = words.stream();
        boolean before = s.isParallel();
        boolean after = s.parallel().isParallel();
        System.out.println("findFirst " + first + ", findAny present " + any + ", unordered().limit(10) donne " + limited + " elements ; isParallel " + before + " puis "
                + after + ", sequential() " + words.parallelStream().sequential().isParallel());

        // Les collecteurs CONCURRENTS : une seule map partagee, remplie par tous les threads.
        ConcurrentMap<Integer, List<String>> byLength = words.parallelStream().collect(Collectors.groupingByConcurrent(String::length));
        ConcurrentMap<String, Integer> counts = words.parallelStream().collect(Collectors.toConcurrentMap(w -> w, w -> 1, Integer::sum));
        Map<Integer, Long> sizes = new TreeMap<>();
        byLength.forEach((len, ws) -> sizes.put(len, (long) ws.size()));
        Map<String, Integer> repeated = new TreeMap<>();
        counts.forEach((w, c) -> {
            if (c > 1) {
                repeated.put(w, c);
            }
        });
        System.out.println("groupingByConcurrent (longueur -> nombre) " + sizes + " ; toConcurrentMap, mots repetes " + repeated);

        // Les tableaux ont aussi leurs operations paralleles.
        int[] values = IntStream.range(0, 12).map(i -> (i * 7 + 3) % 12).toArray();
        Arrays.parallelSort(values);
        int[] prefix = IntStream.rangeClosed(1, 8).toArray();
        Arrays.parallelPrefix(prefix, Integer::sum);
        System.out.println("parallelSort " + Arrays.toString(values) + " ; parallelPrefix (sommes cumulees) " + Arrays.toString(prefix));
    }
}
