package collections.exercises;

import collections.ExerciseChecker;

import java.util.Arrays;
import java.util.List;

/**
 * EXERCICE 13 - Maximum glissant avec une Deque (niveau : difficile)
 * ============================================================================
 *
 * Rappel express du decoupage en "boites magiques" : voir
 * Exercise01_ListAlgorithms.java.
 *
 *
 * ==================================================================
 * TODO : maxSlidingWindow(nums, k)
 * ==================================================================
 *
 * -- Le probleme, explique comme a un tout petit enfant --
 *
 * Imagine des blocs de tailles differentes qui defilent devant toi sur
 * un tapis roulant, un par un. Tu as une petite fenetre qui ne montre
 * que les k DERNIERS blocs passes (les autres, plus anciens, ne sont
 * plus visibles). A chaque nouveau bloc qui arrive dans la fenetre, tu
 * dois annoncer : "le plus grand bloc actuellement visible dans ma
 * fenetre, c'est celui-la !"
 *
 * -- Essayons a la main --
 *
 * Blocs : 1, 3, -1, -3, 5, 3, 6, 7. Taille de fenetre k=3.
 *
 * Fenetre [1, 3, -1] -> le plus grand est 3.
 * Fenetre [3, -1, -3] -> le plus grand est 3.
 * Fenetre [-1, -3, 5] -> le plus grand est 5.
 * Fenetre [-3, 5, 3] -> le plus grand est 5.
 * Fenetre [5, 3, 6] -> le plus grand est 6.
 * Fenetre [3, 6, 7] -> le plus grand est 7.
 *
 * Resultat : [3, 3, 5, 5, 6, 7].
 *
 * -- Ce qu'on remarque --
 *
 * Methode naive : a chaque nouvelle fenetre, reregarder les k blocs un
 * par un pour trouver le plus grand. Ca marche, mais c'est refaire le
 * meme travail en boucle pour rien. Astuce d'un enfant malin : des
 * qu'un NOUVEAU bloc plus grand arrive, tous les blocs plus PETITS qui
 * trainent devant lui (donc plus anciens, encore dans la fenetre) ne
 * POURRONT PLUS JAMAIS etre le plus grand tant que ce nouveau bloc est
 * encore visible - ils sont definitivement "grilles", autant les
 * oublier tout de suite. Ne garder que les blocs qui ont encore une
 * CHANCE d'etre un jour le plus grand de leur fenetre, ranges du plus
 * grand (devant) au plus petit (derriere), suffit.
 *
 * Refais l'exemple a la main en notant, a chaque bloc, la liste des
 * "blocs encore en course" (du plus grand au plus petit) : tu verras
 * qu'elle ne grandit jamais beaucoup, car les petits sont elimines des
 * qu'un plus grand arrive derriere eux.
 *
 * -- Le plan --
 *
 *   1. Preparer une Deque qui contiendra des INDICES de blocs (pas les
 *      valeurs elles-memes), toujours ranges du plus grand (devant, en
 *      tete) au plus petit (derriere, en queue).
 *   2. Pour chaque nouveau bloc (indice i) : d'abord, retirer de la
 *      queue de la Deque tous les indices dont la valeur est <= a la
 *      valeur du nouveau bloc (ils viennent d'etre "grilles" pour de
 *      bon).
 *   3. Ajouter l'indice i en queue de la Deque.
 *   4. Si l'indice en tete de la Deque est sorti de la fenetre actuelle
 *      (trop ancien, il a plus de k places de retard), le retirer de la
 *      tete.
 *   5. Des que la fenetre a atteint sa taille complete (i >= k-1), la
 *      valeur du bloc dont l'indice est en TETE de la Deque est le
 *      maximum de la fenetre courante : la noter dans le resultat.
 *
 * -- Ce plan a-t-il besoin d'une boite magique separee ? --
 *
 * Non : les 5 etapes forment UN SEUL parcours de tableau qui avance
 * indice par indice ; les separer casserait la logique. Elles restent
 * ensemble dans maxSlidingWindow().
 *
 * Exemple a verifier : maxSlidingWindow([1,3,-1,-3,5,3,6,7], 3) ==
 * [3,3,5,5,6,7]
 *
 *
 * Indices techniques Java (a lire seulement si le plan a la main est
 * clair mais que la traduction en code bloque) :
 *
 *   - Deque<Integer> deque = new ArrayDeque<>(); (contient des indices,
 *     pas des valeurs)
 *   - "retirer de la queue tant que <= " :
 *       while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
 *           deque.pollLast();
 *       }
 *   - Ajouter l'indice courant : deque.offerLast(i);
 *   - "sorti de la fenetre" : deque.peekFirst() <= i - k -> deque.pollFirst();
 *   - Des que i >= k - 1 : result.add(nums[deque.peekFirst()]);
 *   - Piege classique : bien retirer les indices hors-fenetre APRES
 *     avoir ajoute le nouvel indice, mais AVANT de lire le maximum de
 *     la fenetre courante.
 */
public class Exercise13_SlidingWindowMaximum {

    public static List<Integer> maxSlidingWindow(int[] nums, int k) {
        throw new UnsupportedOperationException("TODO : implementer maxSlidingWindow()");
    }

    public static void main(String[] args) {
        int[] nums = {1, 3, -1, -3, 5, 3, 6, 7};
        List<Integer> result = maxSlidingWindow(nums, 3);
        ExerciseChecker.check("maxSlidingWindow([1,3,-1,-3,5,3,6,7], 3) == [3,3,5,5,6,7]",
                result.equals(Arrays.asList(3, 3, 5, 5, 6, 7)));

        int[] decreasing = {9, 8, 7, 6};
        ExerciseChecker.check("fenetre sur une sequence decroissante [9,8,7,6], k=2 -> [9,8,7]",
                maxSlidingWindow(decreasing, 2).equals(Arrays.asList(9, 8, 7)));

        int[] single = {5};
        ExerciseChecker.check("k == taille du tableau -> un seul maximum",
                maxSlidingWindow(single, 1).equals(Arrays.asList(5)));

        ExerciseChecker.summary();
    }
}
