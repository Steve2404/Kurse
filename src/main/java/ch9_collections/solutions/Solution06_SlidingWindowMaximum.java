package ch9_collections.solutions;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Corrige de l'exercice 6. A ne consulter qu'apres avoir essaye par
 * vous-meme dans collections.exercises.Exercise06_SlidingWindowMaximum.
 */
public class Solution06_SlidingWindowMaximum {

    public static List<Integer> maxSlidingWindow(int[] nums, int k) {
        // La Deque garde des INDICES aux valeurs decroissantes : le max est devant, on jette derriere les plus petits et devant ceux sortis de la fenetre. O(n).
        List<Integer> result = new ArrayList<>();
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < nums.length; i++) {
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) {
                deque.pollLast();
            }
            deque.offerLast(i);

            if (deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }

            if (i >= k - 1) {
                result.add(nums[deque.peekFirst()]);
            }
        }

        return result;
    }
}
