package ch10_streams.exercises;

import ch10_streams.ExerciseChecker;

import java.util.List;
import java.util.Set;

/**
 * EXERCICE 9 - Les reductions : reduce() et collect() (niveau : moyen)
 * =================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_OptionalBasics.java.
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Une REDUCTION, c'est une operation terminale qui "ecrase" TOUS les
 * elements du stream ensemble pour n'en faire plus qu'UN SEUL
 * resultat (comme le "rouleau compresseur" de l'Exercise06 du
 * chapitre lambdas). reduce(), count(), max(), min() sont des
 * reductions "classiques". collect() est un cas particulier
 * important : une reduction MUTABLE - au lieu de fabriquer un nouveau
 * resultat a chaque etape, elle GARDE le meme objet collecteur (par
 * exemple, une List qui grandit) et le modifie au fur et a mesure.
 *
 *
 * ==================================================================
 * TODO 1 : sumWithReduce(values)
 * ==================================================================
 *
 * -- Essayons a la main --
 *
 * values = [1, 2, 3, 4, 5]. reduce(0, Integer::sum) part de 0 (la
 * "valeur de depart", ce qu'on porte AVANT de croiser le premier
 * element), puis ecrase avec chaque element l'un apres l'autre :
 * 0+1=1, 1+2=3, 3+3=6, 6+4=10, 10+5=15.
 *
 * -- Le plan --
 *
 *   1. values.stream().reduce(0, Integer::sum).
 *
 *
 * ==================================================================
 * TODO 2 : joinWithCollect(words)
 * ==================================================================
 *
 * -- Le plan --
 *
 *   1. words.stream().collect(Collectors.joining(", ")).
 *
 *
 * ==================================================================
 * TODO 3 : toSetCollect(values)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Un Set ne garde jamais les doublons : collecter dans un Set, c'est
 * comme verser une poignee de billes (avec des couleurs qui se
 * repetent) dans un sac qui refuse automatiquement toute couleur
 * deja presente.
 *
 * -- Le plan --
 *
 *   1. values.stream().collect(Collectors.toSet()).
 *
 * -- Ces 3 TODO ont-ils besoin d'une boite magique separee ? --
 *
 * Non : chacun tient en une seule ligne, c'est le CHOIX du bon
 * Collector qui est le vrai coeur de l'exercice.
 *
 * Exemple a verifier : sumWithReduce([1,2,3,4,5]) == 15.
 * joinWithCollect(["a","b","c"]) == "a, b, c".
 * toSetCollect([1,2,2,3,3,3]) == {1,2,3} (3 elements, pas 6).
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Collectors.joining(", ") insere le separateur ENTRE les
 *     elements, sans en ajouter un de trop au debut ou a la fin.
 *   - Integer::sum est une method reference statique equivalente a
 *     (a, b) -> a + b.
 */
public class Exercise09_ReduceAndCollectBasics {

    public static int sumWithReduce(List<Integer> values) {
        throw new UnsupportedOperationException("TODO 1 : implementer sumWithReduce()");
    }

    public static String joinWithCollect(List<String> words) {
        throw new UnsupportedOperationException("TODO 2 : implementer joinWithCollect()");
    }

    public static Set<Integer> toSetCollect(List<Integer> values) {
        throw new UnsupportedOperationException("TODO 3 : implementer toSetCollect()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("sumWithReduce([1,2,3,4,5]) == 15", sumWithReduce(List.of(1, 2, 3, 4, 5)) == 15);

        ExerciseChecker.check("joinWithCollect([a,b,c]) == 'a, b, c'",
                joinWithCollect(List.of("a", "b", "c")).equals("a, b, c"));

        Set<Integer> result = toSetCollect(List.of(1, 2, 2, 3, 3, 3));
        ExerciseChecker.check("toSetCollect elimine les doublons -> {1,2,3}",
                result.equals(Set.of(1, 2, 3)) && result.size() == 3);

        ExerciseChecker.summary();
    }
}
