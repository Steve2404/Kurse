package streams.exercises;

import streams.ExerciseChecker;

import java.util.List;

/**
 * EXERCICE 12 - Combiner 2 collectors en 1 avec Collectors.teeing() (niveau : capstone, difficile)
 * ==============================================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_OptionalBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine que tu veuilles connaitre A LA FOIS le plus petit ET le plus
 * grand element d'un stream. Un stream ne se consomme qu'UNE SEULE
 * FOIS (rappel de l'Exercise05) : on ne peut donc PAS faire
 * "values.stream().min(...)" PUIS "values.stream().max(...)" sur le
 * MEME stream deja construit - il faudrait le reconstruire entierement
 * pour le second appel.
 *
 * Collectors.teeing(collectorA, collectorB, fusion) resout ca
 * elegamment : il fait tourner LES DEUX collectors EN MEME TEMPS, sur
 * UN SEUL passage du stream (chaque element est envoye aux DEUX
 * collectors), puis appelle 'fusion' UNE SEULE FOIS a la toute fin
 * pour combiner leurs 2 resultats en UN SEUL resultat final.
 *
 *
 * ==================================================================
 * TODO : minMaxSummary(values)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * values = [5, 3, 8, 1, 9, 2].
 *
 * collectorA = Collectors.minBy(ordre naturel) -> trouve 1 (dans un
 *   Optional<Integer>, car un stream VIDE n'aurait pas de minimum).
 * collectorB = Collectors.maxBy(ordre naturel) -> trouve 9.
 * fusion = (minOpt, maxOpt) -> "min=" + minOpt.get() + ", max=" + maxOpt.get().
 *
 * Resultat final : "min=1, max=9".
 *
 * -- Le plan --
 *
 *   1. Preparer a part, dans 2 variables EXPLICITEMENT typees
 *      Collector<Integer, ?, Optional<Integer>>, le collector "min" et
 *      le collector "max" (voir le piege ci-dessous pour comprendre
 *      pourquoi "a part" et pas directement en ligne).
 *   2. values.stream().collect(Collectors.teeing(minCollector,
 *      maxCollector, (min, max) -> "min=" + min.get() + ", max=" + max.get())).
 *
 * -- Piege a eviter --
 *
 * Ecrire directement les 2 appels a minBy()/maxBy() EN LIGNE, comme
 * arguments de teeing(), peut faire echouer l'inference de type du
 * compilateur (il n'arrive plus a deviner que T = Integer pour les 2
 * collectors EN MEME TEMPS que le type de retour final). Les stocker
 * d'abord dans 2 variables au type explicite lui donne l'information
 * dont il a besoin, sans ambiguite.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : ce sont juste 2 variables preparatoires suivies d'un seul
 * appel a collect() - teeing() EST la boite qui combine deja tout
 * pour toi.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Collector<Integer, ?, Optional<Integer>> minCollector =
 *         Collectors.minBy(Comparator.naturalOrder());
 *     (et pareil pour maxCollector avec Collectors.maxBy())
 *   - Le 3e argument de teeing() est un BiFunction<R1,R2,R> qui prend
 *     le resultat de collectorA PUIS celui de collectorB, dans cet
 *     ordre, et rend le resultat final combine.
 */
public class Exercise12_TeeingCollector {

    public static String minMaxSummary(List<Integer> values) {
        throw new UnsupportedOperationException("TODO : implementer minMaxSummary()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("minMaxSummary([5,3,8,1,9,2]) == 'min=1, max=9'",
                minMaxSummary(List.of(5, 3, 8, 1, 9, 2)).equals("min=1, max=9"));

        ExerciseChecker.check("minMaxSummary sur un seul element -> min == max",
                minMaxSummary(List.of(7)).equals("min=7, max=7"));

        ExerciseChecker.summary();
    }
}
