package ch9_collections.drills.exercises;

import ch9_collections.ExerciseChecker;

import java.util.List;

/**
 * DRILL 02 - Queue, Deque, pile et PriorityQueue
 * ==============================================
 *
 * Mode d'emploi : voir Drill01_ListAndSet. Donnees : Pantry.NUMBERS = [5, 3, 8, 1, 9, 2]
 * et Pantry.FRUITS.
 *
 *
 * -- Les TODO (methode visee entre crochets) --
 *
 * TODO 1  : fifo()             [offer / poll] mettre NUMBERS dans une ArrayDeque en file, en retirer 2 -> [5, 3].
 * TODO 2  : lifo()             [push / pop] NUMBERS en pile, en retirer 2 -> [2, 9].
 * TODO 3  : peekEmpty()        [peek sur vide] String.valueOf(new ArrayDeque<Integer>().peek()) -> "null".
 * TODO 4  : popEmpty()         [pop sur vide] le nom simple de l'exception -> "NoSuchElementException".
 * TODO 5  : smallestThree()    [PriorityQueue] les 3 plus petits de NUMBERS, dans l'ordre de sortie -> [1, 2, 3].
 * TODO 6  : largestThree()     [PriorityQueue + Comparator.reverseOrder()] -> [9, 8, 5].
 * TODO 7  : bothEnds()         [pollFirst / pollLast] NUMBERS dans une Deque : [premier, dernier] -> [5, 2].
 * TODO 8  : backwards()        [descendingIterator] NUMBERS parcourus de la fin -> [2, 9, 1, 8, 3, 5].
 * TODO 9  : fourInserts()      [offerLast / offerFirst / addFirst] offerLast b, offerFirst a, offerLast c, addFirst z -> [z, a, b, c].
 * TODO 10 : shortestFruits()   [PriorityQueue avec Comparator] FRUITS par longueur puis alphabet ; 3 premiers sortis -> [kiwi, kiwi, pomme].
 *
 *
 * ---------------------------------------------------------------------
 * CARTE MEMOIRE (a ne lire qu'en cas de blocage, puis a cacher) :
 *
 *                 exception (grincheux)     valeur speciale (poli)
 *   ajouter       add / addFirst / addLast  offer / offerFirst / offerLast (rend false)
 *   retirer       remove / removeFirst ...  poll / pollFirst / pollLast (rend null)
 *   regarder      element / getFirst ...    peek / peekFirst / peekLast (rend null)
 *
 *   File : offer (fin) + poll (debut)       Pile : push + pop + peek (tous au DEBUT)
 *   pop et element sur vide -> NoSuchElementException
 *   ArrayDeque refuse null (NullPointerException) ; LinkedList l'accepte
 *   PriorityQueue : poll rend le plus PETIT ; new PriorityQueue<>(Comparator.reverseOrder()) -> le plus grand
 *   Attention : afficher une PriorityQueue ne montre PAS l'ordre trie ; seul poll() l'est
 *   descendingIterator() : parcours de la fin vers le debut
 * ---------------------------------------------------------------------
 */
public class Drill02_QueueAndDeque {

    public static List<Integer> fifo() {
        throw new UnsupportedOperationException("TODO 1 : implementer fifo()");
    }

    public static List<Integer> lifo() {
        throw new UnsupportedOperationException("TODO 2 : implementer lifo()");
    }

    public static String peekEmpty() {
        throw new UnsupportedOperationException("TODO 3 : implementer peekEmpty()");
    }

    public static String popEmpty() {
        throw new UnsupportedOperationException("TODO 4 : implementer popEmpty()");
    }

    public static List<Integer> smallestThree() {
        throw new UnsupportedOperationException("TODO 5 : implementer smallestThree()");
    }

    public static List<Integer> largestThree() {
        throw new UnsupportedOperationException("TODO 6 : implementer largestThree()");
    }

    public static List<Integer> bothEnds() {
        throw new UnsupportedOperationException("TODO 7 : implementer bothEnds()");
    }

    public static List<Integer> backwards() {
        throw new UnsupportedOperationException("TODO 8 : implementer backwards()");
    }

    public static List<String> fourInserts() {
        throw new UnsupportedOperationException("TODO 9 : implementer fourInserts()");
    }

    public static List<String> shortestFruits() {
        throw new UnsupportedOperationException("TODO 10 : implementer shortestFruits()");
    }

    public static void main(String[] args) {
        ExerciseChecker.check("1  fifo == [5, 3]", fifo().equals(List.of(5, 3)));
        ExerciseChecker.check("2  lifo == [2, 9]", lifo().equals(List.of(2, 9)));
        ExerciseChecker.check("3  peekEmpty == null", peekEmpty().equals("null"));
        ExerciseChecker.check("4  popEmpty == NoSuchElementException", popEmpty().equals("NoSuchElementException"));
        ExerciseChecker.check("5  smallestThree == [1, 2, 3]", smallestThree().equals(List.of(1, 2, 3)));
        ExerciseChecker.check("6  largestThree == [9, 8, 5]", largestThree().equals(List.of(9, 8, 5)));
        ExerciseChecker.check("7  bothEnds == [5, 2]", bothEnds().equals(List.of(5, 2)));
        ExerciseChecker.check("8  backwards == [2, 9, 1, 8, 3, 5]", backwards().equals(List.of(2, 9, 1, 8, 3, 5)));
        ExerciseChecker.check("9  fourInserts == [z, a, b, c]", fourInserts().equals(List.of("z", "a", "b", "c")));
        ExerciseChecker.check("10 shortestFruits == [kiwi, kiwi, pomme]", shortestFruits().equals(List.of("kiwi", "kiwi", "pomme")));

        ExerciseChecker.summary();
    }
}
