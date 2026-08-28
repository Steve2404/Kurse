package ch10_streams.solutions;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise01_OptionalBasics.
 */
public class Solution01_OptionalBasics {

    public static Optional<Integer> safeDivide(int a, int b) {
        if (b == 0) {
            return Optional.empty();
        }
        return Optional.of(a / b);
    }

    public static String describeSafely(Optional<Integer> opt) {
        if (opt.isPresent()) {
            return "Resultat : " + opt.get();
        }
        return "Pas de resultat";
    }

    public static int valueOrFallback(Optional<Integer> opt, Supplier<Integer> fallback) {
        return opt.orElseGet(fallback);
    }

    public static int requireValue(Optional<Integer> opt) {
        return opt.orElseThrow(() -> new IllegalStateException("Aucune valeur presente"));
    }

    public static void logIfPresent(Optional<Integer> opt, List<String> sink) {
        opt.ifPresent(v -> sink.add("Valeur presente : " + v));
    }
}
