package collections.solutions;

import java.util.ArrayList;
import java.util.List;

/**
 * Corrige de l'exercice 1. A ne consulter qu'apres avoir essaye par
 * vous-meme dans collections.exercises.Exercise01_ListAlgorithms.
 */
public class Solution01_ListAlgorithms {

    public static <T> void rotate(List<T> list, int k) {
        int size = list.size();
        if (size == 0) {
            return;
        }
        int normalizedK = ((k % size) + size) % size;
        if (normalizedK == 0) {
            return;
        }
        List<T> copy = new ArrayList<>(list);
        for (int i = 0; i < size; i++) {
            list.set((i + normalizedK) % size, copy.get(i));
        }
    }

    public static <T> List<T> distinctPreservingOrder(List<T> list) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (!result.contains(item)) {
                result.add(item);
            }
        }
        return result;
    }
}