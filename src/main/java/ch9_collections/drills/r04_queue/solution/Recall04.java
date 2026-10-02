package ch9_collections.drills.r04_queue.solution;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * SOLUTION du drill de rappel 4 - Queue, Deque, PriorityQueue.
 */
public class Recall04 {

    public static void main(String[] args) {
        Queue<String> q = new LinkedList<>();
        q.offer("a");
        q.add("b");
        q.offer("c");
        System.out.println("D01 : " + q.peek() + " " + q.element() + " " + q.poll() + " " + q.remove() + " " + q);
        Queue<String> empty = new ArrayDeque<>();
        System.out.println("D02 : " + empty.peek() + " " + empty.poll() + " " + empty.offer("x") + " " + empty.size());
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println("D03 : " + stack + " " + stack.peek() + " " + stack.pop() + " " + stack);
        Deque<String> d = new ArrayDeque<>();
        d.offerFirst("b");
        d.offerLast("c");
        d.addFirst("a");
        d.addLast("d");
        System.out.println("D04 : " + d + " " + d.peekFirst() + d.peekLast() + " " + d.pollFirst() + d.pollLast() + " " + d + " " + d.removeFirst() + " " + d.getLast());
        PriorityQueue<Integer> pq = new PriorityQueue<>(List.of(5, 1, 4, 2));
        PriorityQueue<Integer> maxPq = new PriorityQueue<>(Collections.reverseOrder());
        maxPq.addAll(List.of(5, 1, 4, 2));
        StringBuilder sb = new StringBuilder();
        while (!pq.isEmpty()) {
            sb.append(pq.poll());
        }
        System.out.println("D05 : " + sb + " " + maxPq.poll() + maxPq.poll() + " " + maxPq.peek());
        PriorityQueue<String> byLen = new PriorityQueue<>((x, y) -> x.length() != y.length() ? x.length() - y.length() : x.compareTo(y));
        byLen.addAll(List.of("ccc", "a", "bb", "aa"));
        System.out.println("D06 : " + byLen.poll() + " " + byLen.poll() + " " + byLen.poll() + " " + byLen.size());
        Deque<String> trail = new ArrayDeque<>(List.of("x", "y", "z"));
        StringBuilder back = new StringBuilder();
        Iterator<String> rev = trail.descendingIterator();   // de la queue vers la tete
        while (rev.hasNext()) {
            back.append(rev.next());
        }
        System.out.println("D07 : " + back + " " + trail.contains("y") + " " + trail.removeLastOccurrence("y") + " " + trail);
    }
}
