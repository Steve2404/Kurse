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
        // Division par zero impossible : on rend une boite VIDE plutot que de planter.
        // Optional.of est sur ici, car a / b n'est jamais null.
        if (b == 0) {
            return Optional.empty();
        }
        return Optional.of(a / b);
    }

    public static String describeSafely(Optional<Integer> opt) {
        // Style "classique" : on DEMANDE (isPresent) avant d'OUVRIR (get),
        // car get() sur une boite vide lance NoSuchElementException.
        if (opt.isPresent()) {
            return "Resultat : " + opt.get();
        }
        return "Pas de resultat";
    }

    public static int valueOrFallback(Optional<Integer> opt, Supplier<Integer> fallback) {
        // orElseGet est PARESSEUX : fallback.get() n'est appele que si la boite est vide.
        // (orElse(fallback.get()) l'appellerait TOUJOURS.)
        return opt.orElseGet(fallback);
    }

    public static int requireValue(Optional<Integer> opt) {
        // Le Supplier fabrique l'exception ; il n'est appele (et l'exception lancee) que si la boite est vide.
        return opt.orElseThrow(() -> new IllegalStateException("Aucune valeur presente"));
    }

    public static void logIfPresent(Optional<Integer> opt, List<String> sink) {
        // ifPresent n'a pas de "sinon" : rien ne se passe du tout pour une boite vide.
        opt.ifPresent(v -> sink.add("Valeur presente : " + v));
    }
}
