package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.List;
import java.util.Map;

/**
 * EXERCICE 11 - Partitionner avec Collectors.partitioningBy() (niveau : moyen/difficile)
 * ====================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_OptionalBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * partitioningBy(Predicate) ressemble a groupingBy(), mais avec UNE
 * regle stricte en plus : la cle n'est JAMAIS autre chose que true ou
 * false (un Predicate ne peut renvoyer que ca). Consequence
 * importante : la Map resultat a TOUJOURS EXACTEMENT 2 cles (true ET
 * false), MEME si aucun element ne matche l'une des deux - dans ce
 * cas, cette cle pointe vers une liste VIDE, elle ne disparait
 * jamais.
 *
 *
 * ==================================================================
 * TODO : partitionEvenOdd(values)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * values = [1, 2, 3, 4, 5, 6].
 *
 * Predicate : "est pair" (n % 2 == 0).
 *
 * Resultat : {false=[1, 3, 5], true=[2, 4, 6]}.
 *
 * -- Le plan --
 *
 *   1. values.stream().collect(Collectors.partitioningBy(n -> n % 2 == 0)).
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : une seule ligne suffit.
 *
 * Exemple a verifier : sur values = [1, 3, 5] (aucun nombre pair), le
 * resultat contient QUAND MEME la cle 'true', mais avec une liste
 * VIDE - pas d'absence de cle.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - map.containsKey(true) est TOUJOURS vrai avec partitioningBy(),
 *     contrairement a groupingBy() ou une cle peut carrement etre
 *     absente si personne ne l'a jamais atteinte.
 */
public class Exercise11_PartitioningBy {

    public static Map<Boolean, List<Integer>> partitionEvenOdd(List<Integer> values) {
        throw new UnsupportedOperationException("TODO : implementer partitionEvenOdd()");
    }

    public static void main(String[] args) {
        Map<Boolean, List<Integer>> result = partitionEvenOdd(List.of(1, 2, 3, 4, 5, 6));
        ExerciseChecker.check("partitionEvenOdd : false -> [1, 3, 5]", result.get(false).equals(List.of(1, 3, 5)));
        ExerciseChecker.check("partitionEvenOdd : true -> [2, 4, 6]", result.get(true).equals(List.of(2, 4, 6)));

        Map<Boolean, List<Integer>> onlyOdd = partitionEvenOdd(List.of(1, 3, 5));
        ExerciseChecker.check("partitionEvenOdd : la cle 'true' existe TOUJOURS, meme vide",
                onlyOdd.containsKey(true) && onlyOdd.get(true).isEmpty());

        ExerciseChecker.summary();
    }
}
