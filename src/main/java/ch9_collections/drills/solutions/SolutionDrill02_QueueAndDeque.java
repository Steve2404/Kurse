package ch9_collections.drills.solutions;

import ch9_collections.drills.Pantry;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Corrige du drill 2. A ne consulter qu'apres avoir essaye par
 * vous-meme dans ch9_collections.drills.exercises.Drill02_QueueAndDeque.
 */
public class SolutionDrill02_QueueAndDeque {

    public static List<Integer> fifo() {
        // File : on entre a la fin (offer), on sort par le debut (poll) -> le premier arrive sort le premier.
        Queue<Integer> queue = new ArrayDeque<>();
        for (int n : Pantry.NUMBERS) {
            queue.offer(n);
        }
        return List.of(queue.poll(), queue.poll());
    }

    public static List<Integer> lifo() {
        // Pile : push et pop travaillent tous deux au debut -> le dernier arrive sort le premier.
        Deque<Integer> stack = new ArrayDeque<>();
        for (int n : Pantry.NUMBERS) {
            stack.push(n);
        }
        return List.of(stack.pop(), stack.pop());
    }

    public static String peekEmpty() {
        // peek est "poli" : null sur une Deque vide, pas d'exception.
        return String.valueOf(new ArrayDeque<Integer>().peek());
    }

    public static String popEmpty() {
        // pop est un removeFirst deguise : NoSuchElementException sur vide.
        try {
            new ArrayDeque<Integer>().pop();
            return "aucune";
        } catch (NoSuchElementException e) {
            return e.getClass().getSimpleName();
        }
    }

    public static List<Integer> smallestThree() {
        // PriorityQueue : poll rend toujours le plus petit restant (ordre naturel).
        PriorityQueue<Integer> pq = new PriorityQueue<>(Pantry.NUMBERS);
        return List.of(pq.poll(), pq.poll(), pq.poll());
    }

    public static List<Integer> largestThree() {
        // reverseOrder inverse la priorite : le plus grand sort en premier.
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        pq.addAll(Pantry.NUMBERS);
        return List.of(pq.poll(), pq.poll(), pq.poll());
    }

    public static List<Integer> bothEnds() {
        // Une Deque se vide par les deux bouts.
        Deque<Integer> deque = new ArrayDeque<>(Pantry.NUMBERS);
        return List.of(deque.pollFirst(), deque.pollLast());
    }

    public static List<Integer> backwards() {
        // descendingIterator parcourt de la fin vers le debut sans rien retirer.
        Deque<Integer> deque = new ArrayDeque<>(Pantry.NUMBERS);
        List<Integer> result = new ArrayList<>();
        Iterator<Integer> it = deque.descendingIterator();
        while (it.hasNext()) {
            result.add(it.next());
        }
        return result;
    }

    public static List<String> fourInserts() {
        // First = debut, Last = fin ; offer et add ne different que par leur facon d'echouer.
        Deque<String> deque = new ArrayDeque<>();
        deque.offerLast("b");
        deque.offerFirst("a");
        deque.offerLast("c");
        deque.addFirst("z");
        return new ArrayList<>(deque);
    }

    public static List<String> shortestFruits() {
        // La priorite vient du Comparator : longueur, puis alphabet pour departager.
        PriorityQueue<String> pq = new PriorityQueue<>(
                Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
        pq.addAll(Pantry.FRUITS);
        return List.of(pq.poll(), pq.poll(), pq.poll());
    }
}
