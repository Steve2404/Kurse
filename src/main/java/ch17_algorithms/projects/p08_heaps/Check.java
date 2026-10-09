package ch17_algorithms.projects.p08_heaps;

import projectkit.TestKit;
import projectkit.TestKit.Mutant;

import java.util.List;

/**
 * Le correcteur : projet 8 (ne pas modifier). Enonce : TODO.md.
 * Lance-le tel quel pour verifier TON code et TES tests, ou avec l'argument "solution".
 * Les tests de reference contiennent des tests de VITESSE.
 */
public class Check {

    /** Les bugs que tes tests doivent attraper (INDICES.md dit, replie, ce que change chacun). */
    static final List<Mutant> MUTANTS = List.of(
            new Mutant("MinHeap.java", "int parent = (i - 1) / 2;", "int parent = i / 2;"),
            new Mutant("MinHeap.java", "if (right < size && heap[right] < heap[smallest]) {", "if (right < size && heap[right] < heap[i]) {"),
            new Mutant("MinHeap.java", "        heap[0] = heap[size];\n", "        heap[0] = heap[Math.max(0, size - 1)];\n"),
            new Mutant("MinHeap.java", "heap = Arrays.copyOf(heap, size * 2);", "heap = Arrays.copyOf(heap, size + 1);"),
            new Mutant("MinHeap.java", "        if (size == 0) {\n            throw new NoSuchElementException(\"tas vide\");", "        if (size < 0) {\n            throw new NoSuchElementException(\"tas vide\");"),
            new Mutant("Emergency.java", "Comparator.comparingInt(Patient::severity).reversed().thenComparingInt(Patient::arrival)", "Comparator.comparingInt(Patient::severity).reversed()"),
            new Mutant("Emergency.java", "Comparator.comparingInt(Patient::severity).reversed().thenComparingInt(Patient::arrival)", "Comparator.comparingInt(Patient::severity).thenComparingInt(Patient::arrival)"),
            new Mutant("Heaps.java", "if (best.size() > k) {", "if (best.size() >= k) {"),
            new Mutant("Heaps.java", "for (int i = out.length - 1; i >= 0; i--) {\n            out[i] = best.poll();", "for (int i = 0; i < out.length; i++) {\n            out[i] = best.poll();"),
            new Mutant("Heaps.java", "            if (next < lists.get(h[1]).size()) {", "            if (next < lists.get(h[1]).size() - 1) {"),
            new Mutant("MedianFinder.java", "        high.add(low.poll());\n        if (high.size() > low.size()) {", "        if (high.size() > low.size()) {"),
            new Mutant("MedianFinder.java", "return ((long) low.peek() + high.peek()) / 2.0;", "return (low.peek() + high.peek()) / 2.0;"),
            new Mutant("MedianFinder.java", "new PriorityQueue<>(Collections.reverseOrder())", "new PriorityQueue<>()"));

    static final List<String> API_CODE = List.of(
            "final class MinHeap", "void add(int v)", "int poll()", "int peek()", "static int[] heapSort(int[] a)",
            "private void siftUp(int", "private void siftDown(int",
            "record Patient(String name, int severity, int arrival)", "final class Emergency", "void arrive(Patient p)", "Patient next()",
            "int waitingCount()", "final class Heaps", "static int[] topK(int[] a, int k)",
            "static List<Integer> mergeSorted(List<List<Integer>> lists)", "final class MedianFinder", "double median()",
            "PriorityQueue", "Comparator", "!Arrays.sort", "!Collections.sort", "!.sorted(");

    static final List<String> API_TESTS = List.of(
            "@Test", "assertEquals(", "assertArrayEquals(", "assertThrows(", "assertTimeoutPreemptively(",
            "!System.out", "!Thread.sleep");

    public static void main(String[] args) throws Exception {
        TestKit.checkProject(Check.class, args, 12, MUTANTS, API_CODE, API_TESTS);
    }
}
