package ch10_streams.solutions;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.exercises.Exercise06_LazyPipelineEngineering.
 */
public class Solution06_LazyPipelineEngineering {

    public static boolean isPrime(int n) {
        // noneMatch s'arrete au 1er diviseur trouve. On ne teste que jusqu'a la racine carree.
        if (n < 2) {
            return false;
        }
        return IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(d -> n % d == 0);
    }

    public static List<Integer> firstNPrimes(int n) {
        // Suite infinie + filter + limit : grace a la paresse, on ne teste que les nombres necessaires.
        // boxed() pour passer de IntStream a Stream<Integer> avant toList().
        return IntStream.iterate(2, x -> x + 1)
                .filter(Solution06_LazyPipelineEngineering::isPrime)
                .limit(n)
                .boxed()
                .toList();
    }

    public static int firstPrimeAbove(int threshold, int[] tested) {
        // tested est un TABLEAU : tested[0]++ modifie une case, pas la variable (autorise dans une lambda).
        return IntStream.iterate(threshold + 1, x -> x + 1)
                .filter(x -> {
                    tested[0]++;
                    return isPrime(x);
                })
                .findFirst()
                .getAsInt();
    }

    public static Optional<String> firstLongUppercase(List<String> words, int minLength, List<String> trace) {
        // Un element descend TOUT le pipeline avant que le suivant commence : la trace
        // alterne f: et m:, et findFirst arrete tout apres le 1er resultat.
        return words.stream()
                .filter(w -> {
                    trace.add("f:" + w);
                    return w.length() >= minLength;
                })
                .map(w -> {
                    trace.add("m:" + w);
                    return w.toUpperCase();
                })
                .findFirst();
    }

    public static List<Integer> smallestKWithTrace(List<Integer> values, int k, List<String> trace) {
        // sorted() est une barriere : il doit voir TOUS les elements (tous les "in:") avant
        // d'en laisser sortir un seul ; limit(k) coupe ensuite apres k "out:".
        return values.stream()
                .peek(x -> trace.add("in:" + x))
                .sorted()
                .peek(x -> trace.add("out:" + x))
                .limit(k)
                .toList();
    }

    public static List<Integer> distinctFromCycle(int cycleLength, int wanted) {
        // Il n'existe que cycleLength valeurs distinctes : en demander plus ferait tourner
        // distinct a l'infini. Math.min garantit que le pipeline se termine.
        return Stream.iterate(1, x -> x % cycleLength + 1)
                .distinct()
                .limit(Math.min(wanted, cycleLength))
                .toList();
    }

    public static Optional<String> firstRepeated(List<String> words, List<String> visited) {
        // Set.add rend false si l'element y etait deja : c'est notre detecteur de doublon.
        // Lambda "a etat" : acceptable en sequentiel, a eviter en parallele.
        Set<String> seen = new HashSet<>();
        return words.stream()
                .filter(w -> {
                    visited.add(w);
                    return !seen.add(w);
                })
                .findFirst();
    }
}
