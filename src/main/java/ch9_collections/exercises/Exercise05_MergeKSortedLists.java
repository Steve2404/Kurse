package ch9_collections.exercises;

import ch9_collections.ExerciseChecker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * EXERCICE 5 - Fusionner k listes triees avec une PriorityQueue (niveau : difficile)
 * ====================================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 *
 * ==================================================================
 * TODO : mergeKSortedLists(lists)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine plusieurs files d'enfants a la cantine, et dans CHAQUE
 * file, les enfants sont deja ranges du plus petit au plus grand.
 * Mais il y a plusieurs files differentes (une par classe). Le
 * directeur veut UNE SEULE grande file, avec TOUS les enfants de
 * toutes les classes, toujours ranges du plus petit au plus grand.
 *
 * -- Essayons a la main --
 *
 * File A (deja triee) : 1m, 4m, 7m (des tailles imaginaires). File B :
 * 2m, 3m. File C : 0m, 9m, 10m.
 *
 * Comment ferais-tu, TOI, sans ordinateur, pour fabriquer la grande
 * file ? Voila l'astuce d'un enfant malin : il n'a pas besoin de
 * regarder TOUT LE MONDE a chaque fois. Il regarde SEULEMENT le
 * premier enfant de chaque file (puisque chaque file est deja triee,
 * le plus petit de la file est forcement devant). Il compare ces 3
 * "premiers de file" entre eux, prend le plus petit des 3, le met
 * dans la grande file, et le remplace par le nouveau premier de SA
 * file d'origine. Puis il recommence.
 *
 * Fais-le vraiment sur les 3 files ci-dessus, sur une feuille, en
 * barrant a chaque fois l'enfant qui part dans la grande file.
 *
 * -- Ce qu'on remarque --
 *
 * A chaque instant, on n'a jamais besoin de regarder que le "premier
 * visible" de chaque file (3 candidats ici, pas 8 enfants). C'est
 * exactement le travail d'une structure qui te redonne toujours "le
 * plus petit parmi ce qu'elle contient actuellement", sans jamais
 * avoir a tout retrier : ca s'appelle une file de priorite
 * (PriorityQueue). Le "type de probleme" ici, c'est "j'ai besoin de
 * toujours piocher le plus petit/le plus urgent parmi plusieurs
 * candidats qui changent au fil du temps".
 *
 * -- Le plan --
 *
 *   1. Mettre le "premier enfant" de chaque file (non vide) dans la
 *      PriorityQueue, avec le moyen de savoir "de quelle file il
 *      vient" et "a quelle position il etait dans cette file", pour
 *      pouvoir retrouver son remplacant plus tard.
 *   2. Tant que la PriorityQueue n'est pas vide :
 *      a. En sortir le plus petit.
 *      b. L'ajouter a la grande file resultat.
 *      c. Si sa file d'origine a un enfant suivant, le mettre a son
 *         tour dans la PriorityQueue.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Remarque un detail important de l'etape 1 : on ne met pas juste
 * "une valeur" dans la PriorityQueue, on doit aussi savoir "de quelle
 * file" et "a quelle position" pour pouvoir continuer a avancer dans
 * cette file plus tard. Se raconte-t-il tout seul (Q1) : "un curseur
 * qui pointe sur une position precise dans une liste precise, et qui
 * sait donner sa valeur et le curseur suivant" ? Oui, totalement,
 * meme sans parler de fusion de listes. Cache-t-il sa propre petite
 * recette (Q3) : "calculer la position suivante, verifier qu'on n'a
 * pas depasse la fin de sa file" ? Oui. Verdict : ce "curseur" merite
 * sa propre petite classe/boite a part, distincte de la boucle
 * principale qui orchestre tout.
 *
 * Exemple a verifier : [[1,4,7],[2,3],[0,9,10]] -> [0,1,2,3,4,7,9,10]
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Une petite classe interne "Cursor" avec (listIndex,
 *     elementIndex) et une methode value() qui lit dans la liste
 *     d'origine est plus simple a manipuler qu'un tableau d'int.
 *   - PriorityQueue<Cursor> avec un
 *     Comparator.comparingInt(c -> c.value()).
 *   - Pensez a ne jamais inserer les sous-listes vides des le depart.
 *   - Ne mettez PAS tous les elements dans la PriorityQueue d'un coup
 *     (ca reviendrait a ignorer que les listes sont deja triees) :
 *     l'interet de l'approche "curseur par liste" est une complexite
 *     O(n log k) au lieu de O(n log n).
 */
public class Exercise05_MergeKSortedLists {

    public static List<Integer> mergeKSortedLists(List<List<Integer>> lists) {
        throw new UnsupportedOperationException("TODO : implementer mergeKSortedLists()");
    }

    public static void main(String[] args) {
        List<List<Integer>> input = Arrays.asList(
                Arrays.asList(1, 4, 7),
                Arrays.asList(2, 3),
                Arrays.asList(0, 9, 10)
        );

        List<Integer> merged = mergeKSortedLists(input);
        ExerciseChecker.check("Fusion de 3 listes triees donne [0,1,2,3,4,7,9,10]",
                merged.equals(Arrays.asList(0, 1, 2, 3, 4, 7, 9, 10)));

        List<List<Integer>> withEmpty = Arrays.asList(
                new ArrayList<>(),
                Arrays.asList(5),
                new ArrayList<>()
        );
        ExerciseChecker.check("Fusion avec des listes vides gere le cas sans erreur",
                mergeKSortedLists(withEmpty).equals(Arrays.asList(5)));

        List<List<Integer>> allEmpty = Arrays.asList(new ArrayList<>(), new ArrayList<>());
        ExerciseChecker.check("Fusion de listes toutes vides retourne une liste vide",
                mergeKSortedLists(allEmpty).isEmpty());

        ExerciseChecker.summary();
    }
}