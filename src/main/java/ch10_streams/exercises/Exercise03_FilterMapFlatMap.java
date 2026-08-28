package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.List;

/**
 * EXERCICE 3 - filter(), map() et flatMap() (niveau : moyen)
 * ======================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_OptionalBasics.java.
 *
 * -- Les 3 outils du jour, en une phrase chacun --
 *
 *   - filter(Predicate<T>)   : garde seulement les elements qui
 *                              passent le test, EN GARDANT le meme
 *                              type.
 *   - map(Function<T,R>)     : transforme CHAQUE element en un AUTRE,
 *                              un-a-un (autant d'elements en sortie
 *                              qu'en entree).
 *   - flatMap(Function<T,Stream<R>>) : chaque element devient TOUT UN
 *                              petit stream, et flatMap les APLATIT
 *                              tous en un seul grand stream (les
 *                              streams vides disparaissent tout
 *                              simplement, sans laisser de trou).
 *
 *
 * ==================================================================
 * TODO 1 : longUppercaseWords(words, minLength)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * words = ["cat", "elephant", "dog", "hippopotamus", "ox"],
 * minLength = 4.
 *
 * filter (longueur >= 4) garde : "elephant", "hippopotamus". map
 * (toUpperCase) donne : "ELEPHANT", "HIPPOPOTAMUS".
 *
 * -- Le plan --
 *
 *   1. words.stream().filter(mot dont la longueur >= minLength)
 *      .map(String::toUpperCase).collect(Collectors.toList()).
 *
 *
 * ==================================================================
 * TODO 2 : flattenAndDouble(nestedLists)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine plusieurs petits paniers de billes, poses les uns a cote des
 * autres (une List<List<Integer>>, un panier par ligne). flatMap videt
 * TOUS les paniers dans un seul grand tas, dans l'ordre - les paniers
 * VIDES ne posent aucun probleme, ils ne laissent juste rien dans le
 * tas final.
 *
 * -- Essayons a la main --
 *
 * nestedLists = [[1,2], [], [3,4,5], [6]].
 *
 * flatMap aplatit en : [1, 2, 3, 4, 5, 6] (le panier vide n'apparait
 * simplement pas). map (x2) donne ensuite : [2, 4, 6, 8, 10, 12].
 *
 * -- Le plan --
 *
 *   1. nestedLists.stream().flatMap(panier -> panier.stream())
 *      .map(n -> n * 2).collect(Collectors.toList()).
 *
 * -- Ces 2 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une seule chaine d'appels, pas besoin de
 * decouper davantage.
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - filter(mot -> mot.length() >= minLength)
 *   - flatMap(List::stream) marche aussi (method reference sur
 *     l'instance panier, equivalent a panier -> panier.stream()).
 */
public class Exercise03_FilterMapFlatMap {

    public static List<String> longUppercaseWords(List<String> words, int minLength) {
        throw new UnsupportedOperationException("TODO 1 : implementer longUppercaseWords()");
    }

    public static List<Integer> flattenAndDouble(List<List<Integer>> nestedLists) {
        throw new UnsupportedOperationException("TODO 2 : implementer flattenAndDouble()");
    }

    public static void main(String[] args) {
        List<String> words = List.of("cat", "elephant", "dog", "hippopotamus", "ox");
        ExerciseChecker.check("longUppercaseWords(4) == [ELEPHANT, HIPPOPOTAMUS]",
                longUppercaseWords(words, 4).equals(List.of("ELEPHANT", "HIPPOPOTAMUS")));

        List<List<Integer>> nestedLists = List.of(List.of(1, 2), List.of(), List.of(3, 4, 5), List.of(6));
        ExerciseChecker.check("flattenAndDouble aplatit puis double",
                flattenAndDouble(nestedLists).equals(List.of(2, 4, 6, 8, 10, 12)));

        ExerciseChecker.summary();
    }
}
