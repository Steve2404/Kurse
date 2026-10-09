package ch17_algorithms.projects.p08_heaps.solution;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Un tas binaire MIN, range dans un tableau : le parent de la case i est en (i - 1) / 2,
 * ses enfants en 2i + 1 et 2i + 2. Chaque parent est <= ses enfants : le minimum est toujours en 0.
 */
public final class MinHeap {

    private int[] heap = new int[16];
    private int size;

    // O(log n) : on ajoute au bout, puis on fait REMONTER tant que le parent est plus grand.
    public void add(int v) {
        if (size == heap.length) {
            heap = Arrays.copyOf(heap, size * 2);
        }
        heap[size] = v;
        siftUp(size);
        size++;
    }

    // O(log n) : le dernier prend la place de la racine, puis DESCEND vers le plus petit de ses enfants.
    public int poll() {
        int min = peek();
        size--;
        heap[0] = heap[size];
        siftDown(0);
        return min;
    }

    public int peek() {
        if (size == 0) {
            throw new NoSuchElementException("tas vide");
        }
        return heap[0];
    }

    public int size() {
        return size;
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap[parent] <= heap[i]) {
                return;
            }
            swap(i, parent);
            i = parent;
        }
    }

    // Piege : descendre vers le PLUS PETIT des deux enfants, sinon le plus petit se retrouve sous un plus grand.
    private void siftDown(int i) {
        while (true) {
            int left = 2 * i + 1;
            int right = left + 1;
            int smallest = i;
            if (left < size && heap[left] < heap[smallest]) {
                smallest = left;
            }
            if (right < size && heap[right] < heap[smallest]) {
                smallest = right;
            }
            if (smallest == i) {
                return;
            }
            swap(i, smallest);
            i = smallest;
        }
    }

    private void swap(int i, int j) {
        int t = heap[i];
        heap[i] = heap[j];
        heap[j] = t;
    }

    // Le tri par tas : n ajouts puis n retraits, O(n log n), sans tableau trie a l'avance.
    public static int[] heapSort(int[] a) {
        MinHeap h = new MinHeap();
        for (int v : a) {
            h.add(v);
        }
        int[] out = new int[a.length];
        for (int i = 0; i < out.length; i++) {
            out[i] = h.poll();
        }
        return out;
    }
}
