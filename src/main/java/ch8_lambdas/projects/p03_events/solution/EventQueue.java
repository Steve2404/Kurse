package ch8_lambdas.projects.p03_events.solution;

/**
 * SOLUTION - une file de priorite en TAS BINAIRE dans un tableau : la racine (case 0) est le prochain evenement.
 * Enfants de i : 2i+1 et 2i+2 ; parent : (i-1)/2.
 */
public class EventQueue {

    private Event[] heap = new Event[8];
    private int size;

    public boolean isEmpty() {
        return size == 0;
    }

    public void push(Event e) {
        if (size == heap.length) {
            heap = java.util.Arrays.copyOf(heap, size * 2);
        }
        int i = size++;
        heap[i] = e;
        while (i > 0 && heap[i].before(heap[(i - 1) / 2])) {   // remonter
            swap(i, (i - 1) / 2);
            i = (i - 1) / 2;
        }
    }

    public Event pop() {
        Event top = heap[0];
        heap[0] = heap[--size];
        int i = 0;
        while (true) {                                         // redescendre vers le plus petit enfant
            int l = 2 * i + 1;
            int r = l + 1;
            int smallest = i;
            if (l < size && heap[l].before(heap[smallest])) {
                smallest = l;
            }
            if (r < size && heap[r].before(heap[smallest])) {
                smallest = r;
            }
            if (smallest == i) {
                return top;
            }
            swap(i, smallest);
            i = smallest;
        }
    }

    private void swap(int a, int b) {
        Event t = heap[a];
        heap[a] = heap[b];
        heap[b] = t;
    }
}
