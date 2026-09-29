package ch10_streams.solutions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Corrige de l'exercice 4. A ne consulter qu'apres avoir essaye par
 * vous-meme dans streams.exercises.Exercise04_PipelineLaziness.
 */
public class Solution04_PipelineLaziness {

    public static Stream<Integer> filterEvenThenTimesTen(List<Integer> values, int[] filterCalls, int[] mapCalls) {
        // On rend le Stream SANS operation terminale : a ce stade filter et map n'ont
        // encore rien execute (les compteurs restent a 0). C'est la paresse.
        return values.stream()
                .filter(n -> {
                    filterCalls[0]++;
                    return n % 2 == 0;
                })
                .map(n -> {
                    mapCalls[0]++;
                    return n * 10;
                });
    }

    public static Optional<Integer> firstEvenTimesTen(List<Integer> values, int[] filterCalls) {
        // findFirst est une operation "court-circuit" : des qu'un element passe, le pipeline
        // s'arrete, donc filter n'est appele que sur le debut de la liste.
        return values.stream()
                .filter(n -> {
                    filterCalls[0]++;
                    return n % 2 == 0;
                })
                .map(n -> n * 10)
                .findFirst();
    }
}
