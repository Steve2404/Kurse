package ch9_collections.solutions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Corrige de l'exercice 5.
 */
public class Solution05_MergeKSortedLists {

    private static final class Cursor {
        final int listIndex;
        final int elementIndex;
        final List<List<Integer>> lists;

        Cursor(List<List<Integer>> lists, int listIndex, int elementIndex) {
            this.lists = lists;
            this.listIndex = listIndex;
            this.elementIndex = elementIndex;
        }

        int value() {
            return lists.get(listIndex).get(elementIndex);
        }

        Cursor next() {
            int nextElementIndex = elementIndex + 1;
            if (nextElementIndex >= lists.get(listIndex).size()) {
                return null;
            }
            return new Cursor(lists, listIndex, nextElementIndex);
        }
    }

    public static List<Integer> mergeKSortedLists(List<List<Integer>> lists) {
        PriorityQueue<Cursor> heap = new PriorityQueue<>(Comparator.comparingInt(Cursor::value));
        for (int i = 0; i < lists.size(); i++) {
            if (!lists.get(i).isEmpty()) {
                heap.offer(new Cursor(lists, i, 0));
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!heap.isEmpty()) {
            Cursor cursor = heap.poll();
            result.add(cursor.value());
            Cursor next = cursor.next();
            if (next != null) {
                heap.offer(next);
            }
        }
        return result;
    }
}