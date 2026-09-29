package ch10_streams.solutions;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 5. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.exercises.Exercise05_StreamSourcesInfinite.
 */
public class Solution05_StreamSourcesInfinite {

    public static List<Long> powersOfTwo(int n) {
        // iterate a 2 arguments est INFINI : limit(n) coupe le robinet. En long car ca grandit vite.
        return Stream.iterate(1L, x -> x * 2).limit(n).toList();
    }

    public static List<Integer> collatz(int start) {
        // iterate a 3 arguments (graine, condition, suivant) s'arrete AVANT d'emettre 1,
        // comme une boucle for : on recolle le 1 final avec Stream.concat.
        return Stream.concat(
                Stream.iterate(start, x -> x != 1, x -> x % 2 == 0 ? x / 2 : 3 * x + 1),
                Stream.of(1)).toList();
    }

    public static List<String> generateIds(String prefix, int count) {
        // Une lambda ne peut pas modifier un int local : AtomicInteger est un objet dont on
        // change le CONTENU, la variable elle-meme reste effectivement finale.
        AtomicInteger counter = new AtomicInteger();
        return Stream.generate(() -> prefix + counter.incrementAndGet()).limit(count).toList();
    }

    public static List<String> headerLines(List<String> lines) {
        // takeWhile s'arrete DEFINITIVEMENT au 1er element qui ne correspond pas (different de filter).
        return lines.stream().takeWhile(l -> l.startsWith("#")).toList();
    }

    public static List<String> bodyLines(List<String> lines) {
        // dropWhile jette le debut tant que la condition est vraie, puis garde TOUT le reste.
        return lines.stream().dropWhile(l -> l.startsWith("#")).toList();
    }

    public static String shortestAndLongest(Supplier<Stream<String>> source) {
        // Un stream ne se consomme qu'UNE fois : on demande un stream NEUF au Supplier
        // pour chaque question (sinon IllegalStateException).
        Comparator<String> byLength = Comparator.comparingInt(String::length);
        String shortest = source.get().min(byLength).orElse("?");
        String longest = source.get().max(byLength).orElse("?");
        return "court=" + shortest + ";long=" + longest;
    }
}
