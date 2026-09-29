package ch10_streams.solutions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Corrige de l'exercice 17. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch10_streams.exercises.Exercise17_ReduceAdvanced.
 */
public class Solution17_ReduceAdvanced {

    public static Optional<String> longestWord(List<String> words) {
        // reduce sans identity : pas de "mot neutre", donc resultat en boite.
        // > strict : a egalite, le tenant du titre (a) garde sa place.
        return words.stream().reduce((a, b) -> b.length() > a.length() ? b : a);
    }

    public static int totalLength(List<String> words) {
        // Resultat Integer, elements String : il faut la forme a 3 arguments.
        // Le combiner (Integer::sum) sert a fusionner les resultats partiels en parallele.
        return words.stream().reduce(0, (total, w) -> total + w.length(), Integer::sum);
    }

    public static String concatWithCollect(List<String> words) {
        // Reduction MUTABLE : on remplit UN SEUL StringBuilder au lieu de creer une String par etape.
        return words.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    public static Map<String, Integer> countOccurrences(List<String> words) {
        // Le combiner doit ADDITIONNER les cases (merge) : putAll ecraserait les compteurs.
        return words.stream().collect(
                HashMap::new,
                (m, w) -> m.merge(w, 1, Integer::sum),
                (m1, m2) -> m2.forEach((k, v) -> m1.merge(k, v, Integer::sum)));
    }

    public static Optional<Integer> maxWithReduce(List<Integer> values) {
        // Integer::max est deja un BinaryOperator<Integer>.
        return values.stream().reduce(Integer::max);
    }

    public static String joinWithReduce(List<String> words, String separator) {
        // "" n'est PAS neutre pour a + separator + b (on obtiendrait "-x-y-z") :
        // forme sans identity, puis orElse("") pour la liste vide.
        return words.stream().reduce((a, b) -> a + separator + b).orElse("");
    }
}
